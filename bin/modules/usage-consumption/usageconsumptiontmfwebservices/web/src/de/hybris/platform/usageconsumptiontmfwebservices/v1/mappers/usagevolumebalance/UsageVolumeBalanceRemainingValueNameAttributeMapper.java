/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.usagevolumebalance;

import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeBalanceModel;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeBalance;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for remainingValueName attribute between {@link UcUsageVolumeBalanceModel} and
 * {@link UsageVolumeBalance}
 *
 * @since 2108
 */
public class UsageVolumeBalanceRemainingValueNameAttributeMapper
		extends UcAttributeMapper<UcUsageVolumeBalanceModel, UsageVolumeBalance>
{
	public UsageVolumeBalanceRemainingValueNameAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcUsageVolumeBalanceModel source, final UsageVolumeBalance target,
			final MappingContext context)
	{
		if (source.getRemainingValue() == null || source.getUnits() == null)
		{
			return;
		}

		target.setRemainingValueName(source.getRemainingValue() + " " + source.getUnits());
	}
}