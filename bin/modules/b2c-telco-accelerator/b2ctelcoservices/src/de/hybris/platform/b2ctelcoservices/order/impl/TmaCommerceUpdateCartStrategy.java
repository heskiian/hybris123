/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Cart strategy implementation for updating product offering in cart.
 *
 * @since 1907
 * @deprecated since 1911. Use {@link TmaUpdateCartStrategy}
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaCommerceUpdateCartStrategy extends DefaultCommerceUpdateCartEntryStrategy implements TmaCartStrategy
{
	private static final long DEFAULT_ENTRY_NUMBER = -1;

	private TmaCommerceCartResourceService commerceCartResourceService;


	@Override
	public List<CommerceCartModification> processCartAction(final List<CommerceCartParameter> commerceCartParameterList)
			throws CommerceCartModificationException
	{
		final List<CommerceCartModification> commerceCartModifications = new ArrayList<>();

		for (final CommerceCartParameter commerceCartParameter : commerceCartParameterList)
		{
			if (isBpo(commerceCartParameter))
			{
				throw new CommerceCartModificationException("Can't update quantity for bundled product offering in cart "
						+ commerceCartParameter.getCart().getCode() + ".");
			}

			if (commerceCartParameter.getEntryNumber() != DEFAULT_ENTRY_NUMBER)
			{
				beforeUpdateCartEntry(commerceCartParameter);
				final CommerceCartModification commerceCartModification = updateQuantityForCartEntry(commerceCartParameter);
				afterUpdateCartEntry(commerceCartParameter, commerceCartModification);

				commerceCartModifications.add(commerceCartModification);
			}
		}

		return commerceCartModifications;
	}

	private boolean isBpo(final CommerceCartParameter commerceCartParameter)
	{
		return CollectionUtils.isNotEmpty(commerceCartParameter.getEntryGroupNumbers())
				&& commerceCartParameter.getEntryGroupNumbers().iterator().next() != DEFAULT_ENTRY_NUMBER
				&& commerceCartParameter.getEntryNumber() == DEFAULT_ENTRY_NUMBER;
	}

	protected TmaCommerceCartResourceService getCommerceCartResourceService()
	{
		return commerceCartResourceService;
	}

	@Required
	public void setCommerceCartResourceService(
			TmaCommerceCartResourceService commerceCartResourceService)
	{
		this.commerceCartResourceService = commerceCartResourceService;
	}

	@Override
	public TmaCartService getCartService()
	{
		return (TmaCartService) super.getCartService();
	}
}
