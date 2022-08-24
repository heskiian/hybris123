/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populator for {@link EntryGroup} validation messages attribute
 *
 * @since 1911
 */
public class TmaEntryGroupValidationMessagesPopulator implements Populator<EntryGroup, EntryGroupData>
{
	private Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter;

	public TmaEntryGroupValidationMessagesPopulator(
			Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter)
	{
		this.cartValidationConverter = cartValidationConverter;
	}

	@Override
	public void populate(EntryGroup entryGroup, EntryGroupData entryGroupData) throws ConversionException
	{
		final Set<TmaCartValidationModel> entryGroupValidationMessages = entryGroup.getValidationMessages();
		if (CollectionUtils.isNotEmpty(entryGroupValidationMessages))
		{
			Set<TmaCartValidationData> validationMessages = entryGroupValidationMessages.stream()
					.map(validationMessage -> getCartValidationConverter().convert(validationMessage))
					.collect(Collectors.toSet());
			entryGroupData.setValidationMessages(validationMessages);
		}
	}

	protected Converter<TmaCartValidationModel, TmaCartValidationData> getCartValidationConverter()
	{
		return cartValidationConverter;
	}
}
