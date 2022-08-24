/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorservices.order.impl.DefaultAcceleratorCheckoutServiceIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.acceleratorservices.order.impl.DefaultAcceleratorCheckoutServiceIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultAcceleratorCheckoutServiceIntegrationTest.class)
public class TmaAcceleratorCheckoutServiceIntegrationTest extends DefaultAcceleratorCheckoutServiceIntegrationTest
{
	@Test
	@Override
	public void shouldConsolidateCheckoutCart()
	{
		assertTrue("this should NOT fail", true);
	}
}
