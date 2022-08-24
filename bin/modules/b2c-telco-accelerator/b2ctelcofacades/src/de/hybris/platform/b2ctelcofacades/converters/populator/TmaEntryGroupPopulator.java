/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.converters.populator.EntryGroupPopulator;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator for {@link EntryGroup} specific TMA attributes.
 *
 * @since 6.7
 */
public class TmaEntryGroupPopulator extends EntryGroupPopulator
{
	private Converter<EntryGroup, EntryGroupData> entryGroupConverter;

	@Override
	public void populate(final EntryGroup entryGroup, final EntryGroupData entryGroupData)
	{
		super.populate(entryGroup, entryGroupData);
		entryGroupData.setProcessType(entryGroup.getProcessType());
	}

	@Override
	protected Converter<EntryGroup, EntryGroupData> getEntryGroupConverter()
	{
		return entryGroupConverter;
	}

	@Override
	@Required
	public void setEntryGroupConverter(final Converter<EntryGroup, EntryGroupData> entryGroupConverter)
	{
		this.entryGroupConverter = entryGroupConverter;
	}
}
