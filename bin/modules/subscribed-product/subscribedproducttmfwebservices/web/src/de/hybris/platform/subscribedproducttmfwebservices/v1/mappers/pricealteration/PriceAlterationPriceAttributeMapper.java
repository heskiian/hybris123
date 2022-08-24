/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.pricealteration;

import de.hybris.platform.subscribedproductservices.model.SpiPriceAlterationModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.PriceAlteration;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link SpiPriceAlterationModel} and
 * {@link PriceAlteration}
 *
 * @since 2105
 */
public class PriceAlterationPriceAttributeMapper extends SpiAttributeMapper<SpiPriceAlterationModel, PriceAlteration>
{
	private MapperFacade mapperFacade;

	public PriceAlterationPriceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiPriceAlterationModel source, final PriceAlteration target,
			final MappingContext context)
	{
		target.setPrice(getMapperFacade().map(source, Price.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final PriceAlteration target, final SpiPriceAlterationModel source,
			final MappingContext context)
	{
		mapperFacade.map(target.getPrice(), source, context);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
