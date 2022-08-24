/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productrelationship;

import de.hybris.platform.subscribedproductservices.model.SpiProductRelationshipModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRelationship;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relationshipType attribute between {@link SpiProductRelationshipModel} and
 * {@link ProductRelationship}
 *
 * @since 2105
 */
public class RelationshipTypeAttributeMapper extends SpiAttributeMapper<SpiProductRelationshipModel, ProductRelationship>
{
	private MapperFacade mapperFacade;

	public RelationshipTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductRelationshipModel source, final ProductRelationship target,
			final MappingContext context)
	{
		if (source.getType() == null)
		{
			return;
		}

		target.setRelationshipType(source.getType());
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
