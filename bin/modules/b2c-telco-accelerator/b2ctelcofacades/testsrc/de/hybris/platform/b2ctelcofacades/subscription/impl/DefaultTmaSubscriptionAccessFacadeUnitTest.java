/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAccessTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultTmaSubscriptionAccessFacadeUnitTest
{
	private static final String PRINCIPAL_UID = "principalUid";
	private static final String SUBSCRIBER_IDENTITY = "subscriberIdentity";
	private static final String BILLING_SYSTEM_ID = "IN";
	private static final TmaAccessType ACCESS_TYPE = TmaAccessType.OWNER;

	private TmaSubscriptionAccessModel subscriptionAccessModel;
	private TmaSubscriptionAccessData subscriptionAccessData;
	private List<TmaSubscriptionAccessData> subscriptionAccessDatas;
	private List<TmaSubscriptionAccessModel> tmaSubscriptionAccessModels;

	@Mock
	private ModelService modelService;

	@Mock
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;

	@Mock
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter;
	private DefaultTmaSubscriptionAccessFacade tmaSubscriptionAccessFacade;

	@Before
	public void setUp() throws SubscriptionFacadeException
	{
		MockitoAnnotations.initMocks(this);
		setTmaSubscriptionAccessModel();
		setTmaSubscriptionAccessData();

		tmaSubscriptionAccessFacade = new DefaultTmaSubscriptionAccessFacade();
		tmaSubscriptionAccessFacade.setTmaSubscriptionAccessService(tmaSubscriptionAccessService);
		tmaSubscriptionAccessFacade.setTmaSubscriptionAccessConverter(tmaSubscriptionAccessConverter);

		when(tmaSubscriptionAccessFacade.getSubscriptionAccessByPrincipalAndSubscriptionBase(PRINCIPAL_UID, BILLING_SYSTEM_ID,
				SUBSCRIBER_IDENTITY)).thenReturn(subscriptionAccessModel);
		when(tmaSubscriptionAccessService.getSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY))
				.thenReturn(tmaSubscriptionAccessModels);
		when(tmaSubscriptionAccessConverter.convertAll(tmaSubscriptionAccessModels)).thenReturn(subscriptionAccessDatas);
	}

	@Test
	public void getSubscriptionAccessByPrincipalAndSubscriptionBaseTest()
	{
		final TmaSubscriptionAccessModel accessModel = tmaSubscriptionAccessFacade
				.getSubscriptionAccessByPrincipalAndSubscriptionBase(PRINCIPAL_UID, BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY);
		assertTrue(subscriptionAccessModel.equals(accessModel));
	}

	@Test
	public void getSubscriptionAccessesBySubscriberIdentity()
	{
		final List<TmaSubscriptionAccessData> subscriptionAccesses = tmaSubscriptionAccessFacade
				.getSubscriptionAccessesBySubscriberIdentity(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertTrue(subscriptionAccessDatas.equals(subscriptionAccesses));
	}

	@Test
	public void testWhenSubscriptionAccessesNull()
	{
		when(tmaSubscriptionAccessService.getSubscriptionAccessesBySubscriberIdentity(BILLING_SYSTEM_ID, SUBSCRIBER_IDENTITY))
				.thenReturn(null);
		final List<TmaSubscriptionAccessData> subscriptionAccesses = tmaSubscriptionAccessFacade
				.getSubscriptionAccessesBySubscriberIdentity(SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID);
		assertTrue(subscriptionAccesses.isEmpty());
	}

	private void setTmaSubscriptionAccessModel()
	{
		tmaSubscriptionAccessModels = new ArrayList<>();
		subscriptionAccessModel = new TmaSubscriptionAccessModel();
		subscriptionAccessModel.setAccessType(ACCESS_TYPE);
		modelService.save(subscriptionAccessModel);
		tmaSubscriptionAccessModels.add(subscriptionAccessModel);
	}

	private void setTmaSubscriptionAccessData()
	{
		subscriptionAccessData = new TmaSubscriptionAccessData();
		subscriptionAccessDatas = new ArrayList<>();
		subscriptionAccessData.setBillingSystemId(BILLING_SYSTEM_ID);
		subscriptionAccessData.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		subscriptionAccessData.setPrincipalUid(PRINCIPAL_UID);
		subscriptionAccessData.setAccessType(TmaAccessTypeData.OWNER);
		subscriptionAccessDatas.add(subscriptionAccessData);
	}
}
