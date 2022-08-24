/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.consumptionsummary;

import de.hybris.platform.usageconsumptionservices.model.UcConsumptionSummaryModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.ConsumptionSummary;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for consumptionPeriod attribute between {@link UcConsumptionSummaryModel} and
 * {@link ConsumptionSummary}
 *
 * @since 2108
 */
public class ConsumptionSummaryConsumptionPeriodAttributeMapper
		extends UcAttributeMapper<UcConsumptionSummaryModel, ConsumptionSummary>
{
	public ConsumptionSummaryConsumptionPeriodAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcConsumptionSummaryModel source, final ConsumptionSummary target,
			final MappingContext context)
	{
		if (source.getConsumptionStartDate() == null || source.getConsumptionEndDate() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getConsumptionStartDate());
		timePeriod.setEndDateTime(source.getConsumptionEndDate());
		target.setConsumptionPeriod(timePeriod);
	}
}
