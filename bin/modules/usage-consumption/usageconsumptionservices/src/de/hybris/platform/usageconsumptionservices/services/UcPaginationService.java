/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptionservices.services;


import org.springframework.util.MultiValueMap;


/**
 * Service handling operations for pagination.
 *
 * @since 2108
 */
public interface UcPaginationService
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
	void addPaginationHeadersToResponse(final MultiValueMap<String, String> header,
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
	 * @return inputLimit if input value is correct; the DEFAULT_LIMIT value from project properties otherwise.
	 */
	Integer checkLimit(Integer inputLimit);
}
