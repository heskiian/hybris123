/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productprice;

import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link SpiProductPriceModel} and {@link ProductPrice}
 *
 * @since 2105
 */
public class ProductPricePriceAttributeMapper extends SpiAttributeMapper<SpiProductPriceModel, ProductPrice>
{
	private MapperFacade mapperFacade;

	public ProductPricePriceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductPriceModel source, final ProductPrice target,
			final MappingContext context)
	{
		target.setPrice(getMapperFacade().map(source, Price.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductPrice target, final SpiProductPriceModel source,
			final MappingContext context)
	{
		mapperFacade.map(target.getPrice(), source, context);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
