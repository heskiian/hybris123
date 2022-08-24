/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.AppointmentRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for appointmentRef attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1911
 */
public class OrderItemAppointmentRefAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	private MapperFacade mapperFacade;

	public OrderItemAppointmentRefAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		if (StringUtils.isEmpty(source.getAppointmentId()))
		{
			return;
		}
		final AppointmentRef appointmentRef = getMapperFacade().map(source, AppointmentRef.class, context);
		target.setAppointment(appointmentRef);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
