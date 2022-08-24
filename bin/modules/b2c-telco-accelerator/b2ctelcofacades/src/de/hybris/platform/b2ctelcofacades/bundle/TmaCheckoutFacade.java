/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.bundle;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.b2ctelcoservices.order.exception.OrderProcessingException;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;

import java.util.List;


/**
 * Facade that extends CheckoutFacade with telco related functionality.
 *
 * @since 1907
 */
public interface TmaCheckoutFacade extends AcceleratorCheckoutFacade
{
	/**
	 * Returns a list of supported delivery modes for the cart and user provided.
	 *
	 * @param cartCode
	 * 		Unique identifier of the cart.
	 * @param userCode
	 * 		Unique identifier of the user.
	 * @return List of supported delivery modes for the provided cart and user.
	 */
	List<DeliveryModeData> getSupportedDeliveryModesForCartAndUser(String cartCode, String userCode);

	/**
	 * Creates an order getting data from cart resource identified by the given identifier
	 *
	 * @param cartId identifier of the cart
	 * @param userId identifier of the cart creator
	 * @param removeCart flag indicating whether cart should be removed once the order is created
	 * @return order DTO object with the details of the order
	 */
	OrderData placeOrderFromCart(String cartId, String userId, boolean removeCart) throws OrderProcessingException;

	/**
	 * Creates an order getting data from order Dto provided.
	 *
	 * @param orderData order input
	 * @param userId identifier if the cart creator
	 * @return the order Dto containing the data of the created order resource
	 */
	OrderData placeOrderFromDto(OrderData orderData, String userId) throws OrderProcessingException;
}
