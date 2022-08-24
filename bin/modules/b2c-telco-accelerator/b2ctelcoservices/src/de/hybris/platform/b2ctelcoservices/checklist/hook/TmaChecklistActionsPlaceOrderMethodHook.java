/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.hook;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistService;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart validation hook to verify, for each cart entry, if required checklist actions are fulfilled
 *
 * @since 1911
 */
public class TmaChecklistActionsPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	private TmaChecklistService checklistService;

	public TmaChecklistActionsPlaceOrderMethodHook(TmaChecklistService checklistService)
	{
		this.checklistService = checklistService;
	}

	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		boolean areActionsFulfilled = getChecklistService().areActionsFulfilled(parameter.getCart());
		if (!areActionsFulfilled)
		{
			throw new InvalidCartException(
					createChecklistValidationMessage(getValidationsFromCart(parameter.getCart().getEntries())).toString());
		}
	}

	private List<TmaCartValidationModel> getValidationsFromCart(final List<AbstractOrderEntryModel> entries)
	{
		final List<TmaCartValidationModel> tmaCartValidationModels = new ArrayList<>();

		entries.forEach(entry -> {
			if (CollectionUtils.isNotEmpty(entry.getChildEntries()))
			{
				tmaCartValidationModels.addAll(getValidationsFromCart((List<AbstractOrderEntryModel>) entry.getChildEntries()));
			}
			Set<TmaCartValidationModel> cartEntryValidations = ((CartEntryModel) entry).getCartEntryValidations();
			tmaCartValidationModels.addAll(cartEntryValidations);
		});

		return tmaCartValidationModels;
	}

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel)
	{
		//nothing to do
	}

	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
	{
		//nothing to do
	}

	private StringBuilder createChecklistValidationMessage(final List<TmaCartValidationModel> tmaCartValidationModels)
	{
		final StringBuilder message = new StringBuilder("Checklist actions are not fullfilled: ");

		tmaCartValidationModels.forEach(
				validationModel -> message.append("Cart entry ").append(validationModel.getCartEntry().getEntryNumber())
						.append(": ").append(validationModel.getMessage()).append(". "));

		return message;
	}

	protected TmaChecklistService getChecklistService()
	{
		return checklistService;
	}

}
