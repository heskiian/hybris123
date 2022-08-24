/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.review;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Review;
import de.hybris.platform.commercefacades.product.data.ReviewData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParty attribute between {@link ReviewData} and {@link Review}
 *
 * @since 1907
 */
public class ReviewRelatedPartyAttributeMapper extends TmaAttributeMapper<ReviewData, Review>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override public void populateTargetAttributeFromSource(ReviewData source, Review target, MappingContext context)
	{
		if (source.getPrincipal() != null)
		{
			target.setRelatedParty(getMapperFacade().map(source.getPrincipal(), RelatedPartyRef.class, context));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
