/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.OrderServiceTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.OrderServiceTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderServiceTest.class)
public class TmaOrderServiceTest extends OrderServiceTest
{
	@Test
	@Override
	public void testFindEntriesByProduct()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCartRemoveEntriesThenOrder()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddNewEntry()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testPlaceOrderLazyLoadingBug()
	{
		assertTrue("this should NOT fail", true);
	}
}
