/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaTierUsageCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import ma.glasnost.orika.MappingContext;



/**
 * This attribute Mapper class maps data for usageChargeType attribute between {@link TmaTierUsageCompositeProdOfferPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopUsageChargeTypeAttributeMapper
		extends TmaAttributeMapper<TmaTierUsageCompositeProdOfferPriceData, ProductOfferingPrice>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaTierUsageCompositeProdOfferPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (source.getUsageChargeType() != null)
		{
			target.setUsageChargeType(source.getUsageChargeType().getCode());
		}
	}
}
