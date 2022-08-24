/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.actions.order;

import de.hybris.platform.b2ctelcofulfillmentprocess.CheckOrderService;
import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.service.TmaB2cOrderProcessFulfilmentService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.enums.ProcessState;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This example action checks the order for required data in the business process. Skipping this action may result in
 * failure in one of the subsequent steps of the process. The relation between the order and the business process is
 * defined in basecommerce extension through item OrderProcess. Therefore if your business process has to access the
 * order (a typical case), it is recommended to use the OrderProcess as a parentClass instead of the plain
 * BusinessProcess.
 */
public class CheckOrderAction extends AbstractSimpleDecisionAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(CheckOrderAction.class);

	private CheckOrderService checkOrderService;
	private TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService;

	public CheckOrderAction(final TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService)
	{
		this.orderProcessFulfilmentService = orderProcessFulfilmentService;
	}

	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (order == null)
		{
			LOG.error("Missing the order, exiting the process");

			getOrderProcessFulfilmentService().triggerB2cOrderProcessEndEvent(process);
			LOG.debug(String.format("Process %s fired event %s with %s status", process.getCode(),
					B2ctelcofulfillmentprocessConstants.B2C_ORDER_PROCESS_END_EVENT_NAME, ProcessState.ERROR));

			return Transition.NOK;
		}

		if (getCheckOrderService().check(order))
		{
			setOrderStatus(order, OrderStatus.CHECKED_VALID);
			return Transition.OK;
		}

		setOrderStatus(order, OrderStatus.CHECKED_INVALID);

		getOrderProcessFulfilmentService().triggerB2cOrderProcessEndEvent(process);
		LOG.debug(String.format("Process %s fired event %s with %s status", process.getCode(),
				B2ctelcofulfillmentprocessConstants.B2C_ORDER_PROCESS_END_EVENT_NAME, ProcessState.ERROR));

		return Transition.NOK;
	}

	protected CheckOrderService getCheckOrderService()
	{
		return checkOrderService;
	}

	@Required
	public void setCheckOrderService(final CheckOrderService checkOrderService)
	{
		this.checkOrderService = checkOrderService;
	}

	protected TmaB2cOrderProcessFulfilmentService getOrderProcessFulfilmentService()
	{
		return orderProcessFulfilmentService;
	}
}
