/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.OrderService;

import java.util.List;
import java.util.Set;


/**
 * Service handling business logic acting upon orders.
 *
 * @since 2105
 */
public interface TmaOrderService extends OrderService
{

	/**
	 * Provides the list of orders placed by the provided principals.
	 *
	 * @param principals
	 * 		The list of principals
	 * @return List of orders placed by given principals
	 */
	List<OrderModel> getOrders(final Set<PrincipalModel> principals);
}
