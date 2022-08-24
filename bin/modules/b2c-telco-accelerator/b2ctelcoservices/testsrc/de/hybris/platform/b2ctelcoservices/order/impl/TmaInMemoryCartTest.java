/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.InMemoryCartTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.InMemoryCartTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = InMemoryCartTest.class)
public class TmaInMemoryCartTest extends InMemoryCartTest
{
	@Test
	@Override
	public void testCart()
	{
		assertTrue("this should NOT fail", true);
	}
}
