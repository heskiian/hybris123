/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for usageType attribute between {@link TmaTierUsageCompositeProdOfferPriceData} and
 * {@link UsagePriceCharge}
 *
 * @since 2007
 */
public class PopUsageChargeTypeAttributeMapper
		extends TmaAttributeMapper<TmaTierUsageCompositeProdOfferPriceData, UsagePriceCharge>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaTierUsageCompositeProdOfferPriceData source,
			final UsagePriceCharge target, final MappingContext context)
	{
		if (source.getUsageChargeType() != null)
		{
			target.setUsageType(source.getUsageChargeType().getCode());
		}
	}
}
