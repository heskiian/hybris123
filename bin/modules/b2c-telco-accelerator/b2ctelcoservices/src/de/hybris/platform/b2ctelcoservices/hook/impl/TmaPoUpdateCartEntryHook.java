/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.hook.TmaCartHookHelper;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;


/**
 * {@link TmaProductOfferingModel} specific cart entry update operations.
 *
 * @since 6.7
 */
public class TmaPoUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{
	private EntryGroupService entryGroupService;

	private TmaCompatibilityPolicyEngine compatibilityPoliciesEngine;

	private ModelService modelService;

	private TmaCartHookHelper tmaCartHookHelper;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaPoUpdateCartEntryHook(final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter parameter)
	{
		final AbstractOrderEntryModel entry = getEntryToUpdate(parameter);

		parameter.setProduct(entry.getProduct());
		parameter.setUser(entry.getOrder().getUser());
	}

	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		if (parameter.getProduct() == null)
		{
			return;
		}

		final CartModel cart = parameter.getCart();

		if (result.getParentEntry() == null) // NOSONAR
		{
			getCompatibilityPoliciesEngine().verifyCompatibilityPoliciesForStandaloneProducts(cart);
			return;
		}

		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(result.getParentEntry());

		getTmaCartHookHelper().recalculateEntries(rootEntry);

		if (!parameter.isAutomaticallyAdded())
		{
			getCompatibilityPoliciesEngine().verifyCompatibilityPolicies(rootEntry, cart.getUser());
		}
	}

	private AbstractOrderEntryModel getEntryToUpdate(final CommerceCartParameter parameter)
	{
		if (parameter.getCart().getEntries() == null)
		{
			throw new IllegalArgumentException("Cart " + parameter.getCart().getCode() + " has no entries");
		}
		return parameter.getCart().getEntries().stream()
				.filter(e -> parameter.getEntryNumber() == e.getEntryNumber().longValue())
				.findAny().orElseThrow(
						() -> new IllegalArgumentException("Entry #" + parameter.getEntryNumber() + " was not found in cart"
								+ parameter.getCart().getCode()));
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

	protected TmaCompatibilityPolicyEngine getCompatibilityPoliciesEngine()
	{
		return compatibilityPoliciesEngine;
	}

	@Required
	public void setCompatibilityPoliciesEngine(
			final TmaCompatibilityPolicyEngine compatibilityPoliciesEngine)
	{
		this.compatibilityPoliciesEngine = compatibilityPoliciesEngine;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TmaCartHookHelper getTmaCartHookHelper()
	{
		return tmaCartHookHelper;
	}

	@Required
	public void setTmaCartHookHelper(TmaCartHookHelper tmaCartHookHelper)
	{
		this.tmaCartHookHelper = tmaCartHookHelper;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
