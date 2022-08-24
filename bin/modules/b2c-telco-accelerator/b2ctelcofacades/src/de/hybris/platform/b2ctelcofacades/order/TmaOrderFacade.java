/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;

import java.util.List;


/**
 * Facade that handles operations related to {@link OrderData}.
 *
 * @since 2102
 */
public interface TmaOrderFacade extends OrderFacade
{
	/**
	 * Returns the details of an Order.
	 *
	 * @param code
	 * 		The id of the Order for which to retrieve the detail.
	 * @param relatedPartyId
	 * 		The id of the customer who owns the order
	 * @return The detail of the order with matching code and relatedPartyId
	 */
	OrderData getOrderDetails(final String code, final String relatedPartyId);

	/**
	 * Provides the list of orders related to the principal provided by its id.
	 *
	 * @param principalId
	 * 		The id of the principal having access to orders
	 * @return the list of orders related to the principal provided by its id
	 */
	List<OrderData> getOrders(final String principalId);

	/**
	 * Updates the status of the order.
	 *
	 * @param code
	 * 		The identifier of the order
	 * @param principalId
	 * 		The identifier of the principal
	 * @param status
	 * 		The new status
	 * @throws IllegalAccessException
	 * 		If the order status updated is not allowed
	 */
	void updateStatus(final String code, final String principalId, final String status) throws IllegalAccessException;


	/**
	 * Checks if authenticated user provided by its id is able to access any orders related to the principal provided by its id.
	 *
	 * @param authenticatedUserId
	 * 		The identifier of the authenticated user
	 * @param principalId
	 * 		The identifier of the principal for which the request was made
	 * @return True if user can access orders related to the principal, false otherwise
	 */
	boolean hasUserAccessToOrders(final String authenticatedUserId, final String principalId);

	/**
	 * Checks if a specific user can update the status of an order.
	 *
	 * @param principalId
	 * 		The owner of the order
	 * @param authenticatedUserId
	 * 		The identifier of the authenticated user
	 * @param orderId
	 * 		The identifier of the order
	 * @return True if authenticated user has the right to updated the status of the specified order, false otherwise
	 */
	boolean canUserUpdateOrderStatus(final String principalId, final String authenticatedUserId, final String orderId);
}
