/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.enums.SpiProductStatusType;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductStatusType;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for status attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductStatusAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;

	public ProductStatusAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (source.getStatus() == null)
		{
			return;
		}

		target.setStatus(getMapperFacade().map(source.getStatus(), ProductStatusType.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source,
			final MappingContext context)
	{
		if (target.getStatus() == null)
		{
			return;
		}
		source.setStatus(SpiProductStatusType.valueOf(target.getStatus().name()));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
