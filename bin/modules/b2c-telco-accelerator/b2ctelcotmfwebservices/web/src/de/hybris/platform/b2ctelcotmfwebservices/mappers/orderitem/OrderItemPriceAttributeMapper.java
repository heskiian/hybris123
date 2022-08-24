/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This attribute Mapper class maps data for itemPrice attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemPriceAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	private Map<String, Class<OrderPrice>> orderPriceTypeDtoMap;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		if (source.getPrice() == null)
		{
			return;
		}

		final List<OrderPrice> outputPrices = new ArrayList<>();
		final TmaAbstractOrderPriceData tmaAbstractOrderPriceData = source.getPrice();
		final OrderPrice orderPrice = getMapperFacade()
				.map(tmaAbstractOrderPriceData, getOrderPriceTypeDtoMap().get(tmaAbstractOrderPriceData.getClass().getSimpleName()),
						context);
		outputPrices.add(orderPrice);
		target.setItemPrice(outputPrices);
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

	@Required
	public void setOrderPriceTypeDtoMap(
			Map<String, Class<OrderPrice>> orderPriceTypeDtoMap)
	{
		this.orderPriceTypeDtoMap = orderPriceTypeDtoMap;
	}
}
