/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Resets delivery mode to null every time on updating cart to avoid keeping inappropriate delivery mode for the new
 * items.
 */
public class ResetDeliveryMethodOnUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{

	private CartService cartService;

	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter arg0)
	{
		return; //NOPMD
	}

	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter arg0, final CommerceCartModification arg1)
	{
		final CartModel sessionCart = getCartService().getSessionCart();
		if (sessionCart != null)
		{
			sessionCart.setDeliveryMode(null);
		}
	}

	/**
	 * @return the cartService
	 */
	protected CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}
}
