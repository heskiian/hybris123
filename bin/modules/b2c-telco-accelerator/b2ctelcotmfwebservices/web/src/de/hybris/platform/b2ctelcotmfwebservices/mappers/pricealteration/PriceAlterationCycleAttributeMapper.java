/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Cycle;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PriceAlteration;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlteration}
 *
 * @since 2007
 */
public class PriceAlterationCycleAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlteration>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source, final PriceAlteration target,
			final MappingContext context)
	{
		if (source.getCycleStart() == null && source.getCycleEnd() == null)
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
