/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.checkout;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorfacades.order.checkout.AnonymousCheckoutIntegrationTest;

import org.junit.Before;


/**
 * Replaces {@link de.hybris.platform.acceleratorfacades.order.checkout.AnonymousCheckoutIntegrationTest}
 *
 * @since 2003
 *
 */

@IntegrationTest(replaces = AnonymousCheckoutIntegrationTest.class)
public class TmaAnonymousCheckoutIntegrationTest extends AnonymousCheckoutIntegrationTest
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();

		importCsv("/b2ctelcofacades/test/testPolicy.impex", "utf-8");
	}
}
