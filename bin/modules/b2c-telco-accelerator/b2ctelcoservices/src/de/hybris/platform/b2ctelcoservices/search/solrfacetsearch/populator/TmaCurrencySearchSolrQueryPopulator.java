/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will filter the results based on the currency.
 *
 * @since 1903
 */
public class TmaCurrencySearchSolrQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String CURRENCY_FIELD = "currency_string";

	private CommonI18NService i18NService;

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final String currency = getI18NService().getCurrentCurrency().getIsocode();
		target.getSearchQuery().addFilterQuery(CURRENCY_FIELD, SearchQuery.Operator.OR, currency);
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(CommonI18NService i18NService)
	{
		this.i18NService = i18NService;
	}

}
