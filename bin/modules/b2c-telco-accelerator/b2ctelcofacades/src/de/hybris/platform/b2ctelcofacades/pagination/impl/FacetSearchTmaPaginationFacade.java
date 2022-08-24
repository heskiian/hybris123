/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.pagination.impl;

import de.hybris.platform.b2ctelcofacades.pagination.TmaFacetSearchPaginationFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of {@link TmaFacetSearchPaginationFacade}
 *
 * @since 1907
 */
public class FacetSearchTmaPaginationFacade extends DefaultTmaPaginationFacade implements TmaFacetSearchPaginationFacade
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
	@Override
	public <S> List<S> getItemsFromPagesWithOffsetAndLimit(List<FacetSearchPageData<SearchStateData, S>> sourceResults,
			Integer offset, Integer limit)
	{
		if (sourceResults.size() == 1)
		{
			return sourceResults.get(0).getResults();
		}

		final List<S> itemsDataList = new ArrayList<>();

		int startindex = Math.max(0, offset % limit);
		int endIndex = sourceResults.get(0).getResults().size();

		for (int i = startindex; i < endIndex; i++)
		{
			itemsDataList.add(sourceResults.get(0).getResults().get(i));
		}

		if (sourceResults.size() == 1)
		{
			return itemsDataList;
		}

		endIndex = Math.min(sourceResults.get(1).getResults().size(), startindex);

		for (int i = 0; i < endIndex; i++)
		{
			itemsDataList.add(sourceResults.get(1).getResults().get(i));
		}

		return itemsDataList;
	}
}
