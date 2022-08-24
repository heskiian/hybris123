/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productrelationship;

import de.hybris.platform.subscribedproductservices.model.SpiProductRelationshipModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRelationship;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link SpiProductRelationshipModel} and
 * {@link ProductRelationship}
 *
 * @since 2105
 */
public class ProductRelationshipTypeAttributeMapper extends SpiAttributeMapper<SpiProductRelationshipModel, ProductRelationship>
{
	public ProductRelationshipTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductRelationshipModel source, final ProductRelationship target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(SubscribedproducttmfwebservicesConstants.SPI_PRODUCT_RELATIONSHIP_TYPE);
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductRelationship target, final SpiProductRelationshipModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getRelationshipType()))
		{
			source.setType(target.getRelationshipType());
		}
	}
}
