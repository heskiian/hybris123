/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendationitem;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRecommendationRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecommendationItem;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for offering attribute between {@link TmaOfferData} and {@link RecommendationItem}
 *
 * @since 1907
 */
public class TmaRecommendationItemOfferingAttributeMapper extends TmaAttributeMapper<TmaOfferData, RecommendationItem>
{
	 /**
	  * Mapper facade
	  */
	 private MapperFacade mapperFacade;

	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, RecommendationItem target, MappingContext context)
	 {
		  if (source.getProduct() != null)
		  {
				target.setOffering(getMapperFacade().map(source.getProduct(), ProductOfferingRecommendationRef.class, context));
		  }
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
