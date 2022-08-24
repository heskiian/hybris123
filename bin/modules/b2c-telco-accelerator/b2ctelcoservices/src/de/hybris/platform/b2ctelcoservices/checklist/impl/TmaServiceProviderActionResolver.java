/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;




/**
 * Implementation of {@link TmaChecklistActionResolver} to resolve
 * {@link TmaChecklistActionType#SWITCH_SERVICE_PROVIDER}
 * actions
 *
 * @since 2003
 */
public class TmaServiceProviderActionResolver extends DefaultTmaAbstractChecklistActionResolver
{
	private static final String INVALID_MESSAGE = "Empty " +
			TmaChecklistActionType.SERVICE_PROVIDER + " set on the cart entry!";

	public TmaServiceProviderActionResolver(final ModelService modelService, final TmaCartValidationBuilder cartValidationBuilder)
	{
		super(modelService, cartValidationBuilder);
	}

	@Override
	protected boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry)
	{
		if (!ObjectUtils.isEmpty(cartEntry.getProcessType()) && StringUtils.equalsIgnoreCase(cartEntry.getProcessType().getCode(),
				TmaProcessType.SWITCH_SERVICE_PROVIDER.getCode()))
		{
			return StringUtils.isNotBlank(cartEntry.getServiceProvider());
		}
		return true;
	}

	@Override
	protected String getInvalidMessage()
	{
		return INVALID_MESSAGE;
	}
}
