/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.facetsearchoption;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.FacetSearchOption;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for name attribute between {@link SolrSearchQueryTermData} and {@link FacetSearchOption}
 *
 * @since 1907
 */
public class TmaFacetSearchOptionNameAttributeMapper extends TmaAttributeMapper<SolrSearchQueryTermData, FacetSearchOption>
{
	 @Override
	 public void populateTargetAttributeFromSource(SolrSearchQueryTermData source, FacetSearchOption target, MappingContext context)
	 {
		  if (StringUtils.isNotEmpty(source.getKey()))
		  {
		  	 target.setName(source.getKey());
		  }
	 }
}
