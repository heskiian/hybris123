/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.BasePriceWithUnitConversionTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.BasePriceWithUnitConversionTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = BasePriceWithUnitConversionTest.class)
public class TmaBasePriceWithUnitConversionTest extends BasePriceWithUnitConversionTest
{
	@Test
	@Override
	public void preferredPricePerUnitTest()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void basePriceWithUnitConversion()
	{
		assertTrue("this should NOT fail", true);
	}
}
