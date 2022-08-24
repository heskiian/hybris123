/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.search;


import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.commons.lang.StringUtils;


/**
 * This clause will filter the results based on the eligibility context.
 *
 * @param <INDEXED_PROPERTY_TYPE>
 * @param <INDEXED_TYPE_SORT_TYPE>
 * @since 1810
 */
public class TmaEligibilitySolrClausesPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private TmaSolrEligibilityQueryBuilder eligibilityQueryBuilder;
	private TmaEligibilityContextService tmaEligibilityContextService;

	@Override
	public void populate(
			final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{
		if (!getTmaEligibilityContextService().shouldApplyEligibility())
		{
			return;
		}
		// filter out products that are not eligible only if the user is in configured usergroups
		final SearchQuery searchQuery = target.getSearchQuery();
		final String eligibilityRawQuery = eligibilityQueryBuilder.buildQuery();
		if (StringUtils.isEmpty(eligibilityRawQuery))
		{
			return;
		}
		searchQuery.addFilterRawQuery(eligibilityRawQuery);
	}

	protected TmaSolrEligibilityQueryBuilder getEligibilityQueryBuilder()
	{
		return eligibilityQueryBuilder;
	}

	public void setEligibilityQueryBuilder(
			TmaSolrEligibilityQueryBuilder eligibilityQueryBuilder)
	{
		this.eligibilityQueryBuilder = eligibilityQueryBuilder;
	}

	public TmaEligibilityContextService getTmaEligibilityContextService()
	{
		return tmaEligibilityContextService;
	}

	public void setTmaEligibilityContextService(final TmaEligibilityContextService tmaEligibilityContextService)
	{
		this.tmaEligibilityContextService = tmaEligibilityContextService;
	}
}
