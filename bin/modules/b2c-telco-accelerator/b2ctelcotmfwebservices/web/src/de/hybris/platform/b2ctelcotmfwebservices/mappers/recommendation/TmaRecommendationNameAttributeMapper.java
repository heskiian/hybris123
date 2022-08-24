/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link TmaOfferData} and {@link Recommendation}
 *
 * @since 1911
 */
public class TmaRecommendationNameAttributeMapper extends TmaAttributeMapper<TmaOfferData, Recommendation>
{
	private static final String RECOMMENDATION_NAME = "Recommendation for ";
	private static final String SEPARATOR = ", ";

	@Override
	public void populateTargetAttributeFromSource(TmaOfferData source, Recommendation target, MappingContext context)
	{
		if (source.getPrincipalData() != null && source.getProcessType() != null && source.getProduct() != null
				&& source.getSubscriptionBase() != null)
		{
			target.setName(
					RECOMMENDATION_NAME + source.getPrincipalData().getUid() + SEPARATOR + source.getSubscriptionBase().getCode()
							+ SEPARATOR + source.getProcessType().getCode() + SEPARATOR + source.getProduct().getCode());
		}
	}
}
