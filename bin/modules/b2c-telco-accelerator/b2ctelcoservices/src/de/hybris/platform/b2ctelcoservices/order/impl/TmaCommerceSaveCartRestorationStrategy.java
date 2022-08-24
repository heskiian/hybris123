/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartRestoration;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceSaveCartRestorationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.ArrayList;
import java.util.List;


/***
 * TmaCommerceSaveCartRestorationStrategy, an extension of {@link DefaultCommerceSaveCartRestorationStrategy} to restore
 * given car for the given user.
 *
 * @since 1911
 */
public class TmaCommerceSaveCartRestorationStrategy extends DefaultCommerceSaveCartRestorationStrategy implements TmaCartStrategy
{

	/**
	 * Restores given cart for the given user
	 *
	 * @param CommerceCartParameter
	 * @return the CommerceCartModification List
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> cartModifications = new ArrayList<>();

		for (final CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{
			try
			{
				final CommerceCartRestoration restoration = super.restoreCart(commerceCartParameter);
				cartModifications.addAll(restoration.getModifications());
			}
			catch (final CommerceCartRestorationException ex)
			{
				throw new CommerceCartModificationException(ex.getMessage(), ex);
			}
		}

		return cartModifications;
	}

}
