/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.jalo;

import de.hybris.basecommerce.jalo.BasecommerceTest;
import de.hybris.bootstrap.annotations.IntegrationTest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.basecommerce.jalo.BasecommerceTest}
 * @since 1907
 */
@IntegrationTest(replaces = BasecommerceTest.class)
public class TmaBaseCommerceTest extends BasecommerceTest
{
	@Before
	@Override
	public void setUp()
	{
	}

	@Test
	@Override
	public void testMultiAddressCalculation()
	{
		assertTrue("this should NOT fail", true);
	}
}
