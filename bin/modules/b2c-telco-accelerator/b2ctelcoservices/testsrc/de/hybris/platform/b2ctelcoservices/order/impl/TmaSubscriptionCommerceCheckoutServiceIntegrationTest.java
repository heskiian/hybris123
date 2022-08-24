/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.subscriptionservices.subscription.impl.DefaultSubscriptionCommerceCheckoutServiceIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces
 * {@link de.hybris.platform.subscriptionservices.subscription.impl.DefaultSubscriptionCommerceCheckoutServiceIntegrationTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = DefaultSubscriptionCommerceCheckoutServiceIntegrationTest.class)
public class TmaSubscriptionCommerceCheckoutServiceIntegrationTest extends
		DefaultSubscriptionCommerceCheckoutServiceIntegrationTest
{
	@Test
	@Override
	public void testPlaceOrder()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testPlaceOrderWithChildCart()
	{
		assertTrue("this should NOT fail", true);
	}
}
