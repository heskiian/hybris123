/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel} as source and
 * {@link de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData} as target type.
 *
 * @since 1907
 */
public class TmaAbstractOrderCompositePriceDataPopulator<SOURCE extends TmaAbstractOrderCompositePriceModel, TARGET extends TmaAbstractOrderCompositePriceData>
		extends TmaAbstractOrderPriceDataPopulator<SOURCE, TARGET>
{
	private Map<String, AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> tmaAbstractOrderCompositeComponentMap;

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);

		final Set<TmaAbstractOrderPriceModel> childPrices = source.getChildPrices();
		final Set<TmaAbstractOrderPriceData> targetChildPrices = new HashSet<>();

		for (TmaAbstractOrderPriceModel childPrice : childPrices)
		{
			final String priceClass = childPrice.getItemtype();
			if (getTmaAbstractOrderCompositeComponentMap().containsKey(priceClass))
			{
				AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData> abstractPopulatingConverter = getTmaAbstractOrderCompositeComponentMap()
						.get(priceClass);
				targetChildPrices.add(abstractPopulatingConverter.convert(childPrice));
			}
		}
		target.setChildPrices(targetChildPrices);
	}

	protected Map<String, AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> getTmaAbstractOrderCompositeComponentMap()
	{
		return tmaAbstractOrderCompositeComponentMap;
	}

	@Required
	public void setTmaAbstractOrderCompositeComponentMap(
			Map<String, AbstractPopulatingConverter<TmaAbstractOrderPriceModel, TmaAbstractOrderPriceData>> tmaAbstractOrderCompositeComponentMap)
	{
		this.tmaAbstractOrderCompositeComponentMap = tmaAbstractOrderCompositeComponentMap;
	}
}
