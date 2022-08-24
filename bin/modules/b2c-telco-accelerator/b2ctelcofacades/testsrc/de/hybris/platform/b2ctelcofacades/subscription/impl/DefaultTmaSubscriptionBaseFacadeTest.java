/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.subscription.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionBaseFacadeTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String IDENTITY_TO_BE_CREATED = "IDENTITY_TO_BE_CREATED";

	@Resource
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;

	@Resource
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;


	@Test
	public void createSubscriptionBase()
	{
		tmaSubscriptionBaseFacade.createSubscriptionBase(IDENTITY_TO_BE_CREATED, BILLING_SYSTEM_ID, BILLING_ACCOUNT_ID);
		Assert.assertEquals(IDENTITY_TO_BE_CREATED,
				tmaSubscriptionBaseService.getSubscriptionBase(IDENTITY_TO_BE_CREATED, BILLING_SYSTEM_ID).getSubscriberIdentity());
		Assert.assertEquals(IDENTITY_TO_BE_CREATED, tmaSubscriptionBaseFacade
				.getSubscriptionBaseForSubscriberIdentity(IDENTITY_TO_BE_CREATED, BILLING_SYSTEM_ID).getSubscriberIdentity());

	}

	@Test(expected = ModelNotFoundException.class)
	public void getSubscriptionBaseForSubscriberIdentityNotFoundWhenBillingSystemIDEmpty()
	{
		tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity(IDENTITY_TO_BE_CREATED, "");
	}

	@Test(expected = ModelNotFoundException.class)
	public void getSubscriptionBaseForSubscriberIdentityNotFoundWhenSubscriberIdentityEmpty()
	{
		tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity("", BILLING_SYSTEM_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void getSubscriptionBaseForSubscriberIdentityNotFoundWhenBothEmpty()
	{
		tmaSubscriptionBaseFacade.getSubscriptionBaseForSubscriberIdentity("", "");
	}

	@Test(expected = ModelNotFoundException.class)
	public void deleteSubscriptionBase()
	{
		tmaSubscriptionBaseFacade.deleteSubscriptionBase(BILLING_SUBSCRIPTION_ID, BILLING_SYSTEM_ID);
		tmaSubscriptionBaseService.getSubscriptionBase(BILLING_SUBSCRIPTION_ID, BILLING_SYSTEM_ID);
	}

	@Test
	public void generateSubscriptionBase()
	{
		final TmaSubscriptionBaseData tmaSubscriptionBaseData = tmaSubscriptionBaseFacade
				.generateSubscriptionBase(BILLING_ACCOUNT_ID);
		Assert.assertEquals(BILLING_SYSTEM_ID, tmaSubscriptionBaseData.getBillingSystemId());
	}

	@Test
	public void testGetAllSubscriptionBases()
	{
		final Set<TmaSubscriptionBaseData> tmaSubscriptionBases = tmaSubscriptionBaseFacade.getAllSubscriptionBases();
		assertNotNull(tmaSubscriptionBases);
		Assert.assertEquals(BILLING_SYSTEM_ID, tmaSubscriptionBases.iterator().next().getBillingSystemId());
		assertThat(SUBSCRIPTION_BASES,
				Matchers.hasItem(Matchers.containsString(tmaSubscriptionBases.iterator().next().getSubscriberIdentity())));
	}

	@Test
	public void testGetSubscriptionBaseBySubscriberIdentity()
	{
		final TmaSubscriptionBaseData subscriptionBases = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY);
		assertNotNull(subscriptionBases);
		Assert.assertEquals(SUBSCRIBER_IDENTITY, subscriptionBases.getSubscriberIdentity());
		Assert.assertEquals(BILLING_SYSTEM_ID, subscriptionBases.getBillingSystemId());

	}

	@Test(expected = ModelNotFoundException.class)
	public void getSubscriptionBaseForInvalidSubscriberIdentity()
	{
		tmaSubscriptionBaseFacade.getSubscriptionBaseBySubscriberIdentity(NON_EXISTENT_ID);
	}

	@Test
	public void testIsIdenticalBillAgremmentTrue()
	{
		final TmaSubscriptionBaseData subscriptionBase = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY);
		final TmaSubscriptionBaseData subscriptionBase1 = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY1);
		final List<TmaSubscriptionBaseData> subscriptionBases = new ArrayList<>();
		subscriptionBases.add(subscriptionBase);
		subscriptionBases.add(subscriptionBase1);
		final boolean isIdenticalBillAgremment = tmaSubscriptionBaseFacade.isIdenticalBillAgremment(subscriptionBases);
		Assert.assertTrue(isIdenticalBillAgremment);

	}

	@Test
	public void testIsIdenticalBillAgremmentFalse()
	{
		final TmaSubscriptionBaseData subscriptionBase = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY);
		final TmaSubscriptionBaseData subscriptionBase1 = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY2);
		final List<TmaSubscriptionBaseData> subscriptionBases = new ArrayList<>();
		subscriptionBases.add(subscriptionBase);
		subscriptionBases.add(subscriptionBase1);
		final boolean isIdenticalBillAgremment = tmaSubscriptionBaseFacade.isIdenticalBillAgremment(subscriptionBases);
		Assert.assertFalse(isIdenticalBillAgremment);

	}

	@Test
	public void testIsIdenticalBillAgremmentWithNullBillAgree()
	{
		final TmaSubscriptionBaseData subscriptionBase2 = tmaSubscriptionBaseFacade
				.getSubscriptionBaseBySubscriberIdentity(SUBSCRIBER_IDENTITY3);
		final List<TmaSubscriptionBaseData> subscriptionBases = new ArrayList<>();
		subscriptionBases.add(subscriptionBase2);
		final boolean isIdenticalBillAgremment = tmaSubscriptionBaseFacade.isIdenticalBillAgremment(subscriptionBases);
		Assert.assertFalse(isIdenticalBillAgremment);

	}

}
