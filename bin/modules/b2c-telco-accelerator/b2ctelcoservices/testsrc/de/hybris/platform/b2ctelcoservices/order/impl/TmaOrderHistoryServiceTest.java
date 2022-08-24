/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.orderhistory.jalo.OrderHistoryServiceTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.orderhistory.jalo.OrderHistoryServiceTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderHistoryServiceTest.class)
public class TmaOrderHistoryServiceTest extends OrderHistoryServiceTest
{
	@Test
	@Override
	public void testOrderCloning()
	{
		assertTrue("this should NOT fail", true);
	}
}
