/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageUnit;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageUnit attribute between {@link TmaUsageProdOfferPriceChargeData} and
 * {@link UsagePriceCharge}
 *
 * @since 2007
 */
public class PopUsageUnitAttributeMapper extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, UsagePriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaUsageProdOfferPriceChargeData source,
			final UsagePriceCharge target, final MappingContext context)
	{
		if (source.getUsageUnit() != null)
		{
			final UsageUnit usageUnit = new UsageUnit();
			usageUnit.setId(source.getUsageUnit().getId());
			usageUnit.setName(source.getUsageUnit().getName());
			target.setUsageUnit(usageUnit);
		}
	}
}
