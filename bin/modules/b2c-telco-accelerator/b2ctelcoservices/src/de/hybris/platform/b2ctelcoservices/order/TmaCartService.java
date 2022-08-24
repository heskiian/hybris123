/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;


/**
 * Service handling add to cart functionality
 *
 * @since 1907
 */
public interface TmaCartService extends CartService
{
	/**
	 * Creates a cart for the provided user.
	 *
	 * @param userModel
	 *           The user for which the cart will be created
	 * @param baseSiteModel
	 *           The base site for which the cart will be created
	 * @return The cart which was created.
	 */
	CartModel createCartForUserAndBaseSite(UserModel userModel, BaseSiteModel baseSiteModel);

	/**
	 * This method validates the applied eligibility
	 *
	 * @param cart
	 *           The cart
	 * @return false all the applied eligibility are valid, true otherwise
	 *
	 */
	boolean validateCartForEligibilityRules(AbstractOrderModel cart);

}
