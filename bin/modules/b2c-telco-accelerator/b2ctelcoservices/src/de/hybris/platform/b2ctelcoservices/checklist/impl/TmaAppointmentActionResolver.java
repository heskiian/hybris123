/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Implementation of {@link TmaChecklistActionResolver} to resolve {@link TmaChecklistActionType#APPOINTMENT} actions
 *
 * @since 1911
 */
public class TmaAppointmentActionResolver extends DefaultTmaAbstractChecklistActionResolver
{
	private static final String INVALID_MESSAGE = TmaChecklistActionType.APPOINTMENT + " is not set on the cart entry!";

	public TmaAppointmentActionResolver(ModelService modelService, TmaCartValidationBuilder cartValidationBuilder)
	{
		super(modelService, cartValidationBuilder);
	}

	@Override
	protected boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry)
	{
		return cartEntry.getAppointmentReference() != null;
	}

	@Override
	protected String getInvalidMessage()
	{
		return INVALID_MESSAGE;
	}

}
