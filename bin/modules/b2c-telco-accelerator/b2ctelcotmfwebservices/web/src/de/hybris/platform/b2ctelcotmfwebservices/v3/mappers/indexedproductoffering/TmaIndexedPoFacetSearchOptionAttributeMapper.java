/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.indexedproductoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.FacetSearchOption;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.IndexedProductOffering;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for facetSearchOption attribute between {@link SolrSearchQueryTermData} and
 * {@link IndexedProductOffering}
 *
 * @since 2007
 */
public class TmaIndexedPoFacetSearchOptionAttributeMapper
		extends TmaAttributeMapper<SolrSearchQueryTermData, IndexedProductOffering>
{
	private MapperFacade mapperFacade;

	public TmaIndexedPoFacetSearchOptionAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(SolrSearchQueryTermData source, IndexedProductOffering target,
			MappingContext context)
	{
		if (source != null)
		{
			target.addFacetSearchOptionItem(getMapperFacade().map(source, FacetSearchOption.class, context));
		}

	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
