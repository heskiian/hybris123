/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Resets delivery mode to null every time on adding a new product to the cart to avoid keeping inappropriate delivery
 * mode for the new items.
 */
public class ResetDeliveryMethodOnAddToCartMethodHook implements CommerceAddToCartMethodHook
{

	private CartService cartService;


	@Override
	public void beforeAddToCart(final CommerceCartParameter paramCommerceCartParameter) throws CommerceCartModificationException
	{
		return; //NOPMD
	}


	@Override
	public void afterAddToCart(final CommerceCartParameter paramCommerceCartParameter,
			final CommerceCartModification paramCommerceCartModification) throws CommerceCartModificationException
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
