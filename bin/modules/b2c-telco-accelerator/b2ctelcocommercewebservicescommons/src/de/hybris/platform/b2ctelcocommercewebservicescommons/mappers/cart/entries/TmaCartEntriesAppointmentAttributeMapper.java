/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TmaAppointmentWSDto;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps appointment attribute between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 1911
 */
public class TmaCartEntriesAppointmentAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderEntryWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final OrderEntryWsDTO target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getAppointmentId()))
		{
			final TmaAppointmentWSDto appointmentWSDto = new TmaAppointmentWSDto();
			appointmentWSDto.setId(source.getAppointmentId());
			target.setAppointment(appointmentWSDto);
		}
	}
}
