/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.indexedproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.FacetSearchOption;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndexedProductOffering;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for facetSearchOption attribute between {@link SolrSearchQueryTermData} and
 * {@link IndexedProductOffering}
 *
 * @since 1907
 */
public class TmaIndexedPoFacetSearchOptionAttributeMapper
	 extends TmaAttributeMapper<SolrSearchQueryTermData, IndexedProductOffering>
{
	 /**
	  * Mapper facade
	  */
	 private MapperFacade mapperFacade;

	 @Override
	 public void populateTargetAttributeFromSource(SolrSearchQueryTermData source, IndexedProductOffering target,
		  MappingContext context)
	 {
		  if (source != null)
		  {
				target.addFacetSearchOptionItem(getMapperFacade().map(source, FacetSearchOption.class, context));
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
