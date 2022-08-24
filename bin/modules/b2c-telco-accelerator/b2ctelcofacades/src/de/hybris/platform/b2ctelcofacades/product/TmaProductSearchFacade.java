/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.b2ctelcofacades.data.ProductOfferingSearchContextData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductResultData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;

import java.util.Collection;
import java.util.List;


/**
 * Facade handling ProductOffering search operations.
 *
 * @param <T>
 *           Class that extends {@link ProductData}
 * @since 1907
 */
public interface TmaProductSearchFacade<T extends ProductData>
{
	/**
	 * Calculates page number from offset and limit and returns those page/pages which contain the requested Product
	 * Offerings.
	 *
	 * @param searchQuery
	 *           The query to be executed
	 * @param offset
	 *           The nth Product Offering from which limit number of Product Offerings will be sent (starts from 0)
	 * @param limit
	 *           The number of Product Offerings
	 * @return List of {@link ProductSearchPageData} which contains the requested product offerings
	 */
	List<FacetSearchPageData<SearchStateData, T>> getSolrSearchResults(SolrSearchQueryData searchQuery, Integer offset,
			Integer limit);

	/**
	 * Creates a list of {@link SolrSearchQueryTermData} from the parameter provided.
	 *
	 * @param facetSearchOption
	 *           list of filter options
	 * @return List of {@link SolrSearchQueryTermData}
	 */
	List<SolrSearchQueryTermData> decodeFacetSearchQuery(final List<String> facetSearchOption);

	/**
	 * Retrieve {@link ProductResultData} for given {@link ProductOfferingSearchContext} and collection of
	 * {@link ProductOption}
	 *
	 * @param productOfferingSearchContextData
	 *           product search context object stores all attributes for search
	 * @param options
	 *           options set that determines amount of information that will be attached to the returned product.
	 * @return {@link ProductResultData}
	 * @deprecated since 1911 - use getAllProductsForOptions method of ProductExportFacade instead
	 */
	@Deprecated(since = "1911", forRemoval= true)
	ProductResultData getProductOfferingsForOptions(final ProductOfferingSearchContextData productOfferingSearchContext,
			final Collection<ProductOption> options);
}
