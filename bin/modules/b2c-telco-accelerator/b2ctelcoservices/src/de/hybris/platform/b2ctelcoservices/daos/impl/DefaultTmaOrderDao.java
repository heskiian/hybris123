/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaOrderDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaOrderDao}
 *
 * @since 2105
 */
public class DefaultTmaOrderDao extends DefaultOrderDao implements TmaOrderDao
{
	private static final String FIND_ORDERS_BY_CUSTOMER_LIST = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.USER
			+ "} IN (?principals) AND {" + OrderModel.VERSIONID + "} IS NULL ";

	@Override
	public List<OrderModel> findOrders(final Set<PrincipalModel> principals)
	{
		if (CollectionUtils.isEmpty(principals))
		{
			throw new IllegalArgumentException("Principal must not be empty.");
		}

		final Map<String, Object> queryParams = new HashMap<>();

		queryParams.put("principals", principals);

		final SearchResult<OrderModel> result = getFlexibleSearchService().search(FIND_ORDERS_BY_CUSTOMER_LIST, queryParams);
		return result.getResult();
	}
}
