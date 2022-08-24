/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.product;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOffering attribute between {@link TmaSubscribedProductData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductPoAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductPoAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final Product target,
			final MappingContext context)
	{
		if (StringUtils.isNoneBlank(source.getProductCode()))
		{
			final ProductOfferingRef productOffering = getMapperFacade().map(source.getProductOfferingRef(),
					ProductOfferingRef.class, context);
			target.setProductOffering(productOffering);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
