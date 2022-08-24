/*
 *Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricechargeentry;


import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;

import java.util.Date;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for lastUpdate attribute between {@link UsageChargeEntryData} and
 * {@link UsagePriceChargeEntryWsDTO}
 *
 * @since 1907
 */
public class TmaUpceLastUpdateAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntryWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntryWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		Date lastUpdate = null;
		if (source instanceof TierUsageChargeEntryData)
		{
			lastUpdate = ((TierUsageChargeEntryData) source).getModifiedTime();
		}

		if (source instanceof OverageUsageChargeEntryData)
		{
			lastUpdate = ((OverageUsageChargeEntryData) source).getModifiedTime();
		}

		if (lastUpdate != null)
		{
			target.setModifiedTime(lastUpdate);
		}
	}
}
