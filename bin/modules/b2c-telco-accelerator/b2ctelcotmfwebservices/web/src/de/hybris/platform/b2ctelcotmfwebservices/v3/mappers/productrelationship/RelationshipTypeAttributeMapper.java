/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productrelationship;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductRelationship;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relationshipType attribute between {@link TmaSubscribedProductData} and
 * {@link ProductRelationship}
 *
 * @since 2102
 */
public class RelationshipTypeAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, ProductRelationship>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final ProductRelationship target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getParentPOCode()))
		{
			target.setType(B2ctelcotmfwebservicesConstants.BUNDLESPRODUCTRELATIONSHIP);
		}
		else
		{
			target.setType(B2ctelcotmfwebservicesConstants.TARGETSPRODUCTRELATIONSHIP);
		}
	}
}
