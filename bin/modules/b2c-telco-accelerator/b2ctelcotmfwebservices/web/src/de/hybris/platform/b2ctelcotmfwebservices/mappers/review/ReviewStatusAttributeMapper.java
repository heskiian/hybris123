/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.review;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Review;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ReviewStatus;
import de.hybris.platform.commercefacades.product.data.ReviewData;

import ma.glasnost.orika.MappingContext;


public class ReviewStatusAttributeMapper extends TmaAttributeMapper<ReviewData, Review>
{
	@Override
	public void populateTargetAttributeFromSource(ReviewData source, Review target, MappingContext context)
	{
		if (source.getLifecycleStatus() != null)
		{
			ReviewStatus reviewStatus = ReviewStatus.valueOf(source.getLifecycleStatus().toUpperCase());
			target.setStatus(reviewStatus);
		}
	}
}
