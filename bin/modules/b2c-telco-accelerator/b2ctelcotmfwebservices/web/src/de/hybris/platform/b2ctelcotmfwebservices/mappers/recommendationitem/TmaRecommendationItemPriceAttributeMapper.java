/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendationitem;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecommendationItem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link TmaOfferData} and {@link RecommendationItem}
 *
 * @since 1907
 */
public class TmaRecommendationItemPriceAttributeMapper extends TmaAttributeMapper<TmaOfferData, RecommendationItem>
{
	 /**
	  * Mapper facade
	  */
	 private MapperFacade mapperFacade;

	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, RecommendationItem target, MappingContext context)
	 {
		if (source.getProduct() != null && ObjectUtils.isEmpty(source.getProduct().getMainSpoPriceInBpo()))
		{
			 return;
		}

		final List<ProductOfferingPrice> productOfferingPriceList = new ArrayList<>();

		 productOfferingPriceList
				 .add(getMapperFacade().map(source.getProduct().getMainSpoPriceInBpo(), ProductOfferingPrice.class, context));

		target.setPrice(productOfferingPriceList);
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
