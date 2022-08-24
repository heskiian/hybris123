/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaProductSpecificationData} and {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationAtTypeAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source, final ProductSpecification target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
