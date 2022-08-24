/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;


/**
 * Strategy handling resource validation and saving operations.
 *
 * @since 1911
 */
public interface TmaAbstractOrderResourceStrategy
{
	/**
	 * Validates that the resources from the {@link CommerceCartParameter} are valid to be stored on cart.
	 *
	 * @param parameter
	 * 		the commerce cart parameter storing information to be stored on cart
	 * @return {@link TmaCartValidationResult}
	 */
	TmaCartValidationResult validateResource(final CommerceCartParameter parameter);

	/**
	 * Updates the resource from the @{@link CommerceCartParameter} on the given order entry.
	 *
	 * @param commerceCartParameter
	 * 		contains attributes used for cart entry updates
	 * @param commerceCartModification
	 * 		contains the updates made on the cart
	 * @throws CommerceCartModificationException
	 * 		in case of any error occurs during cart update
	 */
	void updateResource(final CommerceCartParameter commerceCartParameter, final CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException;
}
