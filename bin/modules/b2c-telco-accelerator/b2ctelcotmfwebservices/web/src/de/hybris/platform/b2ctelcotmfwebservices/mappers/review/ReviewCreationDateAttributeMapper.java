/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.review;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Review;
import de.hybris.platform.commercefacades.product.data.ReviewData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for creationDate attribute between {@link ReviewData} and {@link Review}
 *
 * @since 1907
 */
public class ReviewCreationDateAttributeMapper extends TmaAttributeMapper<ReviewData, Review>
{
	@Override
	public void populateTargetAttributeFromSource(ReviewData source, Review target, MappingContext context)
	{
		if (source.getDate() != null)
		{
			target.setCreationDate(source.getDate());
		}
	}
}
