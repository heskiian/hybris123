/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofulfillmentprocess.service.impl;

import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.service.TmaB2cOrderProcessFulfilmentService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;


/**
 * Default implementation of {@link TmaB2cOrderProcessFulfilmentService}.
 *
 * @since 2105
 */
public class DefaultTmaB2cOrderProcessFulfilmentService implements TmaB2cOrderProcessFulfilmentService
{
	private static final String SEPARATOR = "_";

	private BusinessProcessService businessProcessService;

	public DefaultTmaB2cOrderProcessFulfilmentService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	public void triggerB2cOrderProcessEndEvent(final OrderProcessModel process)
	{
		if (process.getParentProcess() == null)
		{
			return;
		}

		getBusinessProcessService().triggerEvent(process.getParentProcess().getCode() + SEPARATOR
				+ B2ctelcofulfillmentprocessConstants.B2C_ORDER_PROCESS_END_EVENT_NAME);
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}
}
