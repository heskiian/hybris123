/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.price;

import de.hybris.platform.subscribedproductservices.model.SpiComponentProdPriceModel;
import de.hybris.platform.subscribedproductservices.model.SpiPriceAlterationModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for percentage attribute between {@link SpiComponentProdPriceModel} and {@link Price}
 *
 * @since 2105
 */
public class PricePercentageAttributeMapper extends SpiAttributeMapper<SpiPriceAlterationModel, Price>
{

	public PricePercentageAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiPriceAlterationModel source, final Price target,
			final MappingContext context)
	{
		if (source.getPercentage() == null)
		{
			return;
		}

		target.setPercentage(Float.valueOf(source.getPercentage().toString()));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Price target, final SpiPriceAlterationModel source,
			final MappingContext context)
	{
		if (target.getPercentage() == null)
		{
			return;
		}

		source.setPercentage(Double.valueOf(target.getPercentage()));
	}
}
