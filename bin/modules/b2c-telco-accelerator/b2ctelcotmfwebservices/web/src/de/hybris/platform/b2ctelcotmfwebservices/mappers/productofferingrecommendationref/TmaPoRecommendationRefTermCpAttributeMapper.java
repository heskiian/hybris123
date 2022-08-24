/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingrecommendationref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRecommendationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTermRef;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product offering term attribute between {@link ProductData} and
 * {@link ProductOfferingRecommendationRef}
 * Used with Composite Prices.
 *
 * @since 2007
 */
public class TmaPoRecommendationRefTermCpAttributeMapper
		extends TmaAttributeMapper<ProductData, ProductOfferingRecommendationRef>
{
	private MapperFacade mapperFacade;

	public TmaPoRecommendationRefTermCpAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(ProductData source, ProductOfferingRecommendationRef target,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPrices()))
		{
			return;
		}

		Map<String, SubscriptionTermData> subscriptionTermDataMap = new HashMap<>();
		for (PriceData price : source.getPrices())
		{
			if (CollectionUtils.isEmpty(price.getSubscriptionTerms()))
			{
				return;
			}

			price.getSubscriptionTerms().forEach((SubscriptionTermData subscriptionTermData) -> {
				if (!subscriptionTermDataMap.containsKey(subscriptionTermData.getId()))
				{
					subscriptionTermDataMap.put(subscriptionTermData.getId(), subscriptionTermData);
				}
			});
		}
		final List<ProductOfferingTermRef> productOfferingTermList = new ArrayList<>();

		subscriptionTermDataMap.values().forEach(subscriptionTermData -> {
			final ProductOfferingTermRef productOfferingTerm = getMapperFacade()
					.map(subscriptionTermData, ProductOfferingTermRef.class, context);
			productOfferingTermList.add(productOfferingTerm);
		});

		target.setProductOfferingTerm(productOfferingTermList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
