/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaProdOfferPriceAlterationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Cycle;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProdOfferPriceAlteration;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaProdOfferPriceAlterationData} and
 * {@link ProdOfferPriceAlteration}
 *
 * @since 2007
 */
public class PopDiscountCycleAttributeMapper extends TmaAttributeMapper<TmaProdOfferPriceAlterationData, ProdOfferPriceAlteration>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProdOfferPriceAlterationData source,
			final ProdOfferPriceAlteration target, final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getCycleStart()))
		{
			return;
		}

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
