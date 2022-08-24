/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecificationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for product specification attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoPsAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

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

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
