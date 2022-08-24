/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CycleWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PriceAlterationWsDTO;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycle attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlterationWsDTO}
 *
 * @since 2007
 */
public class TmaPriceAlterationCycleAttributeMapper
		extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlterationWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source,
			final PriceAlterationWsDTO target, final MappingContext context)
	{
		if (source.getCycleStart() == null && source.getCycleEnd() == null)
		{
			return;
		}

		final CycleWsDTO cycle = new CycleWsDTO();

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
