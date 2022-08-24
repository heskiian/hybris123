/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.order.data.OrderData;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute mapper class maps data for orderCost attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 2003
 */
public class ProductOrderOrderCostAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public ProductOrderOrderCostAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
			final MappingContext context)
	{
		List<OrderPrice> orderPrices = new ArrayList<>();
		if (source.getPrice() == null)
		{
			return;
		}
		orderPrices.add(getMapperFacade().map(source.getPrice(), OrderPrice.class));
		target.setOrderCost(orderPrices);
	}

	private MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
