/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceAddToCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link CommerceAddToCartMethodHook} responsible for performing compatibility operations when updating the cart.
 *
 * @since 2105
 */
public class TmaCompatibilityAddToCartMethodHook implements CommerceAddToCartMethodHook
{
	private TmaCompatibilityPolicyEngine compatibilityPolicyEngine;
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaCompatibilityAddToCartMethodHook(final TmaCompatibilityPolicyEngine compatibilityPolicyEngine,
			final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.compatibilityPolicyEngine = compatibilityPolicyEngine;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void beforeAddToCart(CommerceCartParameter parameter)
	{
		//no implementation needed
	}

	/**
	 * Verifies the compatibility policies for the product offerings found on cart.
	 *
	 * @param parameter
	 * 		A parameter object
	 * @param result
	 * 		A return value of addToCart method
	 */
	@Override
	public void afterAddToCart(CommerceCartParameter parameter, CommerceCartModification result)
	{
		if (result.getQuantityAdded() <= 0 || result.getEntry() == null)
		{
			return;
		}

		final AbstractOrderEntryModel orderEntry = result.getEntry();
		final AbstractOrderModel order = orderEntry.getOrder();

		if (orderEntry.getMasterEntry() == null && CollectionUtils.isEmpty(orderEntry.getChildEntries()))
		{
			compatibilityPolicyEngine.verifyCompatibilityPoliciesForStandaloneProducts(order);
			return;
		}

		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(orderEntry);
		getCompatibilityPolicyEngine().verifyCompatibilityPolicies(rootEntry, order.getUser());
	}

	protected TmaCompatibilityPolicyEngine getCompatibilityPolicyEngine()
	{
		return compatibilityPolicyEngine;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
