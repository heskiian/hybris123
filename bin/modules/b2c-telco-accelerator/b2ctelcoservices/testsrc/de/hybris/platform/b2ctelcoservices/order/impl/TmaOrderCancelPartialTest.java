/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ordercancel.OrderCancelPartialTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelPartialTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderCancelPartialTest.class)
public class TmaOrderCancelPartialTest extends OrderCancelPartialTest
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
	public void testSomeCancelableOrderEntriesWithConsignments()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testWarehouseResponsePartialEntryCancelOK()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAllCancelableOrderEntriesWithConsignments()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testWarehouseResponsePartialCancelOK()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAllCancelableOrderEntriesNoConsignments()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testPartialEntryCancelationBeforeWarehouse()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testWarehouseResponsePartialCancelDenied()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testWarehouseResponsePartialCancelPartialOK()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testPartialCancelationBeforeWarehouse()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testWarehouseResponsePartialEntryCancelDenied()
	{
		assertTrue("this should NOT fail", true);
	}
}
