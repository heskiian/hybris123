/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productterm;

import de.hybris.platform.subscribedproductservices.model.SpiProductTermModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductTerm;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for amount attribute between {@link ProductTerm} and
 * {@link SpiProductTermModel}
 *
 * @since 2111
 */
public class SpiProductTermAmountAttributeMapper extends SpiAttributeMapper<SpiProductTermModel, ProductTerm>
{
	public SpiProductTermAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductTermModel source, final ProductTerm target,
			final MappingContext context)
	{
     // Do nothing
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductTerm target, final SpiProductTermModel source,
			final MappingContext context)
	{
		if (target.getDuration() != null && target.getDuration().getAmount() != null)
		{
			source.setAmount(target.getDuration().getAmount().doubleValue());
		}
	}
}
