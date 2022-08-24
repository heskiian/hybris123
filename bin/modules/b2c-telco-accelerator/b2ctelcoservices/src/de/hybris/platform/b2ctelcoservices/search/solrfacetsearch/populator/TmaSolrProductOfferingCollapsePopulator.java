/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * Populator for collapsing Search Results which are returned as Product Offering Prices into Product Offerings. The collapsing
 * happens for the highest priority price depending on the {@link PriceRowModel#getPriority()} field.
 * The populator will add a clause to the Search Query which will:
 * - sort the prices by priority (highest to lowest)
 * - will return only one price for an spo and for the current currency
 *
 * @param <INDEXED_PROPERTY_TYPE>
 * 		the indexed property type - default {@link PriceRowModel}TmaSearchResultProductOfferingPopulator.java:91
 * @param <INDEXED_TYPE_SORT_TYPE>
 * 		the indexed sort type
 * @since 2007
 * @deprecated since 2011.
 */
@Deprecated(since = "2011", forRemoval= true)
public class TmaSolrProductOfferingCollapsePopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String PRICE_PRIORITY_FIELD_NAME = "pricePriority_int";
	private static final String SORT_BY_PRICE_PRIORITY_AND_COLLAPSE_QUERY = "{"
			+ "!collapse field=spo_string "
			+ "sort=\"%s desc\""
			+ "}";

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		target.getSearchQuery()
				.addFilterRawQuery(String.format(SORT_BY_PRICE_PRIORITY_AND_COLLAPSE_QUERY, PRICE_PRIORITY_FIELD_NAME));
	}
}
