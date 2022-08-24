/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart strategy implementation for deleting product offering from cart.
 *
 * @since 2102
 */
public class TmaDeleteFromCartStrategy implements TmaCartStrategy
{
	private static final long DEFAULT_ENTRY_NUMBER = -1;

	private CommerceUpdateCartEntryStrategy updateCartEntryStrategy;
	private TmaAbstractOrderEntryService abstractOrderEntryService;
	private ModelService modelService;

	public TmaDeleteFromCartStrategy(final CommerceUpdateCartEntryStrategy updateCartEntryStrategy,
			final TmaAbstractOrderEntryService abstractOrderEntryService, final ModelService modelService)
	{
		this.updateCartEntryStrategy = updateCartEntryStrategy;
		this.abstractOrderEntryService = abstractOrderEntryService;
		this.modelService = modelService;
	}

	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModifications = new ArrayList<>();

		for (final CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{
			validateEntryNumber(commerceCartParameter);
			commerceCartParameter.setQuantity(0);
			commerceCartModifications.addAll(removeEntry(commerceCartParameter));
		}

		return commerceCartModifications;
	}

	/**
	 * Traverses the hierarchical structure and removes the specified entry and all its children
	 *
	 * @param parameter
	 * 		contains attribute for cart update
	 * @return list {@link CommerceCartModification}
	 * @throws CommerceCartModificationException
	 * 		if the entry number does not exist in cart
	 */
	protected List<CommerceCartModification> removeEntry(@Nonnull final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		final List<CommerceCartParameter> entriesToBeRemoved = prepareEntriesForRemoval(parameter, false);
		final AbstractOrderEntryModel parentEntry = getParentEntry(parameter);
		final List<CommerceCartModification> modification = removeEntries(entriesToBeRemoved);
		modification.addAll(removeEmptyParentBpos(parentEntry, parameter.isEnableHooks()));
		return modification;
	}

	private List<CommerceCartModification> removeEntries(final List<CommerceCartParameter> parameters)
			throws CommerceCartModificationException
	{

		final List<CommerceCartModification> result = new ArrayList<>();

		for (CommerceCartParameter parameter : parameters)
		{
			result.add(getUpdateCartEntryStrategy().updateQuantityForCartEntry(parameter));
		}

		return result;
	}

	private List<CommerceCartParameter> prepareEntriesForRemoval(@Nonnull final CommerceCartParameter parameter,
			final Boolean isParentEntryRemoved)
	{
		final AbstractOrderEntryModel entry = getAbstractOrderEntryService().getEntryBy(parameter.getCart(),
				(int) parameter.getEntryNumber());

		if (entry == null)
		{
			throw new IllegalArgumentException(String.format("No entry found in cart '%s' with entry number '%d'",
					parameter.getCart().getCode(), parameter.getEntryNumber()));
		}

		final AbstractOrderEntryModel parentEntry = entry.getMasterEntry();
		if (parentEntry != null && parentEntry.getProduct() instanceof TmaFixedBundledProductOfferingModel && Boolean.FALSE
				.equals(isParentEntryRemoved))
		{
			throw new IllegalArgumentException(String.format("Can not remove the fixed bundled component found in cart '%s' with "
					+ "entry number " + "'%d'", parameter.getCart().getCode(), parameter.getEntryNumber()));
		}

		final List<CommerceCartParameter> result = new ArrayList<>();
		result.add(parameter);

		for (AbstractOrderEntryModel childEntry : entry.getChildEntries())
		{
			final CommerceCartParameter updateParameter = createCommerceCartParameter(parameter.getCart(), parameter.isEnableHooks(),
					childEntry);

			result.addAll(prepareEntriesForRemoval(updateParameter, true));
		}

		result.sort((firstCommerceCartParameter, secondCommerceCartParameter) -> {
			final Optional<AbstractOrderEntryModel> firstAbstractOrderEntryModel = firstCommerceCartParameter.getCart().getEntries()
					.stream()
					.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getEntryNumber() == firstCommerceCartParameter
							.getEntryNumber())
					.findFirst();

			final Optional<AbstractOrderEntryModel> secondAbstractOrderEntryModel = secondCommerceCartParameter.getCart()
					.getEntries().stream()
					.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getEntryNumber() == secondCommerceCartParameter
							.getEntryNumber())
					.findFirst();

			return secondAbstractOrderEntryModel.get().getEntryNumber() - firstAbstractOrderEntryModel.get().getEntryNumber();
		});

		return result;
	}

	/**
	 * Checks if the entry number is valid and exists in the cart
	 *
	 * @param parameter
	 * 		contains attributes used for cart entry updates
	 * @throws CommerceCartModificationException
	 * 		if the cart does not have the entry group number provided in the parameter
	 */
	private void validateEntryNumber(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		final int entryNumber = (int) parameter.getEntryNumber();
		final List<AbstractOrderEntryModel> entries = parameter.getCart().getEntries();

		if (entryNumber == DEFAULT_ENTRY_NUMBER || CollectionUtils.isEmpty(entries))
		{
			return;
		}

		if (entries.size() <= entryNumber)
		{
			throw new CommerceCartModificationException(
					"Entry #" + entryNumber + " was not found in cart " + parameter.getCart().getCode() + ".");
		}

		if (getAbstractOrderEntryService().getEntryBy(parameter.getCart(), (int) parameter.getEntryNumber()) == null)
		{
			throw new CommerceCartModificationException(
					"Entry #" + entryNumber + " was not found in cart " + parameter.getCart().getCode() + ".");
		}
	}

	private List<CommerceCartModification> removeEmptyParentBpos(final AbstractOrderEntryModel entry,
			final boolean areHooksEnabled) throws CommerceCartModificationException
	{
		if (entry == null || CollectionUtils.isNotEmpty(entry.getChildEntries()))
		{
			return Collections.emptyList();
		}

		final List<CommerceCartModification> commerceCartModifications = new ArrayList<>();

		final CommerceCartParameter parameter = createCommerceCartParameter((CartModel) entry.getOrder(), areHooksEnabled, entry);
		commerceCartModifications.add(getUpdateCartEntryStrategy().updateQuantityForCartEntry(parameter));

		final AbstractOrderEntryModel parentEntry = entry.getMasterEntry();

		if (parentEntry == null)
		{
			return commerceCartModifications;
		}

		getModelService().refresh(parentEntry);

		commerceCartModifications.addAll(removeEmptyParentBpos(parentEntry, areHooksEnabled));

		return commerceCartModifications;
	}

	private CommerceCartParameter createCommerceCartParameter(final CartModel cart, final boolean areHooksEnabled,
			final AbstractOrderEntryModel childEntry)
	{
		final CommerceCartParameter updateParameter = new CommerceCartParameter();
		updateParameter.setCart(cart);
		updateParameter.setEnableHooks(areHooksEnabled);
		updateParameter.setQuantity(0L);
		updateParameter.setEntryNumber(childEntry.getEntryNumber());
		updateParameter.setAutomaticallyAdded(childEntry.getAutomaticallyAdded());
		return updateParameter;
	}

	private AbstractOrderEntryModel getParentEntry(final CommerceCartParameter parameter)
	{
		final AbstractOrderEntryModel entry = getAbstractOrderEntryService()
				.getEntryBy(parameter.getCart(), (int) parameter.getEntryNumber());

		if (entry == null || entry.getMasterEntry() == null)
		{
			return null;
		}

		return entry.getMasterEntry();
	}

	protected CommerceUpdateCartEntryStrategy getUpdateCartEntryStrategy()
	{
		return updateCartEntryStrategy;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
