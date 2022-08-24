/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Cycle;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaProdOfferPriceAlterationData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopDiscountCycleAttributeMapper extends TmaAttributeMapper<TmaProdOfferPriceAlterationData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProdOfferPriceAlterationData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		final Cycle cycle = new Cycle();
		if (source.getCycleStart() != null)
		{
			cycle.setCycleStart(source.getCycleStart());
		}
		if (source.getCycleEnd() != null)
		{
			cycle.setCycleEnd(source.getCycleEnd());
		}
		target.setCycle(cycle);
	}
}