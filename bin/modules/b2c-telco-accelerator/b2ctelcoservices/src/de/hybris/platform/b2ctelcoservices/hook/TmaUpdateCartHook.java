/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.hook;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * A hook strategy to run custom code before/after updating a cart
 *
 * @since 1911
 */
public interface TmaUpdateCartHook
{
	/**
	 * Executed before cart update
	 *
	 * @param parameter
	 * 		contains attributes used for cart updates
	 */
	void beforeUpdateCart(CommerceCartParameter parameter);

	/**
	 * Executed after cart update
	 *
	 * @param parameter
	 * 		contains attributes used for cart updates
	 * @param result
	 * 		contains the modifications made on the cart
	 */
	void afterUpdateCart(CommerceCartParameter parameter, CommerceCartModification result);
}
