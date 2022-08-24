/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.GenericQuery;


/**
 * Search processor for enhancing the search query with given params.
 *
 * @param <T>
 * 		parameter based on which the search query will be enhanced.
 * @since 1810
 */
public interface TmaGenericSearchProcessor<T>
{
	/**
	 * Enhances the given query.
	 *
	 * @param query
	 * 		the {@link GenericQuery} to be updated.
	 * @throws TmaSearchQueryException
	 * 		exception to be thrown in case the query cannot be enhanced
	 */
	void enhanceQuery(final GenericQuery query) throws TmaSearchQueryException;

	/**
	 * Enhances the given query based on the given param.
	 *
	 * @param query
	 * 		the {@link GenericQuery} to be updated.
	 * @param param
	 * 		<T> param used for enhancing the query
	 * @throws TmaSearchQueryException
	 * 		exception to be thrown in case the query cannot be enhanced
	 */
	void enhanceQuery(final GenericQuery query, final T param) throws TmaSearchQueryException;
}
