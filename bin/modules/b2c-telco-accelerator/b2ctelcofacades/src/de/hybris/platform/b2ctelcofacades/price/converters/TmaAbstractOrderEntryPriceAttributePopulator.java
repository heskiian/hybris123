/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.core.model.order.AbstractOrderEntryModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.OrderEntryData} as target type, handles pricing information.
 *
 * @since 1907
 */
public class TmaAbstractOrderEntryPriceAttributePopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{
	private Map<String,
			AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> tmaAbstractOrderCompositeComponentMap;

	@Override
	public void populate(final AbstractOrderEntryModel source,final OrderEntryData target)
	{
		final TmaAbstractOrderPriceModel price = source.getPrice();

		if (price == null)
		{
			return;
		}

		final String priceClass = price.getItemtype();

		if (getTmaAbstractOrderCompositeComponentMap().containsKey(priceClass))
		{
			AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData> abstractPopulatingConverter = getTmaAbstractOrderCompositeComponentMap()
					.get(priceClass);
			target.setPrice(abstractPopulatingConverter.convert(price));
		}
	}

	protected Map<String, AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> getTmaAbstractOrderCompositeComponentMap()
	{
		return tmaAbstractOrderCompositeComponentMap;
	}

	@Required
	public void setTmaAbstractOrderCompositeComponentMap(
			Map<String, AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> mapper)
	{
		this.tmaAbstractOrderCompositeComponentMap = mapper;
	}
}
