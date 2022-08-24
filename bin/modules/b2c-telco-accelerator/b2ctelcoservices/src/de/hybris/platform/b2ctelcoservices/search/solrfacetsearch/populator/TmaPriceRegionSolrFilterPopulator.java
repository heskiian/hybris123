/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;


import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * This populator will filter the results based on the region of price plan.
 *
 * @since 1907
 */
public class TmaPriceRegionSolrFilterPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String REGION_FIELD = "region";
	private static final String ALL_REGION_PRICES = " ";
	private TmaRegionService tmaRegionService;

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final SolrSearchQueryData searchQueryData = target.getSearchQueryData();
		if (!ObjectUtils.isEmpty(target.getSearchQueryData())
				&& CollectionUtils.isNotEmpty(target.getSearchQueryData().getFilterTerms())
				&& target.getSearchQueryData().getFilterTerms().stream().anyMatch(term -> term.getKey().equals(REGION_FIELD)))
		{
			target.getSearchQueryData().getFilterTerms().forEach(term ->
			{
				if (term.getKey().equals(REGION_FIELD))
				{
					getTmaRegionService().findRegionByIsocode(term.getValue());
				}
			});
			final SolrSearchQueryTermData query = new SolrSearchQueryTermData();
			query.setKey(REGION_FIELD);
			query.setValue(ALL_REGION_PRICES);
			searchQueryData.getFilterTerms().add(query);

			final SearchQuery targetSearchQuery = target.getSearchQuery();

			if (targetSearchQuery != null)
			{
				targetSearchQuery.addFacetValue(REGION_FIELD, ALL_REGION_PRICES);
			}
		}
	}

	protected TmaRegionService getTmaRegionService()
	{
		return tmaRegionService;
	}

	@Required
	public void setTmaRegionService(final TmaRegionService tmaRegionService)
	{
		this.tmaRegionService = tmaRegionService;
	}
}
