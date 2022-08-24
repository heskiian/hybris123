/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.configurableguidedselling.impl;

import de.hybris.platform.b2ctelcofacades.configurableguidedselling.EntryGroupFacade;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.EntryGroupService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation for {@link EntryGroupFacade}.
 *
 * @since 6.6
 *
 */
public class DefaultEntryGroupFacade implements EntryGroupFacade
{
	private EntryGroupService entryGroupService;
	private CartService cartService;

	@Override
	public EntryGroup getRootEntryGroup(final Integer groupNumber)
	{
		return getEntryGroupService().getRoot(getCartService().getSessionCart(), groupNumber);
	}

	@Override
	public EntryGroup getCurrentEntryGroup(final Integer groupNumber)
	{
		return getEntryGroupService().getGroup(getCartService().getSessionCart(), groupNumber);
	}

	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	@Required
	public void setEntryGroupService(final EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}
