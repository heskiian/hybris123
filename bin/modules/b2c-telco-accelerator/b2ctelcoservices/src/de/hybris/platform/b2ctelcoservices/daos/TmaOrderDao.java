/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.daos.OrderDao;

import java.util.List;
import java.util.Set;


/**
 * Data Access Object responsible for handling order entities.
 *
 * @since 2105
 */
public interface TmaOrderDao extends OrderDao
{
	/**
	 * Returns a list of orders owned by the provided principals.
	 *
	 * @param principals
	 * 		The owners of the order
	 * @return List of {@link OrderModel}
	 */
	List<OrderModel> findOrders(final Set<PrincipalModel> principals);
}
