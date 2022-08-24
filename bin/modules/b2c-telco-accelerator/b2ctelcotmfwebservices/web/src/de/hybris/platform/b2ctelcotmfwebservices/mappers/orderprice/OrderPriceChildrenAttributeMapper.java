/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderprice;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for orderPrice children attribute between {@link TmaAbstractOrderCompositePriceData} and
 * {@link OrderPrice}
 *
 * @since 1907
 */
public class OrderPriceChildrenAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderCompositePriceData, OrderPrice>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	private Map<String, Class<OrderPrice>> orderPriceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(TmaAbstractOrderCompositePriceData source, OrderPrice target,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getChildPrices()))
		{
			return;
		}

		final List<OrderPrice> outputPrices = new ArrayList<>();
		source.getChildPrices().forEach((TmaAbstractOrderPriceData tmaAbstractOrderPriceData) -> {
			if (tmaAbstractOrderPriceData instanceof TmaAbstractOrderAlterationPriceData)
			{
				return;
			}

			final OrderPrice cartPrice = getMapperFacade()
					.map(tmaAbstractOrderPriceData,
							getOrderPriceTypeDtoMap().get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
							context);

			outputPrices.add(cartPrice);
		});

		if (CollectionUtils.isNotEmpty(outputPrices))
		{
			target.setOrderPrice(outputPrices);
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

	public Map<String, Class<OrderPrice>> getOrderPriceTypeDtoMap()
	{
		return orderPriceTypeDtoMap;
	}

	public void setOrderPriceTypeDtoMap(
			Map<String, Class<OrderPrice>> orderPriceTypeDtoMap)
	{
		this.orderPriceTypeDtoMap = orderPriceTypeDtoMap;
	}
}
