/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.hook.TmaUpdateCartHook;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPriceContextService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Cart strategy implementation for updating product offering in cart.
 *
 * @since 1911
 */
public class TmaUpdateCartStrategy implements TmaCartStrategy
{
	private static final long DEFAULT_ENTRY_NUMBER = -1;

	private final CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy;
	private final TmaCommerceCartResourceService commerceCartResourceService;
	private final List<TmaUpdateCartHook> commerceUpdateCartHooks;
	private final ConfigurationService configurationService;
	private final CartService cartService;
	private final CommerceCartCalculationStrategy calculationStrategy;
	private final TmaCommercePriceService commercePriceService;
	private TmaPriceContextService tmaPriceContextService;
	private EnumerationService enumerationService;
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaUpdateCartStrategy(final CommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy,
			final TmaCommerceCartResourceService commerceCartResourceService,
			final List<TmaUpdateCartHook> commerceUpdateCartHooks, final ConfigurationService configurationService,
			final CartService cartService, final CommerceCartCalculationStrategy calculationStrategy,
			final TmaCommercePriceService commercePriceService, final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.commerceUpdateCartEntryStrategy = commerceUpdateCartEntryStrategy;
		this.commerceCartResourceService = commerceCartResourceService;
		this.commerceUpdateCartHooks = commerceUpdateCartHooks;
		this.configurationService = configurationService;
		this.cartService = cartService;
		this.calculationStrategy = calculationStrategy;
		this.commercePriceService = commercePriceService;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModifications = new ArrayList<>();
		for (final CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{

			if (commerceCartParameter.getPointOfService() != null)
			{
				getCommerceUpdateCartEntryStrategy().updatePointOfServiceForCartEntry(commerceCartParameter);
			}
			if (commerceCartParameter.getEntryNumber() != DEFAULT_ENTRY_NUMBER)
			{
				getCommerceUpdateCartEntryStrategy().beforeUpdateEntry(commerceCartParameter);

			}
			else
			{
				beforeUpdateCart(commerceCartParameter);
			}

			validatePriceForCartEntry(commerceCartParameter);

			final CommerceCartModification commerceCartModification = createNewCommerceCartModification(commerceCartParameter);
			getCommerceCartResourceService().updateResources(commerceCartParameter, commerceCartModification);


			if (commerceCartParameter.getEntryNumber() != DEFAULT_ENTRY_NUMBER)
			{
				getCommerceUpdateCartEntryStrategy().afterUpdateEntry(commerceCartParameter, commerceCartModification);
			}
			else
			{
				afterUpdateCart(commerceCartParameter, commerceCartModification);
			}

			commerceCartModifications.add(commerceCartModification);
			getCalculationStrategy().recalculateCart(commerceCartParameter);

		}

		return commerceCartModifications;
	}

	protected void beforeUpdateCart(final CommerceCartParameter parameter)
	{
		if (getCommerceUpdateCartHooks() != null && (parameter.isEnableHooks() && getConfigurationService().getConfiguration()
				.getBoolean(B2ctelcoservicesConstants.UPDATECARTHOOK_ENABLED, true)))
		{
			for (final TmaUpdateCartHook updateCartHook : getCommerceUpdateCartHooks())
			{
				updateCartHook.beforeUpdateCart(parameter);
			}
		}
	}

	protected void afterUpdateCart(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		if (getCommerceUpdateCartHooks() != null && parameter.isEnableHooks() && getConfigurationService().getConfiguration()
				.getBoolean(B2ctelcoservicesConstants.UPDATECARTHOOK_ENABLED, true))
		{
			for (final TmaUpdateCartHook updateCartHook : getCommerceUpdateCartHooks())
			{
				updateCartHook.afterUpdateCart(parameter, result);
			}
		}
	}

	protected CommerceCartModification createNewCommerceCartModification(final CommerceCartParameter parameter)
	{
		final CommerceCartModification commerceCartModification = new CommerceCartModification();
		commerceCartModification.setProduct(parameter.getProduct());

		if (parameter.getEntryNumber() == DEFAULT_ENTRY_NUMBER)
		{
			return commerceCartModification;
		}

		final AbstractOrderEntryModel currentOrderEntry = getCartService().getEntryForNumber(parameter.getCart(),
				(int) parameter.getEntryNumber());

		if (currentOrderEntry != null)
		{
			commerceCartModification.setEntry(currentOrderEntry);
		}

		commerceCartModification.setStatusCode(CommerceCartModificationStatus.SUCCESS);

		return commerceCartModification;
	}

	/**
	 * Validates if price is available for given parameters before updating the {@link AbstractOrderEntryModel}
	 *
	 * @param commerceCartParameter
	 * 		attributes of CommerceCartParameter is used to find the price
	 */
	private void validatePriceForCartEntry(final CommerceCartParameter commerceCartParameter)
	{
		if (commerceCartParameter.getEntryNumber() >= 0)
		{
			final AbstractOrderEntryModel currentOrderEntry = getCartService().getEntryForNumber(commerceCartParameter.getCart(),
					(int) commerceCartParameter.getEntryNumber());

			if (!CollectionUtils.isEmpty(commerceCartParameter.getPlaces()))
			{
				final Optional<TmaPlace> newRegionPlace = commerceCartParameter.getPlaces().stream()
						.filter(tmaPlace -> tmaPlace.getRole().equals(TmaPlaceRoleType.PRODUCT_REGION)).findFirst();

				newRegionPlace.ifPresent(place -> currentOrderEntry.setRegion(((TmaRegionPlace) place).getRegion()));
			}
			if (!StringUtils.isEmpty(commerceCartParameter.getProcessType()))
			{
				currentOrderEntry.setProcessType(
						getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE, commerceCartParameter.getProcessType()));
			}
			if (!ObjectUtils.isEmpty(commerceCartParameter.getSubscriptionInfo()))
			{
				currentOrderEntry.setSubscriptionInfo(commerceCartParameter.getSubscriptionInfo());
			}

			final PriceRowModel bestApplicablePrice = getCommercePriceService().getBestApplicablePrice(currentOrderEntry);
			final ProductModel productModel = currentOrderEntry.getProduct();

			if (ObjectUtils.isEmpty(bestApplicablePrice) && !StringUtils.equalsIgnoreCase(TmaBundledProductOfferingModel._TYPECODE,
					currentOrderEntry.getProduct().getItemtype()))
			{
				throw new IllegalArgumentException(
						"Found no prices for product " + productModel.getCode() + " for cart "
								+ commerceCartParameter.getCart().getCode());
			}
		}
	}

	protected TmaCommerceCartResourceService getCommerceCartResourceService()
	{
		return commerceCartResourceService;
	}

	protected DefaultTmaCommerceUpdateCartEntryStrategy getCommerceUpdateCartEntryStrategy()
	{
		return (DefaultTmaCommerceUpdateCartEntryStrategy) commerceUpdateCartEntryStrategy;
	}

	protected List<TmaUpdateCartHook> getCommerceUpdateCartHooks()
	{
		return commerceUpdateCartHooks;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected CommerceCartCalculationStrategy getCalculationStrategy()
	{
		return calculationStrategy;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	protected TmaPriceContextService getTmaPriceContextService()
	{
		return tmaPriceContextService;
	}

	public void setTmaPriceContextService(final TmaPriceContextService tmaPriceContextService)
	{
		this.tmaPriceContextService = tmaPriceContextService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
