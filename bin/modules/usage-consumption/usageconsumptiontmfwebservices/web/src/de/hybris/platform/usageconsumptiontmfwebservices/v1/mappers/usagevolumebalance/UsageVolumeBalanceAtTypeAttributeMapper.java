/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.usagevolumebalance;

import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeBalanceModel;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.UsageVolumeBalance;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link UcUsageVolumeBalanceModel} and
 * {@link UsageVolumeBalance}
 *
 * @since 2108
 */
public class UsageVolumeBalanceAtTypeAttributeMapper extends UcAttributeMapper<UcUsageVolumeBalanceModel, UsageVolumeBalance>
{
	public UsageVolumeBalanceAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcUsageVolumeBalanceModel source, final UsageVolumeBalance target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(UsageconsumptiontmfwebservicesConstants.UC_USAGE_VOLUME_BALANCE_TYPE);
		}
	}
}
