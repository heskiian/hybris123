/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link TmaOfferData} and {@link Recommendation}
 *
 * @since 1907
 */
public class TmaRecommendationSchemaLocationAttributeMapper extends TmaAttributeMapper<TmaOfferData, Recommendation>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, Recommendation target,
		  MappingContext context)
	 {
		  if (source.getCode() != null)
		  {
				target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
		  }
	 }
}
