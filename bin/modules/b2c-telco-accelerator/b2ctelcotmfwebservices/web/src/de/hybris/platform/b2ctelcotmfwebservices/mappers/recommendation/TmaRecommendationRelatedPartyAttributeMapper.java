/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.recommendation;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Recommendation;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link TmaOfferData} and {@link Recommendation}
 *
 * @since 1907
 */
public class TmaRecommendationRelatedPartyAttributeMapper extends TmaAttributeMapper<TmaOfferData, Recommendation>
{
	 /**
	  * Mapper facade
	  */
	 private MapperFacade mapperFacade;

	 @Override
	 public void populateTargetAttributeFromSource(TmaOfferData source, Recommendation target,
		  MappingContext context)
	 {
		  if (source.getPrincipalData() != null)
		  {
				target.setRelatedParty(getMapperFacade().map(source.getPrincipalData(), RelatedPartyRef.class, context));
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

