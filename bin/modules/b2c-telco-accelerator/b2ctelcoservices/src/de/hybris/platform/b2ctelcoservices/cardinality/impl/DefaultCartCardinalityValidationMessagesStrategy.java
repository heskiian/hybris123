/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link TmaValidationMessagesStrategy} to set the list of validation objects of cardinality type
 *
 * @since 2011
 */
public class DefaultCartCardinalityValidationMessagesStrategy implements TmaValidationMessagesStrategy
{
	private static final String CARDINALITY = "CARDINALITY";

	private TmaCartValidationBuilder cartValidationBuilder;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService entryGroupService;

	private ModelService modelService;

	public DefaultCartCardinalityValidationMessagesStrategy(final TmaCartValidationBuilder cartValidationBuilder,
			final TmaEntryGroupService entryGroupService, final ModelService modelService)
	{
		this.cartValidationBuilder = cartValidationBuilder;
		this.entryGroupService = entryGroupService;
		this.modelService = modelService;
	}

	@Override
	public void setValidationMessagesOn(final CartModel cartModel, final List<String> messages)
	{
		// implementation not needed
	}

	@Override
	public boolean shouldUpdateValidationMessages(final EntryGroup entryGroup, final List<String> errorMessages)
	{
		if (CollectionUtils.isEmpty(errorMessages))
		{
			return false;
		}

		return CollectionUtils.isEmpty(entryGroup.getValidationMessages()) || !entryGroup.getValidationMessages().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet()).containsAll(errorMessages);
	}

	@Override
	public boolean shouldUpdateValidationMessages(final CartEntryModel cartEntry, final List<String> errorMessages)
	{
		if (CollectionUtils.isEmpty(errorMessages))
		{
			return false;
		}

		return CollectionUtils.isEmpty(cartEntry.getCartEntryValidations()) || !cartEntry.getCartEntryValidations().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet()).containsAll(errorMessages);
	}

	@Override
	public void setValidationMessagesOn(final EntryGroup entryGroup, final List<String> messages)
	{
		if (CollectionUtils.isEmpty(messages))
		{
			return;
		}

		entryGroup.setErroneous(true);

		if (entryGroup.getValidationMessages() == null)
		{
			entryGroup.setValidationMessages(new HashSet<>());
		}

		entryGroup.getValidationMessages().addAll(getCartValidationBuilder().createCartValidations(CARDINALITY, messages));
	}

	@Override
	public void setValidationMessagesOn(final CartEntryModel cartEntry, final List<String> messages)
	{
		if (CollectionUtils.isEmpty(messages))
		{
			return;
		}

		if (cartEntry.getCartEntryValidations() == null)
		{
			cartEntry.setCartEntryValidations(new HashSet<>());
		}

		final Set<TmaCartValidationModel> validationMessages = new HashSet<>(cartEntry.getCartEntryValidations());
		validationMessages.addAll(getCartValidationBuilder().createCartValidations(CARDINALITY, messages));
		cartEntry.setCartEntryValidations(validationMessages);

		getModelService().save(cartEntry);
	}

	@Override
	public void cleanupValidationMessagesOn(final CartModel cartModel)
	{
		// no implementation needed
	}

	@Override
	public void cleanupValidationMessagesOn(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup)
	{
		if (CollectionUtils.isEmpty(entryGroup.getValidationMessages()))
		{
			return;
		}

		final List<TmaCartValidationModel> cardinalityValidations = entryGroup.getValidationMessages().stream()
				.filter((TmaCartValidationModel validation) -> CARDINALITY.equals(validation.getCode()))
				.collect(Collectors.toList());

		entryGroup.setErroneous(entryGroup.getValidationMessages().stream()
				.anyMatch((TmaCartValidationModel validation) -> !CARDINALITY.equals(validation.getCode())));

		entryGroup.setValidationMessages(entryGroup.getValidationMessages().stream()
				.filter((TmaCartValidationModel validation) -> !CARDINALITY.equals(validation.getCode()))
				.collect(Collectors.toSet()));

		getEntryGroupService().updateEntryGroup(abstractOrder, entryGroup);
		getModelService().removeAll(cardinalityValidations);
	}

	@Override
	public void cleanupValidationMessagesOn(final CartEntryModel cartEntry)
	{
		if (CollectionUtils.isEmpty(cartEntry.getCartEntryValidations()))
		{
			return;
		}

		final List<TmaCartValidationModel> cardinalityValidations = cartEntry.getCartEntryValidations().stream()
				.filter((TmaCartValidationModel validation) -> CARDINALITY.equals(validation.getCode()))
				.collect(Collectors.toList());

		cartEntry.setCartEntryValidations(cartEntry.getCartEntryValidations().stream()
				.filter((TmaCartValidationModel validation) -> !CARDINALITY.equals(validation.getCode()))
				.collect(Collectors.toSet()));

		getModelService().save(cartEntry);
		getModelService().removeAll(cardinalityValidations);
	}

	protected TmaCartValidationBuilder getCartValidationBuilder()
	{
		return cartValidationBuilder;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected TmaEntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}

