/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationSchemaLocationAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source, final ProductSpecification target,
			final MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
