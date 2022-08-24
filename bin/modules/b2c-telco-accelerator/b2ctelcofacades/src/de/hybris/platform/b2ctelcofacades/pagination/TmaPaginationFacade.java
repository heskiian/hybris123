/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.pagination;

import de.hybris.platform.b2ctelcoservices.constants.B2ctelcoservicesConstants;

import java.util.List;

import org.springframework.util.MultiValueMap;


/**
 * Facade handling operations for pagination.
 *
 * @since 1907
 */
public interface TmaPaginationFacade
{
	/**
	 * Computes the total number of pages based on the given number of items in total and items per page.
	 *
	 * @param noOfItems
	 * 		the total number of items.
	 * @param limit
	 * 		the number of items in a page.
	 * @return the number of pages.
	 */
	Integer getTotalNumberOfPages(Integer noOfItems, Integer limit);

	/**
	 * Adds the x-total-count and pagination links headers to the response.
	 *
	 * @param header
	 * 		The header that will be used in response.
	 * @param url
	 * 		The Url of the http request.
	 * @param queryStringWithoutParams
	 * 		The url string without the offset and limit params
	 * @param offset
	 * 		The offset represents the position in list from where the result list will start.
	 * @param limit
	 * 		The limit represents the number of entries that will be selected for the result list.
	 * @param totalCount
	 * 		The total number of resources of the requested type that exist in the system.
	 */
	public void addPaginationHeadersToResponse(final MultiValueMap<String, String> header,
			final String url, final String queryStringWithoutParams, final Long totalCount, final Integer limit,
			final Integer offset);

	/**
	 * Method will check if the inputOffset is correct, otherwise it will return a default offset.
	 *
	 * @param inputOffset
	 * 		the offset set by the user.
	 * @return inputOffset if input value is correct; 0 otherwise.
	 */
	Integer checkOffset(Integer inputOffset);

	/**
	 * Method will check if the inputLimit is correct, otherwise it will return a default limit.
	 *
	 * @param inputLimit
	 * 		the limit set by the user.
	 * @return inputLimit if input value is correct; the DEFAULT_LIMIT value from {@link B2ctelcoservicesConstants} otherwise.
	 */
	Integer checkLimit(Integer inputLimit);

	/**
	 * Will filter a list of items(model or dto) by offset and limit.
	 *
	 * @param offset
	 * 		The offset represents the position in list from where the result list will start.
	 * @param limit
	 * 		The limit represents the number of entries that will be selected for the result list.
	 * @param inputList
	 * 		The initial list of objects to be filtered.
	 * @param <T>
	 * 		The type of the objects contained in the list.
	 * @return The filtered list.
	 */
	<T> List<T> filterListByOffsetAndLimit(final Integer offset, final Integer limit, final List<T> inputList);
}
