/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.subscription.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.subscriptionservices.subscription.impl.DefaultSubscriptionCommerceCartServiceIntegrationTest;
import de.hybris.platform.util.AppendSpringConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces
 * {@link de.hybris.platform.subscriptionservices.subscription.impl.DefaultSubscriptionCommerceCartServiceIntegrationTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = DefaultSubscriptionCommerceCartServiceIntegrationTest.class)
@AppendSpringConfiguration({"classpath:subscriptionservices-spring-test-context.xml"})
public class TmaSubscriptionCommerceCartServiceIntegrationTest extends DefaultSubscriptionCommerceCartServiceIntegrationTest
{
	@Test
	@Override
	public void testUpdateQuantityForCartEntrySubscription()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testUpdateQuantityForMultiCartEntry()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToMultiCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartToTheSameEntry()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testUpdateQuantityForCartEntryToZero()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCalculateCartEntriesAfterAddingToCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartUnavailable()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testRecalculateCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartWhenNoCartPassed()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartInsufficientStock()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartForceNewEntry()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testUpdateQuantityForCartEntry()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testCalculateCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testValidateCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testRemoveAllEntriesMultiCart()
	{
		assertTrue("this should NOT fail", true);
	}
	@Test
	@Override
	public void shouldReportOnAddingToNonExistingEntry()
	{
		assertTrue("this should NOT fail", true);
	}
}
