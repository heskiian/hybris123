/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionAccessDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscriptionAccessDaoIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String SEARCH_SUBSCRIBER_IDENTITY = "02012344321";

	@Resource
	private TmaSubscriptionAccessDao tmaSubscriptionAccessDao;

	@Test
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBase()
	{
		final TmaSubscriptionAccessModel expectedModel = findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID,
				BILLING_SYSTEM_ID, SEARCH_SUBSCRIBER_IDENTITY);
		assertNotNull("The model is null", expectedModel);
		assertTrue("The model hasn't the expected billing system id",
				BILLING_SYSTEM_ID.equals(expectedModel.getSubscriptionBase().getBillingSystemId()));
		assertTrue("The model hasn't the expected subscriber identity",
				SEARCH_SUBSCRIBER_IDENTITY.equals(expectedModel.getSubscriptionBase().getSubscriberIdentity()));
		assertTrue("The model hasn't the expected access type", ACCESS_TYPE.equals(expectedModel.getAccessType()));
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNonValidPrincipal()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(NON_EXISTENT_ID, BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNonValidBillingSystemId()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID, NON_EXISTENT_ID, SUBSCRIBER_IDENTITY);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNonValidSubscriberIdentity()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID, BILLING_SYSTEM_ID, NON_EXISTENT_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNullPrincipal()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(null, BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNullBillingSystemId()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID, null, SUBSCRIBER_IDENTITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessByPrincipalAndSubscriptionBaseWhenNullSubscriberIdentity()
	{
		findSubscriptionAccessByPrincipalAndSubscriptionBase(CUSTOMER_UID, BILLING_SYSTEM_ID, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessByPrincipalNull()
	{
		findSubscriptionAccessByPrincipal(null);
	}

	@Test
	public void testFindSubscriptionAccessByPrincipal()
	{
		final List<TmaSubscriptionAccessModel> result = findSubscriptionAccessByPrincipal(CUSTOMER_UID);
		assertNotNull("The list is null", result);
		assertTrue("The list has two elements", 1 == result.size());
	}

	@Test
	public void testFindSubscriptionAccessBySubscriberIdentity()
	{
		final List<TmaSubscriptionAccessModel> result = findSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID,
				SUBSCRIBER_IDENTITY);
		assertNotNull("The list is null", result);
		assertTrue("The list has two elements", 2 == result.size());
	}

	@Test
	public void testFindSubscriptionAccessBySubscriptionBaseWhenNotValidBillingSystemId()
	{
		final List<TmaSubscriptionAccessModel> result = findSubscriptionAccessesBySubscriberIdentity(NON_EXISTENT_ID,
				SUBSCRIBER_IDENTITY);
		assertEquals(Collections.emptyList(), result);
	}

	@Test
	public void testFindSubscriptionAccessBySubscriptionBaseWhenNotValidSubscriberIdentity()
	{
		final List<TmaSubscriptionAccessModel> result = findSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID,
				NON_EXISTENT_ID);
		assertEquals(Collections.emptyList(), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessBySubscriptionBaseWhenNullBillingSystemId()
	{
		findSubscriptionAccessesBySubscriberIdentity(null, SUBSCRIBER_IDENTITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessBySubscriptionBaseWhenNullSubscriberIdentity()
	{
		findSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID, null);
	}

	@Test
	public void testFindSubscriptionAccessesByType()
	{
		final List<TmaSubscriptionAccessModel> resultList = findSubscriptionAccessesByType(CUSTOMER_UID, ACCESS_TYPE);
		assertEquals(1, resultList.size());
		final TmaSubscriptionAccessModel result = resultList.get(0);
		assertEquals(TmaAccessType.OWNER, result.getAccessType());
	}

	@Test
	public void testFindSubscriptionAccessesByTypeEmptyList()
	{
		final List<TmaSubscriptionAccessModel> result = findSubscriptionAccessesByType(CUSTOMER_UID, TmaAccessType.ADMINISTRATOR);
		assertEquals(Collections.emptyList(), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessesByTypeWhenNullPrincipalId()
	{
		tmaSubscriptionAccessDao.findSubscriptionAccessesByType(null, TmaAccessType.OWNER);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindSubscriptionAccessesByTypeWhenNullAccessType()
	{
		tmaSubscriptionAccessDao.findSubscriptionAccessesByType(CUSTOMER_UID, null);
	}

	@Test
	public void testFindSubscriptionAccessesByTypeWhenNotValidPrincipalId()
	{
		final List<TmaSubscriptionAccessModel> result = tmaSubscriptionAccessDao.findSubscriptionAccessesByType(NON_EXISTENT_ID,
				TmaAccessType.OWNER);
		assertEquals(Collections.emptyList(), result);
	}

	private List<TmaSubscriptionAccessModel> findSubscriptionAccessByPrincipal(final String principalUid)
	{
		return tmaSubscriptionAccessDao.findSubscriptionAccessByPrincipalUid(principalUid);
	}

	private TmaSubscriptionAccessModel findSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity)
	{
		return tmaSubscriptionAccessDao.findSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId,
				subscriberIdentity);
	}

	private List<TmaSubscriptionAccessModel> findSubscriptionAccessesBySubscriberIdentity(final String billingSystemId,
			final String subscriberIdentity)
	{
		return tmaSubscriptionAccessDao.findSubscriptionAccessesBySubscriberIdentity(subscriberIdentity, billingSystemId);
	}

	public List<TmaSubscriptionAccessModel> findSubscriptionAccessesByType(final String principalUid,
			final TmaAccessType accessType)
	{
		return tmaSubscriptionAccessDao.findSubscriptionAccessesByType(principalUid, accessType);
	}
}
