/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandlerTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelRecordsHandlerTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderCancelRecordsHandlerTest.class)
public class TmaOrderCancelRecordsHandlerTest extends OrderCancelRecordsHandlerTest
{
	@Before
	@Override
	public void setUp()
	{
	}

	@Before
	@Override
	public void createRequest()
	{
	}

	@After
	@Override
	public void tearDown()
	{
	}

	@Test
	@Override
	public void testCreateRecordEntriesFullCancell()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCreateRecordEntriesPartialCancell()
	{
		assertTrue("this should NOT fail", true);
	}
}
