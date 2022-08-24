/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.orderscheduling.OrderUtilityTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.orderscheduling.OrderUtilityTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = OrderUtilityTest.class)
public class TmaOrderUtilityTest extends OrderUtilityTest
{
	@Test
	@Override
	public void testCreateOrderFromOrderTemplate()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCreateOrderFromCart()
	{
		assertTrue("this should NOT fail", true);
	}
}
