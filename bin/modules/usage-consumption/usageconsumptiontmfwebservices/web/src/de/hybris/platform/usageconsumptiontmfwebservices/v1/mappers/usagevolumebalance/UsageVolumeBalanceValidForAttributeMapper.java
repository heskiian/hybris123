/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.usagevolumebalance;

import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeBalanceModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeBalance;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link UcUsageVolumeBalanceModel} and
 * {@link UsageVolumeBalance}
 *
 * @since 2108
 */
public class UsageVolumeBalanceValidForAttributeMapper extends UcAttributeMapper<UcUsageVolumeBalanceModel, UsageVolumeBalance>
{
	public UsageVolumeBalanceValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcUsageVolumeBalanceModel source, final UsageVolumeBalance target,
			final MappingContext context)
	{
		if (source.getStartDate() == null || source.getEndDate() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDate());
		timePeriod.setEndDateTime(source.getEndDate());
		target.setValidFor(timePeriod);
	}
}