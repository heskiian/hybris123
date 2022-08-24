/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.hook;

import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.hook.CommerceUpdateCartEntryHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;
import java.util.Map;


/**
 * Hook for cardinality specific validation when updating item in cart
 *
 * @since 2011
 */
public class TmaBpoCardinalityUpdateCartEntryHook implements CommerceUpdateCartEntryHook
{
	private TmaBpoCardinalityService bpoCardinalityService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService entryGroupService;

	private TmaAbstractOrderEntryService abstractOrderEntryService;

	public TmaBpoCardinalityUpdateCartEntryHook(final TmaBpoCardinalityService bpoCardinalityService,
			final TmaEntryGroupService entryGroupService, final TmaAbstractOrderEntryService abstractOrderEntryService)
	{
		this.bpoCardinalityService = bpoCardinalityService;
		this.entryGroupService = entryGroupService;
		this.abstractOrderEntryService = abstractOrderEntryService;
	}

	@Override
	public void beforeUpdateCartEntry(final CommerceCartParameter parameter)
	{
		// no implementation needed
	}

	@Override
	public void afterUpdateCartEntry(final CommerceCartParameter parameter, final CommerceCartModification result)
	{
		if (result.getParentEntry() == null)
		{
			return;
		}

		final AbstractOrderEntryModel rootEntry = getAbstractOrderEntryService().getRootEntry(result.getParentEntry());

		if (rootEntry == null || !(rootEntry.getProduct() instanceof TmaBundledProductOfferingModel))
		{
			return;
		}

		final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap = getBpoCardinalityService()
				.verifyCardinality(rootEntry);

		validationMessagesMap.forEach((AbstractOrderEntryModel key, List<String> value) ->
				getBpoCardinalityService().updateCardinalityValidationMessages(key, value));
	}

	protected TmaBpoCardinalityService getBpoCardinalityService()
	{
		return bpoCardinalityService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected TmaEntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected TmaAbstractOrderEntryService getAbstractOrderEntryService()
	{
		return abstractOrderEntryService;
	}
}
