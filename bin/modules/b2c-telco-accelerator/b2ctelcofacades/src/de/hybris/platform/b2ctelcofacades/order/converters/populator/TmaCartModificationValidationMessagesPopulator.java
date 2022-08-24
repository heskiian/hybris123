/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commercefacades.order.converters.populator.CartModificationPopulator;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populator for {@link CommerceCartModification} attribute validation messages
 *
 * @since 1911
 */
public class TmaCartModificationValidationMessagesPopulator extends CartModificationPopulator
{

	private final Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter;

	public TmaCartModificationValidationMessagesPopulator(
			final Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter)
	{
		this.cartValidationConverter = cartValidationConverter;
	}

	@Override
	public void populate(final CommerceCartModification source, final CartModificationData target) throws ConversionException
	{
		final Set<TmaCartValidationData> cartAllValidationData = new HashSet<>();

		cartAllValidationData.addAll(updateCartModificationWithValidationCart(source));
		cartAllValidationData.addAll(updateCartModificationWithValidationEntryGroup(source));

		if (CollectionUtils.isEmpty(cartAllValidationData))
		{
			return;
		}

		target.setValidationMessages(cartAllValidationData);

	}

	protected Converter<TmaCartValidationModel, TmaCartValidationData> getCartValidationConverter()
	{
		return cartValidationConverter;
	}

	protected Set<TmaCartValidationData> updateCartModificationWithValidationCart(final CommerceCartModification modification)
	{
		if (modification.getEntry() == null)
		{
			return Collections.<TmaCartValidationData> emptySet();
		}
		if (modification.getEntry().getOrder() == null)
		{
			return Collections.<TmaCartValidationData> emptySet();
		}

		final CartModel cartmodel = (CartModel) modification.getEntry().getOrder();
		final Set<TmaCartValidationModel> validationMessages = cartmodel.getCartValidations();

		if (CollectionUtils.isEmpty(validationMessages))
		{
			return Collections.<TmaCartValidationData> emptySet();
		}

		return validationMessages.stream().map(cartValidation -> getCartValidationConverter().convert(cartValidation))
				.collect(Collectors.toSet());
	}


	protected Set<TmaCartValidationData> updateCartModificationWithValidationEntryGroup(
			final CommerceCartModification modification)
	{
		final Set<TmaCartValidationData> validationMessagesData = new HashSet<>();

		final Set<EntryGroup> updatedGroups = getUpdatedGroups(modification);

		if (updatedGroups != null)
		{
			updatedGroups.forEach(updatedGroup -> {
				validationMessagesData.addAll(getValidationDataForEachGroup(updatedGroup));
			});
		}
		return validationMessagesData;
	}



	private Set<EntryGroup> getUpdatedGroups(final CommerceCartModification modification)
	{
		if (modification.getEntry() == null)
		{
			return Collections.<EntryGroup> emptySet();
		}
		if (CollectionUtils.isEmpty(modification.getEntry().getEntryGroupNumbers()))
		{
			return Collections.<EntryGroup> emptySet();
		}
		if (modification.getEntry().getOrder() == null)
		{
			return Collections.<EntryGroup> emptySet();
		}


		final CartModel cartmodel = (CartModel) modification.getEntry().getOrder();
		final Set<EntryGroup> updatedGroups = new HashSet<>();

		modification.getEntry().getEntryGroupNumbers().forEach(entryGroupNumber -> {
			updatedGroups.addAll(cartmodel.getEntryGroups().stream().filter(group -> group.getGroupNumber().equals(entryGroupNumber))
					.collect(Collectors.toList()));
		});

		return updatedGroups;
	}

	private Set<TmaCartValidationData> getValidationDataForEachGroup(final EntryGroup entryGroup)
	{
		final Set<TmaCartValidationData> validationMessages = new HashSet<>();

		if (entryGroup != null && entryGroup.getValidationMessages() != null)
		{
			entryGroup.getValidationMessages().forEach(cartValidationMessage -> {
				final TmaCartValidationData tmaCartValidationData = getCartValidationConverter().convert(cartValidationMessage);
				tmaCartValidationData.setGroupNumber(entryGroup.getGroupNumber());
				validationMessages.add(tmaCartValidationData);
			});
		}
		return validationMessages;
	}
}


