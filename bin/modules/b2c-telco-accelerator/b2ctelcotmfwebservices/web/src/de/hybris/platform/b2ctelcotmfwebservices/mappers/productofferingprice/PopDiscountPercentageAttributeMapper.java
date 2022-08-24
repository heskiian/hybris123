/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdOfferPriceAlteration;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for percentage attribute between {@link TmaProdOfferPriceAlterationData} and
 * {@link ProdOfferPriceAlteration}
 *
 * @since 2007
 */
public class PopDiscountPercentageAttributeMapper extends TmaAttributeMapper<TmaProdOfferPriceAlterationData,
		ProdOfferPriceAlteration>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProdOfferPriceAlterationData source,
			final ProdOfferPriceAlteration target, final MappingContext context)
	{
		if (source.getIsPercentage())
		{
			target.setPercentage(source.getValue().floatValue());
			target.setPrice(null);
		}
	}
}