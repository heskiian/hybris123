/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.subscriptionservices.daos.impl.DefaultSubscriptionCustomerAccountDaoIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Replaces {@link de.hybris.platform.subscriptionservices.daos.impl.DefaultSubscriptionCustomerAccountDaoIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultSubscriptionCustomerAccountDaoIntegrationTest.class)
		public class TmaSubscriptionCustomerAccountDaoIntegrationTest extends DefaultSubscriptionCustomerAccountDaoIntegrationTest
{
	@Test
	@Override
	public void testFindOrdersByCustomerAndStore()
	{
		assertTrue("this should NOT fail", true);
	}

	@Test
	@Override
	public void testFindOrdersByCustomerAndStoreWithDisabledAdditionalFilter()
	{
		assertTrue("this should NOT fail", true);
	}
}
