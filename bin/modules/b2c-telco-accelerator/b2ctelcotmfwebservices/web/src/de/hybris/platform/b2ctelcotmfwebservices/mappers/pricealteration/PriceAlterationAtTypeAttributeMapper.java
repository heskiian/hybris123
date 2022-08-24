/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricealteration;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderAlterationPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PriceAlteration;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link TmaAbstractOrderAlterationPriceData} and {@link PriceAlteration}
 *
 * @since 2007
 */
public class PriceAlterationAtTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderAlterationPriceData, PriceAlteration>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderAlterationPriceData source, final PriceAlteration target,
			final MappingContext context)
	{
		target.setAttype(target.getClass().getSimpleName());
	}
}

