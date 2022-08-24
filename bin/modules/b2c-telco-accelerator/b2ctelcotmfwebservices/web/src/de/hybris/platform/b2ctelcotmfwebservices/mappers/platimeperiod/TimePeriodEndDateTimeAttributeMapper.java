/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.platimeperiod;

import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for end date time attribute between {@link TmaPricingLogicAlgorithmData} and {@link TimePeriod}
 *
 * @since 2102
 */
public class TimePeriodEndDateTimeAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, TimePeriod>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final TimePeriod target,
			final MappingContext context)
	{
		if (source.getOfflineDate() != null)
		{
			target.setEndDateTime(source.getOfflineDate());
		}
	}
}
