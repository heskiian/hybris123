/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.actions.order;

import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.service.TmaB2cOrderProcessFulfilmentService;
import de.hybris.platform.orderprocessing.events.PaymentFailedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SendPaymentFailedNotificationAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(SendPaymentFailedNotificationAction.class);

	private EventService eventService;
	private TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService;

	public SendPaymentFailedNotificationAction(final TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService)
	{
		this.orderProcessFulfilmentService = orderProcessFulfilmentService;
	}

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Process: " + process.getCode() + " in step " + getClass());
		}
		getEventService().publishEvent(new PaymentFailedEvent(process));

		getOrderProcessFulfilmentService().triggerB2cOrderProcessEndEvent(process);
		LOG.debug(String.format("Process %s fired event %s with %s status", process.getCode(),
				B2ctelcofulfillmentprocessConstants.B2C_ORDER_PROCESS_END_EVENT_NAME, ProcessState.FAILED));
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected TmaB2cOrderProcessFulfilmentService getOrderProcessFulfilmentService()
	{
		return orderProcessFulfilmentService;
	}
}
