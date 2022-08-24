/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.platimeperiod;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for end date time attribute between {@link TmaPricingLogicAlgorithmData} and {@link TimePeriodWsDTO}
 *
 * @since 2102
 */
public class TmaTimePeriodEndDateTimeAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, TimePeriodWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final TimePeriodWsDTO target,
			final MappingContext context)
	{
		if (source.getOfflineDate() != null)
		{
			target.setEndDateTime(source.getOfflineDate());
		}
	}
}
