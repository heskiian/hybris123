/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionBaseDao;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Set;

import javax.annotation.Resource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionBaseDaoIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	@Resource
	private TmaSubscriptionBaseDao tmaSubscriptionBaseDao;
	private TmaSubscriptionBaseModel subscriptionBaseModel;
	private Set<TmaSubscriptionBaseModel> subscriptionBaseModels;

	@Test
	public void testFindSubscriptionBase()
	{
		whenSubscriptionBaseIsFetched(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		thenSubscriptionBaseExists();
	}

	@Test
	public void testfindAllSubscriptionBases()
	{
		findAllAvailableSubscriptionBases();
		thenAllSubscriptionBasesAreReturned();
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionBaseForWrongSubscriberIdentity()
	{
		whenSubscriptionBaseIsFetched(NON_EXISTENT_ID, BILLING_SYSTEM_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionBaseForWrongBillingSystem()
	{
		whenSubscriptionBaseIsFetched(SUBSCRIBER_IDENTITY, NON_EXISTENT_ID);
	}

	@Test
	public void testFindSubscriptionBaseByIdentity()
	{
		subscriptionBaseModel = tmaSubscriptionBaseDao.findSubscriptionBaseByIdentity(SUBSCRIBER_IDENTITY);
		assertEquals(SUBSCRIBER_IDENTITY, subscriptionBaseModel.getSubscriberIdentity());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionBaseNull()
	{
		subscriptionBaseModel = tmaSubscriptionBaseDao.findSubscriptionBaseByIdentity(SUBSCRIBER_IDENTITY_TEST);
		assertEquals(SUBSCRIBER_IDENTITY, subscriptionBaseModel.getSubscriberIdentity());
	}

	private void whenSubscriptionBaseIsFetched(final String subscriberIdentity, final String billingSystemId)
	{
		subscriptionBaseModel = tmaSubscriptionBaseDao.findSubscriptionBase(subscriberIdentity, billingSystemId);
	}

	private void findAllAvailableSubscriptionBases()
	{
		subscriptionBaseModels = tmaSubscriptionBaseDao.findAllSubscriptionBases();
	}

	private void thenSubscriptionBaseExists()
	{
		Assert.assertNotNull(subscriptionBaseModel);
		Assert.assertEquals(subscriptionBaseModel.getSubscriberIdentity(), SUBSCRIBER_IDENTITY);
		Assert.assertEquals(subscriptionBaseModel.getBillingSystemId(), BILLING_SYSTEM_ID);
	}

	private void thenAllSubscriptionBasesAreReturned()
	{
		Assert.assertNotNull(subscriptionBaseModels);
		Assert.assertEquals(BILLING_SYSTEM_ID, subscriptionBaseModels.iterator().next().getBillingSystemId());
		assertThat(SUBSCRIPTION_BASES,
				Matchers.hasItem(Matchers.containsString(subscriptionBaseModels.iterator().next().getSubscriberIdentity())));
	}
}
