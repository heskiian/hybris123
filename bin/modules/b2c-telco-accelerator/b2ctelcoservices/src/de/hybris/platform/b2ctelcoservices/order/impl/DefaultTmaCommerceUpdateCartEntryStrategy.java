/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Strategy responsible for handling update cart entry functionality.
 *
 * @since 1911
 */
public class DefaultTmaCommerceUpdateCartEntryStrategy extends DefaultCommerceUpdateCartEntryStrategy
{
	public void beforeUpdateEntry(final CommerceCartParameter commerceCartParameter)
	{
		super.beforeUpdateCartEntry(commerceCartParameter);
	}

	public void afterUpdateEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		super.afterUpdateCartEntry(parameter, result);
	}

	@Override
	protected CommerceCartModification modifyEntry(final CartModel cartModel, final AbstractOrderEntryModel entryToUpdate,
			final long actualAllowedQuantityChange, final long newQuantity, final Integer maxOrderQuantity)
	{
		final AbstractOrderEntryModel parentEntry = entryToUpdate.getMasterEntry();
		final CommerceCartModification modification = super
				.modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange, newQuantity, maxOrderQuantity);
		modification.setParentEntry(parentEntry);

		return modification;
	}
}
