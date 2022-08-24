/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usage type attribute between {@link UsageChargeData} and {@link UsagePriceCharge}.
 * Usage type is set only in case the source is instance of {@link PerUnitUsageChargeData}
 *
 * @since 1903
 */
public class UpcUsageTypeAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceCharge>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceCharge target,
			final MappingContext context)
	{
		if (!(source instanceof PerUnitUsageChargeData)) {
			return;
		}

		if (((PerUnitUsageChargeData) source).getUsageChargeType() != null)
		{
			target.usageType(((PerUnitUsageChargeData) source).getUsageChargeType().getCode());
		}
	}
}
