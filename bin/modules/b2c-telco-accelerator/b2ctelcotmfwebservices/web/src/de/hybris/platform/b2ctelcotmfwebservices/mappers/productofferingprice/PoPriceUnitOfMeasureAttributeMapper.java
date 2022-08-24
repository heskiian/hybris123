/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for unit of measure attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceUnitOfMeasureAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (source.getUnitFactor() == null || source.getUnit() == null)
		{
			return;
		}

		final Money measurement = new Money();
		measurement.setUnit(source.getUnit().getUnitType());
		measurement.setValue(source.getUnitFactor().toString());

		target.setUnitOfMeasure(measurement);
	}
}
