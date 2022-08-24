/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ServiceSpecificationRef;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for serviceSpecification attribute between {@link TmaProductSpecificationData} and {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationServiceSpecificationAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	private MapperFacade mapperFacade;

	public ProductSpecificationServiceSpecificationAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source,
			final ProductSpecification target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getServiceSpecification()))
		{
			return;
		}

		final List<ServiceSpecificationRef> serviceSpecificationRefs = new ArrayList<>();
		source.getServiceSpecification().forEach(serviceSpecificationData ->
		{
			final ServiceSpecificationRef serviceSpecificationRef = getMapperFacade().map(serviceSpecificationData,
					ServiceSpecificationRef.class, context);
			serviceSpecificationRefs.add(serviceSpecificationRef);
		});

		target.setServiceSpecification(serviceSpecificationRefs);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
