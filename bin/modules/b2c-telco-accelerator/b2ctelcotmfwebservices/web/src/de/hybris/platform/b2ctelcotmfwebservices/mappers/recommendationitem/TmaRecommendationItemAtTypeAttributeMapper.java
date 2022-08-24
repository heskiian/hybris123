/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendationitem;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecommendationItem;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaOfferData} and {@link RecommendationItem}
 *
 * @since 1907
 */
public class TmaRecommendationItemAtTypeAttributeMapper extends TmaAttributeMapper<TmaOfferData, RecommendationItem>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, RecommendationItem target, MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getCode()))
		  {
				target.setAttype(target.getClass().getSimpleName());
		  }
	 }
}
