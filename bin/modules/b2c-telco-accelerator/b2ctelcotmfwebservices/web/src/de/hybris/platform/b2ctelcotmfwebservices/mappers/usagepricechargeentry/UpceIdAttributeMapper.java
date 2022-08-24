/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricechargeentry;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import ma.glasnost.orika.MappingContext;

import java.util.Date;


/**
 * This attribute Mapper class maps data for ID attribute between {@link UsageChargeEntryData} and {@link UsagePriceChargeEntry}
 *
 * @since 1903
 */
public class UpceIdAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntry>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntry target,
			final MappingContext context)
	{
		String sourceId = null;
		if (source instanceof TierUsageChargeEntryData)
		{
			sourceId = ((TierUsageChargeEntryData) source).getId();
		}

		if (source instanceof OverageUsageChargeEntryData)
		{
			sourceId = ((OverageUsageChargeEntryData) source).getId();
		}

		if (sourceId != null)
		{
			target.setId(sourceId);
		}
	}
}
