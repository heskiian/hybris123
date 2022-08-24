/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingrecommendationref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRecommendationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingTermRef;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product offering term attribute between {@link ProductData} and
 * {@link ProductOfferingRecommendationRef}
 *
 * @since 1907
 */
public class TmaPoRecommendationRefTermAttributeMapper
	 extends TmaAttributeMapper<ProductData, ProductOfferingRecommendationRef>
{
	 /**
	  * Mapper facade
	  */
	 private MapperFacade mapperFacade;

	 @Override
	 public void populateTargetAttributeFromSource(ProductData source, ProductOfferingRecommendationRef target,
		  MappingContext context)
	 {
		  if (CollectionUtils.isEmpty(source.getProductOfferingPrices()))
		  {
				return;
		  }

		  Map<String, SubscriptionTermData> subscriptionTermDataMap = new HashMap<>();
		  for (SubscriptionPricePlanData price : source.getProductOfferingPrices())
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

	 @Required
	 public void setMapperFacade(MapperFacade mapperFacade)
	 {
		  this.mapperFacade = mapperFacade;
	 }
}
