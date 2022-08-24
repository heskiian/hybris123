/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link TmaOfferData} and {@link Recommendation}
 *
 * @since 1907
 */
public class TmaRecommendationValidForAttributeMapper extends TmaAttributeMapper<TmaOfferData, Recommendation>
{
	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, Recommendation target,
		  MappingContext context)
	 {
		  final TimePeriod timePeriod = new TimePeriod();
		  if (source.getProduct().getOnlineDate() != null)
		  {
				timePeriod.setStartDateTime(source.getProduct().getOnlineDate());
		  }

		  if (source.getProduct().getOfflineDate() != null)
		  {
				timePeriod.setEndDateTime(source.getProduct().getOfflineDate());
		  }

		  target.setValidFor(timePeriod);
	 }
}
