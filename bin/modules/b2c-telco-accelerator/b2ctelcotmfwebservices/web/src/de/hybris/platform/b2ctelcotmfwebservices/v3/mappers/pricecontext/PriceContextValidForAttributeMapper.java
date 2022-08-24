/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */


package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for validFor attribute between {@link PriceData} and {@link PriceContext}
 *
 * @since 2007
 */
public class PriceContextValidForAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target, final MappingContext context)
	{
		final TimePeriod timePeriod = new TimePeriod();

		if (source.getStartTime() != null)
		{
			timePeriod.setStartDateTime(source.getStartTime());
		}

		if (source.getEndTime() != null)
		{
			timePeriod.setEndDateTime(source.getEndTime());
		}

		target.setValidFor(timePeriod);
	}
}
