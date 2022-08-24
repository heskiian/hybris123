/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.hook;

import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.commerceservices.order.hook.CommerceCloneSavedCartMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceSaveCartResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;
import java.util.Map;


/**
 * Hook for cardinality specific validation when cloning a saved cart
 *
 * @since 2011
 */
public class TmaBpoCardinalityCloneSavedCartMethodHook implements CommerceCloneSavedCartMethodHook
{
	private TmaBpoCardinalityService bpoCardinalityService;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService entryGroupService;

	public TmaBpoCardinalityCloneSavedCartMethodHook(final TmaBpoCardinalityService bpoCardinalityService,
			final TmaEntryGroupService entryGroupService)
	{
		this.bpoCardinalityService = bpoCardinalityService;
		this.entryGroupService = entryGroupService;
	}

	@Override
	public void beforeCloneSavedCart(final CommerceSaveCartParameter parameters)
	{
		// no implementation needed
	}

	@Override
	public void afterCloneSavedCart(final CommerceSaveCartParameter parameter, final CommerceSaveCartResult saveCartResult)
	{
		if (saveCartResult.getSavedCart() == null)
		{
			return;
		}

		final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap = getBpoCardinalityService()
				.verifyCardinalityForOrder(saveCartResult.getSavedCart());

		for (Map.Entry<AbstractOrderEntryModel, List<String>> mapEntry : validationMessagesMap.entrySet())
		{
			getBpoCardinalityService().updateCardinalityValidationMessages(mapEntry.getKey(), mapEntry.getValue());
		}
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
}
