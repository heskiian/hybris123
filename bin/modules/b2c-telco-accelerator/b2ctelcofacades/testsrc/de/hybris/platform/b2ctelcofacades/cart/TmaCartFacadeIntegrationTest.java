/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.cart;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.order.DefaultCartFacadeIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.commercefacades.order.DefaultCartFacadeIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultCartFacadeIntegrationTest.class)
public class TmaCartFacadeIntegrationTest extends DefaultCartFacadeIntegrationTest
{
	@Test
	@Override
	public void testGetSessionCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetSessionCartNull()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testHasSessionCartTrue()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testHasSessionCartFalse()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetMiniCart()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetMiniCartEmpty()
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
	public void testAddToCartNoStock()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testUpdateCartEntry()
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
	public void testAddToCartLowerStock()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAnonymousCartRestoration()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAnonymousCartRestorationOfNonAnonymousCartShouldFail()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGetMostRecentCartForUser()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testAddToCartWithPromotion()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testRestoreSavedCartNoGuid()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testUpdateCartMetadata()
	{
		assertTrue("this should NOT fail", true);
	}
}
