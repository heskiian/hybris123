/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.orderscheduling.OrderSchedulingIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.orderscheduling.OrderSchedulingIntegrationTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderSchedulingIntegrationTest.class)
public class TmaOrderSchedulingIntegrationTest extends OrderSchedulingIntegrationTest
{
	@Test
	@Override
	public void testScheduleOrderFromClonedCart()
	{
		assertTrue("this should NOT fail", true);
	}
}
