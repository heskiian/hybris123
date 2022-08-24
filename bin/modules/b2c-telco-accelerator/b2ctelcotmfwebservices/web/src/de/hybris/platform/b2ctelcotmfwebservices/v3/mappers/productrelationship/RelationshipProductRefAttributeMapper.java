/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productrelationship;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductRelationship;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps product attribute between {@link TmaSubscribedProductData} and
 * {@link ProductRelationship}
 *
 * @since 2102
 */
public class RelationshipProductRefAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, ProductRelationship>
{
	private final MapperFacade mapperFacade;

	public RelationshipProductRefAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final ProductRelationship target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			final ProductRef productRef = getMapperFacade().map(source, ProductRef.class, context);
			target.setProduct(productRef);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
