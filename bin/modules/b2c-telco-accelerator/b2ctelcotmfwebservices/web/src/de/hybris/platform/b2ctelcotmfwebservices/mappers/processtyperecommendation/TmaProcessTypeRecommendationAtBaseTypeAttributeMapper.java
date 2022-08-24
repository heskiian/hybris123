/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.processtyperecommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessTypeRecommendation;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType attribute between {@link TmaOfferData} and
 * {@link ProcessTypeRecommendation}
 *
 * @since 1907
 */
public class TmaProcessTypeRecommendationAtBaseTypeAttributeMapper
	 extends TmaAttributeMapper<TmaOfferData, ProcessTypeRecommendation>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, ProcessTypeRecommendation target,
		  MappingContext context)
	 {
		  if (target.getClass().getSuperclass() != null)
		  {
				target.setAtbaseType(target.getClass().getSuperclass().getSimpleName());
		  }
	 }
}
