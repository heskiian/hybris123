/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofulfillmentprocess.test;

import de.hybris.platform.b2ctelcofulfillmentprocess.actions.order.SendPaymentFailedNotificationAction;
import de.hybris.platform.b2ctelcofulfillmentprocess.service.TmaB2cOrderProcessFulfilmentService;
import de.hybris.platform.orderprocessing.events.FraudErrorEvent;
import de.hybris.platform.orderprocessing.events.PaymentFailedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 *
 */
public class SendPaymentFailedNotificationTest
{
	@Mock
	private TmaB2cOrderProcessFulfilmentService orderProcessFulfilmentService;

	@InjectMocks
	private final SendPaymentFailedNotificationAction sendPaymentFailedNotification = new SendPaymentFailedNotificationAction(
			orderProcessFulfilmentService);

	@Mock
	private EventService eventService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.b2ctelcofulfillmentprocess.actions.order.SendPaymentFailedNotificationAction#executeAction(OrderProcessModel)}
	 * .
	 */
	@Test
	public void testExecuteActionOrderProcessModel()
	{
		final OrderProcessModel process = new OrderProcessModel();
		sendPaymentFailedNotification.executeAction(process);

		Mockito.verify(eventService).publishEvent(Mockito.argThat(new BaseMatcher<FraudErrorEvent>()
		{

			@Override
			public boolean matches(final Object item)
			{
				if (item instanceof PaymentFailedEvent)
				{
					final PaymentFailedEvent event = (PaymentFailedEvent) item;
					if (event.getProcess().equals(process))
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{
				//nothing to do

			}
		}));
	}
}
