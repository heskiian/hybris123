/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricechargeentry;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceChargeEntry;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.TierUsageChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeEntryData;
import ma.glasnost.orika.MappingContext;
import org.springframework.util.ObjectUtils;


/**
 * This attribute Mapper class maps data for tierEnd attribute between {@link UsageChargeEntryData} and
 * {@link UsagePriceChargeEntry}
 *
 * @since 1903
 */
public class UpceTierEndAttributeMapper extends TmaAttributeMapper<UsageChargeEntryData, UsagePriceChargeEntry>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeEntryData source, final UsagePriceChargeEntry target,
			final MappingContext context)
	{
		if (!(source instanceof TierUsageChargeEntryData))
		{
			return;
		}

		if (ObjectUtils.isEmpty(((TierUsageChargeEntryData) source).getTierEnd())) {
			return;
		}

		target.setTierEnd(((TierUsageChargeEntryData) source).getTierEnd());
	}
}
