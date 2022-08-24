/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Adds the search fields to the search query.
 */
public class GuidedSellingSearchSolrQueryPopulator
		implements Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest>
{
	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source, final SolrSearchRequest target)
	{
		final SearchQuery searchQuery = (SearchQuery) target.getSearchQuery();

		if (searchQuery == null || !hasCategoryCode(source) || !hasFilters(source))
		{
			return;
		}
		for (final SolrSearchQueryTermData solrSearchQueryTermData : source.getSearchQueryData().getFilterTerms())
		{
			searchQuery.addQuery(solrSearchQueryTermData.getKey(), solrSearchQueryTermData.getValue());
		}
	}

	private boolean hasCategoryCode(SearchQueryPageableData<SolrSearchQueryData> source)
	{
		return StringUtils.isEmpty(source.getSearchQueryData().getCategoryCode());
	}

	private boolean hasFilters(SearchQueryPageableData<SolrSearchQueryData> source)
	{
		return CollectionUtils.isNotEmpty(source.getSearchQueryData().getFilterTerms());
	}
}
