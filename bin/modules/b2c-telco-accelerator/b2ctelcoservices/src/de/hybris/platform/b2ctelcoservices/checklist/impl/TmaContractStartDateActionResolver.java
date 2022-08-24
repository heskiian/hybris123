/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.Date;


/**
 * Implementation of {@link TmaChecklistActionResolver} to resolve {@link TmaChecklistActionType#CONTRACT_START_DATE} actions
 *
 * @since 2003
 */
public class TmaContractStartDateActionResolver extends DefaultTmaAbstractChecklistActionResolver
{
	private static final String INVALID_MESSAGE = "Invalid " +
			TmaChecklistActionType.CONTRACT_START_DATE + " set on the cart entry!";

	public TmaContractStartDateActionResolver(final ModelService modelService,
			final TmaCartValidationBuilder cartValidationBuilder)
	{
		super(modelService, cartValidationBuilder);
	}

	@Override
	protected boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry)
	{
		final Date contractStartDate = cartEntry.getContractStartDate();

		if (contractStartDate == null)
		{
			return true;
		}

		final Date today = Calendar.getInstance().getTime();

		return contractStartDate.compareTo(today) > 0;
	}

	@Override
	protected String getInvalidMessage()
	{
		return INVALID_MESSAGE;
	}
}
