/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecificationRef;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product specification attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoPsAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;

	public PoPsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (source.getProductSpecification() == null)
		{
			return;
		}

		final ProductSpecificationRef productSpecificationRef = getMapperFacade().map(source.getProductSpecification(),
				ProductSpecificationRef.class, context);

		target.setProductSpecification(productSpecificationRef);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
