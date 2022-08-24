/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.actions.order;

import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcofulfillmentprocess.MockCreateCpiService;
import de.hybris.platform.b2ctelcofulfillmentprocess.constants.B2ctelcofulfillmentprocessConstants;
import de.hybris.platform.b2ctelcofulfillmentprocess.service.TmaB2cOrderProcessFulfilmentService;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.events.OrderCompletedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link MockCreateSubscriptionFromOrderAction}.
 *
 * @since 18.05
 */

public class MockCreateSubscriptionFromOrderAction extends AbstractProceduralAction<OrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(MockCreateSubscriptionFromOrderAction.class);

	private static final String CREATE_SUBSCRIPTION_ON_CHECKOUT_PROPERTY = "checkout.create.subscription";

	private EventService eventService;
	private CheckoutFacade checkoutFacade;
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;
	private MockCreateCpiService mockCreateCpiService;
	private TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService;

	public MockCreateSubscriptionFromOrderAction(final TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService)
	{
		this.orderProcessFulfilmentService = orderProcessFulfilmentService;
	}

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		getEventService().publishEvent(new OrderCompletedEvent(process));

		getOrderProcessFulfilmentService().triggerB2cOrderProcessEndEvent(process);
		LOG.debug(String.format("Process %s fired event %s with %s status", process.getCode(),
				B2ctelcofulfillmentprocessConstants.B2C_ORDER_PROCESS_END_EVENT_NAME, ProcessState.SUCCEEDED));

		if (isCreateSubscriptionFlow())
		{
			final OrderModel orderModel = process.getOrder();
			LOG.info("Process: " + process.getCode() + " in step " + getClass());
			getMockCreateCpiService().mockCreateSubscriptionsFromOrder(orderModel);
		}
	}

	/**
	 * @return checkout.create.subscription from application.properties file.
	 */
	public boolean isCreateSubscriptionFlow()
	{
		return Config.getBoolean(CREATE_SUBSCRIPTION_ON_CHECKOUT_PROPERTY, true);
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

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	@Required
	public void setCheckoutFacade(final CheckoutFacade checkoutFacade)
	{
		this.checkoutFacade = checkoutFacade;
	}


	protected TmaSubscribedProductFacade getTmaSubscribedProductFacade()
	{
		return tmaSubscribedProductFacade;
	}

	@Required
	public void setTmaSubscribedProductFacade(final TmaSubscribedProductFacade tmaSubscribedProductFacade)
	{
		this.tmaSubscribedProductFacade = tmaSubscribedProductFacade;
	}

	@Required
	public MockCreateCpiService getMockCreateCpiService()
	{
		return mockCreateCpiService;
	}

	public void setMockCreateCpiService(MockCreateCpiService mockCreateCpiService)
	{
		this.mockCreateCpiService = mockCreateCpiService;
	}

	protected TmaB2cOrderProcessFulfilmentService getOrderProcessFulfilmentService()
	{
		return orderProcessFulfilmentService;
	}
}
