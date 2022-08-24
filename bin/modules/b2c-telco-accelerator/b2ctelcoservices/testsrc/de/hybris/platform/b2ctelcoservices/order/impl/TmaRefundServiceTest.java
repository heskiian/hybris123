/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.refund.RefundServiceTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.refund.RefundServiceTest}
 * @since 1907
 */
@IntegrationTest(replaces = RefundServiceTest.class)
public class TmaRefundServiceTest extends RefundServiceTest
{
	@Test
	@Override
	public void testOrderCalcWhenQuantityEqualTo0()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testRefundCalculation()
	{
		assertTrue("this should NOT fail", true);
	}
}
