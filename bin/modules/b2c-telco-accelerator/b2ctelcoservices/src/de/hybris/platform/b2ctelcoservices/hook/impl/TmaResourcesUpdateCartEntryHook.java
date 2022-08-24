/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;


/**
 * Resources specific cart entry update operations.
 *
 * @since 1911
 */
public class TmaResourcesUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{
	private TmaCommerceCartResourceService tmaCommerceCartResourceService;

	public TmaResourcesUpdateCartEntryHook(TmaCommerceCartResourceService tmaCommerceCartResourceService)
	{
		this.tmaCommerceCartResourceService = tmaCommerceCartResourceService;
	}

	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter parameter)
	{
		List<TmaCartValidationResult> cartValidationResultList = getTmaCommerceCartResourceService().validateResources(parameter);

		for (TmaCartValidationResult cartValidationResult : cartValidationResultList)
		{
			if (!cartValidationResult.isValid())
			{
				throw new IllegalArgumentException(cartValidationResult.getMessage());
			}
		}

	}

	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		// implementation not required
	}

	protected TmaCommerceCartResourceService getTmaCommerceCartResourceService()
	{
		return tmaCommerceCartResourceService;
	}
}
