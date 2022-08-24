/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;


import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.hook.TmaCartHookHelper;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOperationalProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.subscription.BillingTimeService;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionCommerceCartService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * {@link TmaProductOfferingModel} specific add to cart operations.
 *
 * @since 6.7
 */
public class TmaPoAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private EntryGroupService entryGroupService;

	private TmaPoService tmaPoService;
	private ModelService modelService;
	private EnumerationService enumerationService;

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	private TmaCompatibilityPolicyEngine compatibilityPolicyEngine;

	private CartService cartService;

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	private TmaCartHookHelper tmaCartHookHelper;

	private String defaultProcessType;
	private TmaCommercePriceService commercePriceService;

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	private BillingTimeService billingTimeService;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private SubscriptionCommerceCartService subscriptionCommerceCartService;


	public TmaPoAddToCartMethodHook(final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	/**
	 * Perform validation operations for cart entries containing {@link TmaProductOfferingModel}.
	 *
	 * @param parameters
	 * 		A parameter object - new cart entry details
	 * @throws CommerceCartModificationException
	 * 		in case the parent is not valid
	 */
	@Override
	public void beforeAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException
	{
		if (StringUtils.isEmpty(parameters.getProcessType()))
		{
			parameters.setProcessType(defaultProcessType);
		}

		if (parameters.getProduct() instanceof TmaOperationalProductOfferingModel)
		{
			return;
		}

		if (StringUtils.isNotEmpty(parameters.getBpoCode()))
		{
			final TmaBundledProductOfferingModel parentBundledOffering = (TmaBundledProductOfferingModel) getTmaPoService()
					.getPoForCode(parameters.getBpoCode());
			if (!getTmaPoService().isValidParent((TmaProductOfferingModel) parameters.getProduct(), parentBundledOffering))
			{
				throw new CommerceCartModificationException("Invalid BPO parent code:" + parameters.getBpoCode()
						+ " for entry with product code " + parameters.getProduct().getCode());
			}
		}

		if (!checkSoldIndividuallyFlag(parameters))
		{
			throw new CommerceCartModificationException(
					"Product: " + parameters.getProduct().getCode() + ",cannot be sold individually ");
		}
	}

	@Override
	public void afterAddToCart(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		// no implementation needed
	}

	/**
	 * Marks all cart entries that belong to the same bpo as "not calculated", as the prices within a bpo may vary
	 * depending on the product offerings bought as part of the same bpo and a re-calculation of the whole bpo entries
	 * (and all carts that contain entries of the affected bundle) is necessary.
	 *
	 * @param cart
	 * 		the cart to recalculate entries in.
	 * @param entryGroupNumber
	 * 		entry group number to retrieve the entries which need to be recalculated
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected void invalidateBpoEntries(@Nonnull final CartModel cart, final int entryGroupNumber)
	{
		final EntryGroup rootEntryGroup = getEntryGroupService().getRoot(cart, entryGroupNumber);
		final List<Integer> treeGroupIds = getEntryGroupService().getNestedGroups(rootEntryGroup).stream()
				.filter(group -> GroupType.B2CTELCO_BPO.equals(group.getGroupType())).map(EntryGroup::getGroupNumber)
				.collect(Collectors.toList());
		final Set<ItemModel> models = new HashSet<>();
		cart.getEntries().stream().filter(cartEntry -> cartEntry.getEntryGroupNumbers() != null
				&& CollectionUtils.containsAny(treeGroupIds, cartEntry.getEntryGroupNumbers())).forEach(cartEntry ->
		{
			cartEntry.setCalculated(Boolean.FALSE);
			cartEntry.getOrder().setCalculated(Boolean.FALSE);
			models.add(cartEntry);
			models.add(cartEntry.getOrder());
		});
		getModelService().saveAll(models);
	}

	private boolean checkSoldIndividuallyFlag(final CommerceCartParameter parameters)
	{
		final ProductModel productModel = parameters.getProduct();
		return StringUtils.isBlank(parameters.getBpoCode()) ? productModel.getSoldIndividually() : Boolean.TRUE;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	protected TmaCompatibilityPolicyEngine getCompatibilityPolicyEngine()
	{
		return compatibilityPolicyEngine;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	protected BillingTimeService getBillingTimeService()
	{
		return billingTimeService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected SubscriptionCommerceCartService getSubscriptionCommerceCartService()
	{
		return subscriptionCommerceCartService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	protected CommerceCartCalculationStrategy getCommerceCartCalculationStrategy()
	{
		return commerceCartCalculationStrategy;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	protected TmaCartHookHelper getTmaCartHookHelper()
	{
		return tmaCartHookHelper;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Required
	public void setEntryGroupService(EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}

	@Required
	public void setTmaPoService(TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setEnumerationService(EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	@Required
	public void setCompatibilityPolicyEngine(
			TmaCompatibilityPolicyEngine compatibilityPolicyEngine)
	{
		this.compatibilityPolicyEngine = compatibilityPolicyEngine;
	}

	@Required
	public void setCartService(CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	@Required
	public void setCommerceCartCalculationStrategy(
			CommerceCartCalculationStrategy commerceCartCalculationStrategy)
	{
		this.commerceCartCalculationStrategy = commerceCartCalculationStrategy;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	@Required
	public void setTmaCartHookHelper(TmaCartHookHelper tmaCartHookHelper)
	{
		this.tmaCartHookHelper = tmaCartHookHelper;
	}

	@Required
	public void setDefaultProcessType(String defaultProcessType)
	{
		this.defaultProcessType = defaultProcessType;
	}

	@Required
	public void setCommercePriceService(TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since="2105")
	@Required
	public void setBillingTimeService(BillingTimeService billingTimeService)
	{
		this.billingTimeService = billingTimeService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Required
	public void setSubscriptionCommerceCartService(
			SubscriptionCommerceCartService subscriptionCommerceCartService)
	{
		this.subscriptionCommerceCartService = subscriptionCommerceCartService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
