/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricechargeentry;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.OverageUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;

import java.util.Date;


/**
 * This attribute Mapper class maps data for lastUpdate attribute between {@link UsageChargeEntryData} and
 * {@link UsagePriceChargeEntry}
 *
 * @since 1903
 */
public class UpceLastUpdateAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntry>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntry target,
			final MappingContext context)
	{
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
			target.setLastUpdate(lastUpdate);
		}
	}
}
