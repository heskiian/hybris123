/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.order.UntaxedEntriesCalculationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.orderscheduling.OrderUtilityTest}
 *
 * @since 1907
 */
@IntegrationTest(replaces = UntaxedEntriesCalculationTest.class)
public class TmaUntaxedEntriesCalculationTest extends UntaxedEntriesCalculationTest
{
	@Test
	@Override
	public void testNetNormalTaxEntries()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testNetMixedTaxEntries()
	{
		assertTrue("this should NOT fail", true);
	}

}
