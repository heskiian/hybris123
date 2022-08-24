/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartprice;


import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartPrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cartPrice attribute between {@link TmaAbstractOrderCompositePriceData} and
 * {@link CartPrice}
 *
 * @since 1907
 */
public class CartPriceChildrenAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderCompositePriceData, CartPrice>
{
	private MapperFacade mapperFacade;

	private Map<String, Class<CartPrice>> priceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderCompositePriceData source, final CartPrice target,
			final MappingContext context)
	{
		if (source.getChildPrices() == null)
		{
			return;
		}

		final List<CartPrice> outputPrices = new ArrayList<>();

		source.getChildPrices().forEach((TmaAbstractOrderPriceData tmaAbstractOrderPriceData) -> {
			if (tmaAbstractOrderPriceData instanceof TmaAbstractOrderAlterationPriceData)
			{
				return;
			}

			final CartPrice cartPrice = getMapperFacade()
					.map(tmaAbstractOrderPriceData, priceTypeDtoMap.get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
							context);
			outputPrices.add(cartPrice);
		});

		if (CollectionUtils.isNotEmpty(outputPrices))
		{
			target.setCartPrice(outputPrices);
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	public Map<String, Class<CartPrice>> getPriceTypeDtoMap()
	{
		return priceTypeDtoMap;
	}

	@Required
	public void setPriceTypeDtoMap(Map<String, Class<CartPrice>> priceTypeDtoMap)
	{
		this.priceTypeDtoMap = priceTypeDtoMap;
	}
}
