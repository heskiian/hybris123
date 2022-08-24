/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.OrderInterceptorsIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.OrderInterceptorsIntegrationTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderInterceptorsIntegrationTest.class)
public class TmaOrderInterceptorsIntegrationTest extends OrderInterceptorsIntegrationTest
{
	@Test
	@Override
	public void testOrderLifeCycle()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testOrderCalculationStatusOnDiscountFlagsChange()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testOrderCalculationOnOrderDateChange()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testContractMembersCloning()
	{
		assertTrue("this should NOT fail", true);
	}
}
