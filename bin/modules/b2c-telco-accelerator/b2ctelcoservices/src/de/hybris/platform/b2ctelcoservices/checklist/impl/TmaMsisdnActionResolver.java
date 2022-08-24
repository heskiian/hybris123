/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.b2ctelcoservices.model.TmaConfigurablePscvPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;


/**
 * Implementation of {@link TmaChecklistActionResolver} to resolve {@link TmaChecklistActionType#MSISDN} actions
 *
 * @since 1911
 */
public class TmaMsisdnActionResolver extends DefaultTmaAbstractChecklistActionResolver
{
	private static final String INVALID_MESSAGE = TmaChecklistActionType.MSISDN + " is not configured on the cart entry!";

	public TmaMsisdnActionResolver(ModelService modelService, TmaCartValidationBuilder cartValidationBuilder)
	{
		super(modelService, cartValidationBuilder);
	}

	@Override
	protected boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry)
	{
		final Set<TmaAbstractOrderEntryPscvModel> entryPscvSet = cartEntry.getProductSpecCharacteristicValues();
		final TmaPolicyStatementModel statement = action.getStatement();
		if (!(statement instanceof TmaConfigurablePscvPolicyStatementModel))
		{
			return false;
		}

		return entryPscvSet.stream()
				.anyMatch(entryPscv -> entryPscv.getName().equalsIgnoreCase(((TmaConfigurablePscvPolicyStatementModel) statement)
						.getProductSpecCharacteristic().getName()));
	}

	@Override
	protected String getInvalidMessage()
	{
		return INVALID_MESSAGE;
	}
}
