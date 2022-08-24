/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;


/**
 * Builder for creating {@link GenericConditionList} to be used for enhancing the search query.
 *
 * @param <T>
 * 		parameter based on which the search query will be enhanced.
 * @since 1810
 */
public interface TmaGenericConditionBuilder<T>
{
	/**
	 * Builds a {@link GenericConditionList} for the given param.
	 *
	 * @param query
	 * 		the search query to be enhanced
	 * @param param
	 * 		parameter based on which the search query will be enhanced.
	 * @return created generic condition
	 * @throws TmaSearchQueryException
	 * 		exception to be thrown in case a generic condition cannot be created
	 */
	GenericConditionList buildQueryConditions(final GenericQuery query, final T param) throws TmaSearchQueryException;
}
