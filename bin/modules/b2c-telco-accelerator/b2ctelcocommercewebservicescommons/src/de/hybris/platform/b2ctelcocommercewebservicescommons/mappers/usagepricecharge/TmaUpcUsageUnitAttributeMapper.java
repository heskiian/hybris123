/*
 *Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagepricecharge;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsagePriceChargeWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageUnitWsDTO;
import de.hybris.platform.subscriptionfacades.data.UsageChargeData;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link UsageChargeData} and {@link UsagePriceChargeWsDTO}
 *
 * @since 1907
 */
public class TmaUpcUsageUnitAttributeMapper extends TmaAttributeMapper<UsageChargeData, UsagePriceChargeWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final UsageChargeData source, final UsagePriceChargeWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getUsageUnit() == null)
		{
			return;
		}

		final UsageUnitWsDTO usageUnit = new UsageUnitWsDTO();
		usageUnit.setId(source.getUsageUnit().getId());
		usageUnit.setName(source.getUsageUnit().getName());
		target.setUsageUnit(usageUnit);
	}
}
