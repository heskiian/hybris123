/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for orderItem children attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemChildrenAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}

		List<OrderItem> orderItems = new ArrayList<>();
		source.getEntries().forEach((OrderEntryData orderEntryData) -> {
			OrderItem orderItem = getMapperFacade().map(orderEntryData, OrderItem.class, context);
			orderItems.add(orderItem);
		});

		target.setOrderItem(orderItems);
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
}
