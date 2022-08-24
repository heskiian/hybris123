/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.impl.CommerceCartMergingStrategy;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartMergingStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.microsoft.sqlserver.jdbc.StringUtils;



/**
 * B2ctelcoCommerceCartMergingStrategy, an implementation of CommerceCartMergingStrategy to merge anonymous cart with
 * user cart.
 *
 * @since 1911
 */
public class B2ctelcoCommerceCartMergingStrategy extends DefaultCommerceCartMergingStrategy
		implements CommerceCartMergingStrategy, TmaCartStrategy

{
	private static final Logger LOG = Logger.getLogger(B2ctelcoCommerceCartMergingStrategy.class.getName());

	private final TmaCustomerInventoryService customerInventoryService;

	private final TmaCommerceAddToCartStrategy commerceAddToCartStrategy;

	private final TmaUpdateCartStrategy tmaUpdateCartStrategy;

	public B2ctelcoCommerceCartMergingStrategy(final TmaCustomerInventoryService customerInventoryService,
			final TmaCommerceAddToCartStrategy commerceAddToCartStrategy, final TmaUpdateCartStrategy tmaUpdateCartStrategy)
	{
		this.customerInventoryService = customerInventoryService;
		this.commerceAddToCartStrategy = commerceAddToCartStrategy;
		this.tmaUpdateCartStrategy = tmaUpdateCartStrategy;
	}

	@Override
	public void mergeCarts(final CartModel fromCart, final CartModel toCart, final List<CommerceCartModification> modifications)
			throws CommerceCartMergingException
	{
		validationBeforeMerge(fromCart, toCart, modifications, getUserService().getCurrentUser());

		validateFromCartEligibilityContext(fromCart);

		final List<AbstractOrderEntryModel> entriesToBeMoved = fromCart.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getMasterEntry() == null).collect(Collectors.toList());

		for (final AbstractOrderEntryModel entry : entriesToBeMoved)
		{
			modifications.add(mergeEntryWithCart(entry, toCart));
		}

		toCart.setCalculated(Boolean.FALSE);

		getModelService().save(toCart);

		recalculateCart(toCart);

		fromCart.setEntries(Collections.emptyList());
		getModelService().remove(fromCart);
	}

	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		CommerceCartRestoration restoration;
		final CommerceCartParameter parameter = commerceCartParameterList.get(0);
		parameter.setEnableHooks(true);
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		final CartModel fromCart = getCommerceCartService().getCartForGuidAndSiteAndUser(parameter.getFromCartGUID(),
				currentBaseSite, getUserService().getAnonymousUser());
		try
		{
			restoration = getCommerceCartService().restoreCart(parameter);
			mergeCarts(fromCart, parameter.getCart(), restoration.getModifications());
			parameter.setCart(parameter.getCart());
		}
		catch (CommerceCartMergingException | CommerceCartRestorationException ex)
		{
			throw new CommerceCartModificationException("Could not merge carts " + ex.getMessage(), ex);
		}
		return restoration.getModifications();
	}

	protected void recalculateCart(final CartModel toCart)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setCart(toCart);
		try
		{
			getCommerceCartCalculationStrategy().recalculateCart(parameter);
		}
		catch (final IllegalStateException ex)
		{
			LOG.error("Failed to recalculate order [" + toCart.getCode() + "]", ex);
		}
	}

	protected void validateFromCartEligibilityContext(final CartModel fromCart) throws CommerceCartMergingException
	{
		final Set<TmaProcessType> eligibleProcessesForUser = getCustomerInventoryService().retrieveProcesses();

		for (final AbstractOrderEntryModel entry : fromCart.getEntries())
		{
			if (!eligibleProcessesForUser.contains(entry.getProcessType()))
			{
				throw new CommerceCartMergingException("User is not eligible for one or more cart entries");
			}
		}
	}

	@Override
	protected CommerceCartModification mergeEntryWithCart(final AbstractOrderEntryModel entry, final CartModel toCart)
			throws CommerceCartMergingException
	{
		final AbstractOrderEntryModel entryToMerge = getEntryMergeStrategy().getEntryToMerge(toCart.getEntries(), entry);

		if (entryToMerge == null)
		{
			final AbstractOrderEntryModel clonedEntry = getModelService().clone(entry, entry.getClass());

			final CommerceCartModification modification = addEntryToCart(toCart, entry, null);

			getModelService().detach(clonedEntry);
			clonedEntry.setOrder(toCart);
			updateEntryNumber(clonedEntry, toCart);

			return modification;
		}
		else
		{
			final CommerceCartParameter updateQuantityParameter = new CommerceCartParameter();
			updateQuantityParameter.setCart(toCart);
			updateQuantityParameter.setEnableHooks(true);
			updateQuantityParameter.setNewQuantity(entryToMerge.getQuantity() + entry.getQuantity());
			updateQuantityParameter.setEntryNumber(entryToMerge.getEntryNumber().longValue());
			try
			{
				return getTmaUpdateCartStrategy().processCartAction(Collections.singletonList(updateQuantityParameter)).get(0);
			}
			catch (final CommerceCartModificationException e)
			{
				throw new CommerceCartMergingException("Exception during cart merge", e);
			}
		}
	}

	/**
	 * Adds the provided entry to the given cart.
	 *
	 * @param cart
	 * 		The cart
	 * @param entry
	 * 		The entry to be added
	 * @param parentEntry
	 * 		The parent of the entry
	 * @return The modifications containing the root entry
	 * @throws CommerceCartMergingException
	 * 		If errors occur during add to cart
	 */
	protected CommerceCartModification addEntryToCart(final CartModel cart, final AbstractOrderEntryModel entry,
			final AbstractOrderEntryModel parentEntry) throws CommerceCartMergingException
	{

		final CommerceCartParameter addProductParameter = createCommerceCartParameter(cart, entry, parentEntry);
		final CommerceCartModification commerceCartModification;

		try
		{
			commerceCartModification = getCommerceAddToCartStrategy().addToCart(addProductParameter);
		}
		catch (final CommerceCartModificationException e)
		{
			throw new CommerceCartMergingException("Exception during cart merge", e);
		}

		if (CollectionUtils.isEmpty(entry.getChildEntries()))
		{
			return commerceCartModification;
		}

		for (AbstractOrderEntryModel childEntry : entry.getChildEntries())
		{
			addEntryToCart(cart, childEntry, commerceCartModification.getEntry());
		}

		return commerceCartModification;
	}

	/**
	 * Creates the new cart parameter used for adding the new entry to cart.
	 *
	 * @param cart
	 * 		Current cart
	 * @param entry
	 * 		The entry to be added
	 * @param parentEntry
	 * 		The parent entry
	 * @return {@link CommerceCartParameter} for the new entry
	 */
	protected CommerceCartParameter createCommerceCartParameter(final CartModel cart, final AbstractOrderEntryModel entry,
			final AbstractOrderEntryModel parentEntry)
	{
		final CommerceCartParameter addProductParameter = new CommerceCartParameter();

		addProductParameter.setCart(cart);
		addProductParameter.setEnableHooks(true);
		addProductParameter.setProduct(entry.getProduct());
		addProductParameter.setUnit(entry.getUnit());
		addProductParameter.setQuantity(entry.getQuantity());
		addProductParameter.setEntryNumber(-1L);
		addProductParameter.setCreateNewEntry(false);
		addProductParameter.setBpoCode(entry.getBpo() != null ? entry.getBpo().getCode() : StringUtils.EMPTY);
		addProductParameter.setProcessType(entry.getProcessType().toString());
		addProductParameter.setUser(getUserService().getCurrentUser());
		addProductParameter.setParentEntry(parentEntry);
		setRegionFromCartEntry(entry, addProductParameter);

		return addProductParameter;
	}

	private void setRegionFromCartEntry(final AbstractOrderEntryModel clonedEntry, final CommerceCartParameter addProductParameter)
	{
		final RegionModel regionModel = clonedEntry.getRegion();
		if (!ObjectUtils.isEmpty(regionModel))
		{
			final TmaRegionPlace region = new TmaRegionPlace();
			region.setRegion(regionModel);
			region.setRole(TmaPlaceRoleType.PRODUCT_REGION);
			addProductParameter.setPlaces(Collections.singletonList(region));
		}
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	protected TmaCommerceAddToCartStrategy getCommerceAddToCartStrategy()
	{
		return commerceAddToCartStrategy;
	}

	protected TmaUpdateCartStrategy getTmaUpdateCartStrategy()
	{
		return tmaUpdateCartStrategy;
	}

}
