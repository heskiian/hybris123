/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.pagination;

import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;

import java.util.List;


/**
 * Facade handling operations for pagination when the input models are obtained from FacetSearchPageData.
 *
 * @since 1907
 */
public interface TmaFacetSearchPaginationFacade extends TmaPaginationFacade
{
	/**
	 * Creates and returns list of limit number of items starting from the offsetth member
	 *
	 * @param sourceResults
	 * 		List of search results as FacetSearchPageData
	 * @param offset
	 * 		The nth item from which limit number of Product Offerings will be sent (starts from 0)
	 * @param limit
	 * 		The number of items
	 * @return List of limit number of items
	 */
	<S> List<S> getItemsFromPagesWithOffsetAndLimit(List<FacetSearchPageData<SearchStateData, S>> sourceResults,
			Integer offset, Integer limit);
}
