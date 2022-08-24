/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product attribute between {@link OrderEntryData} and {@link OrderItem}.
 *
 * @since 1911
 */
public class OrderItemProductAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	private MapperFacade mapperFacade;

	public OrderItemProductAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderItem target,
			final MappingContext context)
	{
		final Product product = getMapperFacade().map(source, Product.class, context);
		target.setProduct(product);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
