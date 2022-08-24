/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates the attributes of {@link OrderEntryData} from {@link AbstractOrderEntryModel}
 *
 * @since 2102
 */
public class TmaHierarchicalOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{

	private Converter<AbstractOrderEntryModel, OrderEntryData> abstractOrderEntryConverter;

	public TmaHierarchicalOrderEntryPopulator(final Converter<AbstractOrderEntryModel, OrderEntryData> abstractOrderEntryConverter)
	{
		this.abstractOrderEntryConverter = abstractOrderEntryConverter;
	}

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (CollectionUtils.isNotEmpty(source.getChildEntries()))
		{
			target.setEntries(this.getAbstractOrderEntryConverter().convertAll(source.getChildEntries()));
		}
	}

	protected Converter<AbstractOrderEntryModel, OrderEntryData> getAbstractOrderEntryConverter()
	{
		return abstractOrderEntryConverter;
	}
}
