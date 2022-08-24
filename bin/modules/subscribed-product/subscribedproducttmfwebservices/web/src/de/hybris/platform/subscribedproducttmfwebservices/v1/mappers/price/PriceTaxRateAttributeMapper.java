/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.price;

import de.hybris.platform.subscribedproductservices.model.SpiComponentProdPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for taxRate attribute between {@link SpiComponentProdPriceModel} and {@link Price}
 *
 * @since 2105
 */
public class PriceTaxRateAttributeMapper extends SpiAttributeMapper<SpiComponentProdPriceModel, Price>
{

	public PriceTaxRateAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiComponentProdPriceModel source, final Price target,
			final MappingContext context)
	{
		if (source.getTaxRate() == null)
		{
			return;
		}

		target.setTaxRate(Float.valueOf(source.getTaxRate().toString()));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Price target, final SpiComponentProdPriceModel source,
			final MappingContext context)
	{
		if (target.getTaxRate() == null)
		{
			return;
		}

		source.setTaxRate(Double.valueOf(target.getTaxRate()));
	}
}
