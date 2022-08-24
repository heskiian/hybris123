/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;


/**
 * Service handling cart resources functionality.
 *
 * @since 1911
 */
public interface TmaCommerceCartResourceService
{
	/**
	 * Validates the resources to be stored on cart.
	 *
	 * @param parameter
	 * 		commerce cart parameter containing resources to be stored on cart
	 * @return List of {@link TmaCartValidationResult} containing information about the validation of the parameter
	 */
	List<TmaCartValidationResult> validateResources(final CommerceCartParameter parameter);

	/**
	 * Updates the resources on cart based on the information from the {@param CommerceCartParameter}.
	 * The {@param CommerceCartModification} will be updated to reflect the result of the this operation.
	 *
	 * @param parameter
	 * 		commerce cart parameter containing resources to be stored on cart
	 * @param commerceCartModification
	 * 		contains the updates made on the cart
	 * @throws CommerceCartModificationException
	 * 		in case of any error occurs during cart update
	 */
	void updateResources(final CommerceCartParameter parameter,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException;
}
