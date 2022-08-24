/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for percentage attribute between {@link TmaProdOfferPriceAlterationData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopDiscountPercentageAttributeMapper
		extends TmaAttributeMapper<TmaProdOfferPriceAlterationData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProdOfferPriceAlterationData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (source.getIsPercentage())
		{
			target.setPercentage(source.getValue().floatValue());
			target.setPrice(null);
		}
	}
}
