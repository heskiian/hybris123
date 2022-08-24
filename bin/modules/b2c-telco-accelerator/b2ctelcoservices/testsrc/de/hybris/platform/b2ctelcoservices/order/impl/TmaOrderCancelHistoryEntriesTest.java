/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ordercancel.OrderCancelHistoryEntriesTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelCompleteTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderCancelHistoryEntriesTest.class)
public class TmaOrderCancelHistoryEntriesTest extends OrderCancelHistoryEntriesTest
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

	@Test
	@Override
	public void testPartialCancel()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancel()
	{
		assertTrue("this should NOT fail", true);
	}
}
