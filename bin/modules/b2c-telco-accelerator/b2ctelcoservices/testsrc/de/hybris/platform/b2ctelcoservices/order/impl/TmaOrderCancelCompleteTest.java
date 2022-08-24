/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelCompleteTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.ordercancel.OrderCancelCompleteTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderCancelCompleteTest.class)
public class TmaOrderCancelCompleteTest extends OrderCancelCompleteTest
{
	@Override
	protected OrderModel placeTestOrder()
	{
		return new OrderModel();
	}

	@Override
	public void customSetUp()
	{
	}

	@Test
	@Override
	public void testWarehouseResponseCancelDenied()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancelInHoldingArea()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCompleteCancelWarehouseNotShipping()
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
	public void testSecondCancelRequest()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testExceptionOnPartialCancelImpossible()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testExceptionOnCompleteCancelImpossible()
	{
		assertTrue("this should NOT fail", true);
	}
}
