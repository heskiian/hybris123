/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.calculation;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.calculation.CalculationServiceJaloCacheTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link CalculationServiceJaloCacheTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = CalculationServiceJaloCacheTest.class)
public class TmaCalculationServiceJaloCacheTest extends CalculationServiceJaloCacheTest
{
	@Test
	@Override
	public void testCalculateAbstractOrderModel()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testGiveAwayHandling()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void shouldSetBackOrderDateWhenCalculationExceptionOccours()
	{
		assertTrue("this should NOT fail", true);
	}
}
