/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.platimeperiod;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for start date time attribute between {@link TmaPricingLogicAlgorithmData} and {@link TimePeriodWsDTO}
 *
 * @since 2102
 */
public class TmaTimePeriodStartDateTimeAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, TimePeriodWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final TimePeriodWsDTO target,
			final MappingContext context)
	{
		if (source.getOnlineDate() != null)
		{
			target.setStartDateTime(source.getOnlineDate());
		}
	}
}
