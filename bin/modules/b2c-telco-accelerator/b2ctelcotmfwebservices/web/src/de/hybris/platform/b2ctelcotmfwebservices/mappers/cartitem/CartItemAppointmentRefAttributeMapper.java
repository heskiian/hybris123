/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.cartitem;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.AppointmentRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps appointmentRef attribute for cartItem between {@link OrderEntryData} and {@link CartItem}
 *
 * @since 1911
 */
public class CartItemAppointmentRefAttributeMapper extends TmaAttributeMapper<OrderEntryData, CartItem>
{
	private MapperFacade mapperFacade;

	public CartItemAppointmentRefAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, CartItem target,
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
