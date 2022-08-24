/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.appointmentref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.AppointmentRef;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps id attribute for appointmentRef between {@link OrderEntryData} and {@link AppointmentRef}
 *
 * @since 1911
 */
public class AppointmentRefIdAttributeMapper extends TmaAttributeMapper<OrderEntryData, AppointmentRef>
{
	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, AppointmentRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getAppointmentId()))
		{
			target.setId(source.getAppointmentId());
		}
	}
}
