/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.search;

import de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator.TmaSolrProductOfferingCollapsePopulator;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * This clause will collapse the solr results
 * - will sort the prices for an spo by OTC and RC (lowest to highest)
 * - will return only one price for an spo
 *
 * @param <INDEXED_PROPERTY_TYPE>
 * @param <INDEXED_TYPE_SORT_TYPE>
 * @since 1810
 * @deprecated since 2007. Use {@link TmaSolrProductOfferingCollapsePopulator} instead.
 */
@Deprecated(since = "2007")
public class TmaEligibilitySolrSortAndCollapsePopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private CommerceCommonI18NService i18nService;

	@Override
	public void populate(
			SearchQueryPageableData<SolrSearchQueryData> source,
			SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
			throws ConversionException
	{
		SearchQuery searchQuery = target.getSearchQuery();
		// collapse + sort to get only one eligible price in the result (maybe move clause to one additional populator?) `
		// todo grouping instead of this ?
		String currencyCode = i18nService.getCurrentCurrency().getIsocode().toLowerCase();
		String priceOtcFieldName = "priceOtc_" + currencyCode + "_double";
		String priceRceFieldName = "priceRcFirst_" + currencyCode + "_double";
		searchQuery.addFilterRawQuery("{"
				+ "!collapse field=spo_string "
				+ "sort=\"if(exists(" + priceOtcFieldName + ")," + priceOtcFieldName + "," + priceRceFieldName + ") asc\"" + "}");
	}

	public CommerceCommonI18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(CommerceCommonI18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}
