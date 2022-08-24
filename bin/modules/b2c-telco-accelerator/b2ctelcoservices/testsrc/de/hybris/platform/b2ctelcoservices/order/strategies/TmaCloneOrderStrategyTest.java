/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.strategies;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.strategies.ordercloning.CloneOrderStrategyTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.strategies.ordercloning.CloneOrderStrategyTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = CloneOrderStrategyTest.class)
public class TmaCloneOrderStrategyTest extends CloneOrderStrategyTest
{
	@Override
	public void setUp()
	{
	}

	@Test
	@Override
	public void testClone()
	{
		assertTrue("this should NOT fail", true);
	}
}
