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
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link SpiProductRelationshipModel} and
 * {@link ProductRelationship}
 *
 * @since 2105
 */
public class ProductRelationshipSchemaLocationAttributeMapper
		extends SpiAttributeMapper<SpiProductRelationshipModel, ProductRelationship>
{
	public ProductRelationshipSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductRelationshipModel source, final ProductRelationship target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(SubscribedproducttmfwebservicesConstants.SPI_API_SCHEMA);
		}
	}
}
