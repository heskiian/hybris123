/*
 *Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.subscriptionfacades.data.PerUnitUsageChargeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usage type attribute between {@link UsageChargeData} and
 * {@link UsagePriceChargeWsDTO}. Usage type is set only in case the source is instance of {@link PerUnitUsageChargeData}
 *
 * @since 1907
 */
public class TmaUpcUsageTypeAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!(source instanceof PerUnitUsageChargeData))
		{
			return;
		}

		if (((PerUnitUsageChargeData) source).getUsageChargeType() != null)
		{
			target.setUsageType(((PerUnitUsageChargeData) source).getUsageChargeType().getCode());
		}
	}
}
