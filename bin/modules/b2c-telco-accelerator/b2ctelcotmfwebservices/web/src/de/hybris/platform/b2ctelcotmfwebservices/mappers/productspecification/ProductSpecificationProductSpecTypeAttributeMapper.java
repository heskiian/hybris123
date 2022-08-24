/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productSpecType attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationProductSpecTypeAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	private MapperFacade mapperFacade;

	public ProductSpecificationProductSpecTypeAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source,
			final ProductSpecification target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProductSpecType()))
		{
			return;
		}

		final List<ProductSpecType> productSpecTypes = new ArrayList<>();
		source.getProductSpecType().forEach(productSpecTypeData ->
		{
			final ProductSpecType productSpecType = getMapperFacade().map(productSpecTypeData, ProductSpecType.class, context);
			productSpecTypes.add(productSpecType);
		});

		target.setProductSpecType(productSpecTypes);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
