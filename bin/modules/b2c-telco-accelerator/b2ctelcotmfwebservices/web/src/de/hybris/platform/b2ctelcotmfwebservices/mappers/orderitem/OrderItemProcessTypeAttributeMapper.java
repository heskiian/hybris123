/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attype attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemProcessTypeAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public OrderItemProcessTypeAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		if (source.getProcessType() != null)
		{
			target.setProcessType(getMapperFacade().map(source.getProcessType(), ProcessType.class, context));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
