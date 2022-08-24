/*
 * Copyright (c)  2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Strategy to process autopick actions
 *
 * @since 2102
 */
public class TmaAutopickPolicyActionStrategy extends TmaCompatibilityPolicyActionStrategy
{
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaAutopickPolicyActionStrategy(final TmaPolicyActionResolver policyActionResolver, final ModelService modelService,
			final EntryGroupService entryGroupService, final TmaValidationMessagesStrategy validationMessagesStrategy,
			final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		super(policyActionResolver, modelService, entryGroupService, validationMessagesStrategy);
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	protected AbstractOrderEntryModel getParentEntry(final AbstractOrderEntryModel entry)
	{
		return getAbstractOrderEntryService().getRootEntry(entry);
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
