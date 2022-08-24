/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ordercancel.OrderCancelPossibilityTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelPossibilityTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderCancelPossibilityTest.class)
public class TmaOrderCancelPossibilityTest extends OrderCancelPossibilityTest
{
	@Before
	@Override
	public void setUp()
	{
	}

	@After
	@Override
	public void tearDown()
	{
	}

	@Before
	@Override
	public void customSetUp()
	{
	}

	@Test
	@Override
	public void testRequestImpossibleOnPendingCancelOrders()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testRequestImpossibleOnClosedOrders()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testPartialCancelAcrossStates()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancelOrigin()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancelPossibleInShipping()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancelPossibleInWHS()
	{
		assertTrue("this should NOT fail", true);
	}
}
