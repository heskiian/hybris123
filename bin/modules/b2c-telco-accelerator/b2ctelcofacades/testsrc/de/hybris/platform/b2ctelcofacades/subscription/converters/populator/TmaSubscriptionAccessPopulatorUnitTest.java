/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class TmaSubscriptionAccessPopulatorUnitTest
{
	private static final String BILLING_SYSTEM_ID = "IN";
	private static final String SUBSCRIBER_IDENTITY = "IDENTITY";
	private static final String PRINCIPAL_UID = "CUSTOMER_UID";
	private static final TmaAccessType ACCESS_TYPE = TmaAccessType.BENEFICIARY;
	private TmaSubscriptionAccessModel source;
	private TmaSubscriptionAccessData target;
	private TmaSubscriptionAccessPopulator populator;
	@Mock
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter;
	@Mock
	private Converter<TmaSubscriptionBaseModel, ProductData> tmaSubscriptionBasePORefConverter;

	@Before
	public void setUp()
	{
		populator = new TmaSubscriptionAccessPopulator();
		target = new TmaSubscriptionAccessData();

		final AbstractPopulatingConverter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> converter = new AbstractPopulatingConverter<>();
		converter.setTargetClass(TmaSubscriptionBaseData.class);
		converter.setPopulators(Collections.singletonList(
				new TmaSubscriptionBasePopulator(tmaSubscriptionAccessRefConverter, tmaSubscriptionBasePORefConverter)));

		populator.setSubscriptionBaseDataConverter(converter);
	}

	@Test
	public void testPopulate()
	{
		setUpSource();
		whenPopulateIsRun();
		testDataPopulated();
	}

	private void testDataPopulated()
	{
		assertTrue("It hasn't the expected principal id", source.getPrincipal().getUid().equals(target.getPrincipalUid()));
		assertTrue("It hasn't the expected billing system id",
				source.getSubscriptionBase().getBillingSystemId().equals(target.getBillingSystemId()));
		assertTrue("It hasn't the expected subscriber identity",
				source.getSubscriptionBase().getSubscriberIdentity().equals(target.getSubscriberIdentity()));
		assertTrue("It hasn't the expected access type", source.getAccessType().getCode().equals(
				target.getAccessType().name()));
	}

	private void whenPopulateIsRun()
	{
		populator.populate(source, target);
	}

	private void setUpSource()
	{
		final PrincipalModel principalModel = new PrincipalModel();
		principalModel.setUid(PRINCIPAL_UID);
		final TmaSubscriptionBaseModel subscriptionBaseModel = new TmaSubscriptionBaseModel();
		subscriptionBaseModel.setBillingSystemId(BILLING_SYSTEM_ID);
		subscriptionBaseModel.setSubscriberIdentity(SUBSCRIBER_IDENTITY);
		source = new TmaSubscriptionAccessModel();
		source.setPrincipal(principalModel);
		source.setSubscriptionBase(subscriptionBaseModel);
		source.setAccessType(ACCESS_TYPE);
	}
}
