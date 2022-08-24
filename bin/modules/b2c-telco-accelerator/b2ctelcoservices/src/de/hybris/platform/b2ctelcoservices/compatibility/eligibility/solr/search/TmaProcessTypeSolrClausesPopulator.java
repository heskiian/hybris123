/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.search;


import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * This clause will also provide empty process types in the results.
 *
 * @param <INDEXED_PROPERTY_TYPE>
 * @param <INDEXED_TYPE_SORT_TYPE>
 * @since 2108
 */
public class TmaProcessTypeSolrClausesPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{

	public static final String PROCESS_FIELD = "process";
	public static final String ALL_PROCESS_TYPES = " ";

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final SolrSearchQueryData searchQueryData = target.getSearchQueryData();

		if (!ObjectUtils.isEmpty(target.getSearchQueryData())
				&& CollectionUtils.isNotEmpty(target.getSearchQueryData().getFilterTerms())
				&& target.getSearchQueryData().getFilterTerms().stream().anyMatch(term -> term.getKey().equals(PROCESS_FIELD)))
		{
			final SolrSearchQueryTermData query = new SolrSearchQueryTermData();
			query.setKey(PROCESS_FIELD);
			query.setValue(ALL_PROCESS_TYPES);
			searchQueryData.getFilterTerms().add(query);

			final SearchQuery targetSearchQuery = target.getSearchQuery();

			if (targetSearchQuery != null)
			{
				targetSearchQuery.addFacetValue(PROCESS_FIELD, ALL_PROCESS_TYPES);
			}
		}
	}
}
