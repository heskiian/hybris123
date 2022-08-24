/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaOfferData} and {@link Recommendation}
 *
 * @since 1907
 */
public class TmaRecommendationHrefAttributeMapper extends TmaAttributeMapper<TmaOfferData, Recommendation>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, Recommendation target, MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getCode()))
		  {
				target.setHref(B2ctelcotmfwebservicesConstants.RECOMMENDATION_API_URL + source.getCode());
		  }

	 }
}
