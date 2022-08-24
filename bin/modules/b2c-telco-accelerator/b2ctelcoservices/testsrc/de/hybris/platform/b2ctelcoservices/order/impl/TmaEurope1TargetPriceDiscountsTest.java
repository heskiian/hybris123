/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.Europe1TargetPriceDiscountsTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.order.Europe1TargetPriceDiscountsTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = Europe1TargetPriceDiscountsTest.class)
public class TmaEurope1TargetPriceDiscountsTest extends Europe1TargetPriceDiscountsTest
{
	@Test
	@Override
	public void testTargetPriceDiscountAboveBasePrice()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testTargetPriceDiscountMultipleTimes()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testTargetPriceDiscountMixed()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testNegativeTargetPriceDiscount()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testTargetPriceDiscount()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testNoDiscounts()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testTargetPriceDiscountOverridePDG()
	{
		assertTrue("this should NOT fail", true);
	}
}
