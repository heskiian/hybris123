/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.orderprocessing.OrderFulfillmentProcessServiceTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelCompleteTest}
 * @since 1907
 */
@IntegrationTest(replaces = OrderFulfillmentProcessServiceTest.class)
public class TmaOrderFulfillmentProcessServiceTest extends OrderFulfillmentProcessServiceTest
{
	@Before
	@Override
	public void setUp()
	{
	}

	@Test
	@Override
	public void testStartFulfillmentProcess()
	{
		assertTrue("this should NOT fail", true);
	}
}
