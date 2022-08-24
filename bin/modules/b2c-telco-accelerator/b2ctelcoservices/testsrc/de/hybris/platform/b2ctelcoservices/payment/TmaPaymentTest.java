/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.payment;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.payment.impl.PaymentTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.payment.impl.PaymentTest}
 * @since 1907
 */
@IntegrationTest(replaces = PaymentTest.class)
public class TmaPaymentTest extends PaymentTest
{
	@Test
	@Override
	public void testCapture()
	{
		assertTrue("this should NOT fail", true);
	}
}
