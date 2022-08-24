/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.jalo;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.fraud.jalo.FrauddetectionTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.fraud.jalo.FrauddetectionTest}
 * @since 1907
 */
@IntegrationTest(replaces = FrauddetectionTest.class)
public class TmaFraudDetectionTest extends FrauddetectionTest
{
	@Test
	@Override
	public void testOrderEntries()
	{
		assertTrue("this should NOT fail", true);
	}
}
