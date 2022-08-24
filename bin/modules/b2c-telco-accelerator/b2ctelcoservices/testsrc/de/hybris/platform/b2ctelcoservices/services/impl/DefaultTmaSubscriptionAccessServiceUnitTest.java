/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultTmaSubscriptionAccessDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.core.model.security.PrincipalModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@UnitTest
public class DefaultTmaSubscriptionAccessServiceUnitTest
{
	private static final String PRINCIPAL_UID = "principalUid";
	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";
	private static final String BILLING_SYSTEM_ID = "IN";

	private static final TmaAccessType ACCESS_TYPE = TmaAccessType.OWNER;

	private DefaultTmaSubscriptionAccessService subscriptionAccessService;
	private DefaultTmaSubscriptionAccessDao subscriptionAccessDao;
	private TmaSubscriptionAccessModel subscriptionAccessModel;

	@Before
	public void setUp()
	{
		subscriptionAccessDao = mock(DefaultTmaSubscriptionAccessDao.class);
		subscriptionAccessService = new DefaultTmaSubscriptionAccessService();
		subscriptionAccessService.setTmaSubscriptionAccessDao(subscriptionAccessDao);
		subscriptionAccessModel = createTmaSubscriptionAccessModel();
	}

	@Test
	public void testGetSubscriberAccessByPrincipalAndSubscriptionBase()
	{
		when(subscriptionAccessDao
				.findSubscriptionAccessByPrincipalAndSubscriptionBase(PRINCIPAL_UID, BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY))
				.thenReturn(subscriptionAccessModel);
		TmaSubscriptionAccessModel actual = subscriptionAccessService.getSubscriptionAccessByPrincipalAndSubscriptionBase(
				PRINCIPAL_UID, BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY);

		assertNotNull("The subscriptionAccessModel isn't null", actual);
		assertTrue("The mode has the expected principal uid", PRINCIPAL_UID.equals(actual.getPrincipal().getUid()));
		assertTrue("The subscriptionAccessModel has the expected access type", ACCESS_TYPE.equals(actual.getAccessType()));
		assertTrue("The subscriptionAccessModel has the expected subscriber identity",
				SUBSCRIBER_IDENTITY.equals(actual.getSubscriptionBase().getSubscriberIdentity()));
		assertTrue("The subscriptionAccessModel has the expected subscriber identity",
				BILLING_SYSTEM_ID.equals(actual.getSubscriptionBase().getBillingSystemId()));
	}

	@Test
	public void testGetSubscriptionAccessesBySubscriberIdentity()
	{
		List<TmaSubscriptionAccessModel> expected = Arrays.asList(subscriptionAccessModel);
		when(subscriptionAccessDao.findSubscriptionAccessesBySubscriberIdentity(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID))
				.thenReturn(expected);

		List<TmaSubscriptionAccessModel> actual = subscriptionAccessService
				.getSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY);
		assertTrue("The list has one element", 1 == actual.size());
		assertTrue("The subscriptionAccessModel has the expected uid",
				SUBSCRIBER_IDENTITY.equals(expected.get(0).getSubscriptionBase()
						.getSubscriberIdentity()));
		assertTrue("The subscriptionAccessModel has the expected uid",
				BILLING_SYSTEM_ID.equals(expected.get(0).getSubscriptionBase()
						.getBillingSystemId()));
	}

	@Test
	public void testGetSubscriptionAccessesByPrincipal()
	{
		List<TmaSubscriptionAccessModel> expected = Arrays.asList(subscriptionAccessModel);
		when(subscriptionAccessDao.findSubscriptionAccessByPrincipalUid(PRINCIPAL_UID)).thenReturn(expected);

		List<TmaSubscriptionAccessModel> actual = subscriptionAccessService
				.getSubscriptionAccessesByPrincipalUid(PRINCIPAL_UID);
		assertTrue("The list has one element", 1 == actual.size());
		assertTrue("The subscriptionAccessModel has the expected uid",
				SUBSCRIBER_IDENTITY.equals(expected.get(0).getSubscriptionBase()
						.getSubscriberIdentity()));
		assertTrue("The subscriptionAccessModel has the expected uid",
				BILLING_SYSTEM_ID.equals(expected.get(0).getSubscriptionBase()
						.getBillingSystemId()));
	}
	
	@Test
	public void testGetSubscriptionAccessesByType()
	{
		List<TmaSubscriptionAccessModel> expected = Arrays.asList(subscriptionAccessModel);
		when(subscriptionAccessDao.findSubscriptionAccessesByType(PRINCIPAL_UID, ACCESS_TYPE)).thenReturn(expected);
		List<TmaSubscriptionAccessModel> actual = subscriptionAccessService.getSubscriptionAccessesByType(PRINCIPAL_UID,
				ACCESS_TYPE);
		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0), actual.get(0));
	}

	private TmaSubscriptionAccessModel createTmaSubscriptionAccessModel()
	{
		PrincipalModel principalModel = new PrincipalModel();
		principalModel.setUid(PRINCIPAL_UID);
		TmaSubscriptionBaseModel subscriptionBaseModel = new TmaSubscriptionBaseModel();
		subscriptionBaseModel.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		subscriptionBaseModel.setBillingSystemId(BILLING_SYSTEM_ID);
		subscriptionAccessModel = new TmaSubscriptionAccessModel();
		subscriptionAccessModel.setPrincipal(principalModel);
		subscriptionAccessModel.setAccessType(ACCESS_TYPE);
		subscriptionAccessModel.setSubscriptionBase(subscriptionBaseModel);
		return subscriptionAccessModel;
	}
}
