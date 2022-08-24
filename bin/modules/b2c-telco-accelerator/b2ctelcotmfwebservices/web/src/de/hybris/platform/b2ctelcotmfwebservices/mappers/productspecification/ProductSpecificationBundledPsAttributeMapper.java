/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BundledProductSpecification;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for bundledProductSpecification attribute between {@link TmaCompositeProductSpecificationData}
 * and {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationBundledPsAttributeMapper
		extends TmaAttributeMapper<TmaCompositeProductSpecificationData, ProductSpecification>
{
	private MapperFacade mapperFacade;

	public ProductSpecificationBundledPsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaCompositeProductSpecificationData source,
			final ProductSpecification target, final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getChildren()))
		{
			final List<BundledProductSpecification> bundledProductSpecifications = new ArrayList<>();
			source.getChildren().forEach(productSpecificationData ->
			{
				final BundledProductSpecification bundledProductSpecification = getMapperFacade().map(productSpecificationData,
						BundledProductSpecification.class, context);
				bundledProductSpecifications.add(bundledProductSpecification);
			});

			target.setBundledProductSpecification(bundledProductSpecifications);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
