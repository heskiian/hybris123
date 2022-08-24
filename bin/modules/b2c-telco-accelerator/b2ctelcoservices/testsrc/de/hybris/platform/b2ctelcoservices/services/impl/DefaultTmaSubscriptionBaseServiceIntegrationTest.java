/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.Set;

import javax.annotation.Resource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionBaseServiceIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String NEW_SUBSCRIBER_IDENTITY = "12345678";

	private static Set<TmaSubscriptionBaseModel> tmaSubscriptionBaseModels;

	@Resource
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;

	@Test
	public void testCreateSubscriptionBase()
	{
		final TmaSubscriptionBaseModel subscriptionBase = tmaSubscriptionBaseService.createSubscriptionBase(NEW_SUBSCRIBER_IDENTITY,
				BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
		assertEquals(NEW_SUBSCRIBER_IDENTITY, subscriptionBase.getSubscriberIdentity());
		assertEquals(BILLING_SYSTEM_ID, subscriptionBase.getBillingSystemId());
		assertEquals(0, subscriptionBase.getSubscribedProducts().size());
		assertEquals(0, subscriptionBase.getSubscriptionAccesses().size());
	}

	@Test(expected = ModelNotFoundException.class)
	public void removeSubscriptionBase()
	{
		tmaSubscriptionBaseService.deleteSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		tmaSubscriptionBaseService.getSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
	}

	@Test(expected = ModelSavingException.class)
	public void testSubscriptionBaseCannotBeCreatedWithExistingIds()
	{
		tmaSubscriptionBaseService.createSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteSubscriptionBase()
	{
		final TmaSubscriptionBaseModel existingSubscriptionBase = findSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertNotNull(existingSubscriptionBase.getSubscriberIdentity());
		tmaSubscriptionBaseService.deleteSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		findSubscriptionBase(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteNonExistingSubscriptionBase()
	{
		tmaSubscriptionBaseService.deleteSubscriptionBase(NON_EXISTENT_ID, BILLING_SYSTEM_ID);
	}

	@Test
	public void generateSubscriptionBase()
	{
		final TmaSubscriptionBaseModel tmaSubscriptionBaseModel = tmaSubscriptionBaseService
				.generateSubscriptionBase(BILLING_ACCOUNT_ID);
		assertEquals(BILLING_SYSTEM_ID, tmaSubscriptionBaseModel.getBillingSystemId());
	}

	@Test
	public void testGetAllSubscriptionBases()
	{
		tmaSubscriptionBaseModels = tmaSubscriptionBaseService.getAllSubscriptionBases();
		assertNotNull(tmaSubscriptionBaseModels);
		Assert.assertEquals(BILLING_SYSTEM_ID, tmaSubscriptionBaseModels.iterator().next().getBillingSystemId());
		assertThat(SUBSCRIPTION_BASES,
				Matchers.hasItem(Matchers.containsString(tmaSubscriptionBaseModels.iterator().next().getSubscriberIdentity())));
	}

	private TmaSubscriptionBaseModel findSubscriptionBase(final String subscriberIdentity, final String billingSystemId)
	{
		return tmaSubscriptionBaseService.getSubscriptionBase(subscriberIdentity, billingSystemId);
	}
}
