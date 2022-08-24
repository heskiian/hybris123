/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaUsageProdOfferPriceChargeData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageUnit;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usageUnit attribute between {@link TmaUsageProdOfferPriceChargeData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopUsageUnitAttributeMapper extends TmaAttributeMapper<TmaUsageProdOfferPriceChargeData, ProductOfferingPrice>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaUsageProdOfferPriceChargeData source,
			final ProductOfferingPrice target, final MappingContext context)
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
