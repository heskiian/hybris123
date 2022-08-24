/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceValidForAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
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
