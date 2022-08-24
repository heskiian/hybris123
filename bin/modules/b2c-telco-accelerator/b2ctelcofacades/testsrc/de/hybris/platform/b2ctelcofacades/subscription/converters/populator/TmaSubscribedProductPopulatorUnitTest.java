/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;


import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_BILLING_SYSTEM_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_SUBSCRIBER_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductDataIsPopulatedWithDefaultValues;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductDataIsPopulatedWithDefaultValuesForPO;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.createParentProductOffering;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.createSubscriptionBaseForIdentity;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.generateTmaSubscribedProductModelForProductOffering;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.generateTmaSubscribedProductModelForSubscriptionBase;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class TmaSubscribedProductPopulatorUnitTest
{
	@Mock
	private TmaSubscriptionBaseService subscriptionBaseService;
	private TmaSubscribedProductPopulator subscribedProductPopulator;
	private TmaSubscribedProductModel source;
	private TmaSubscribedProductData target;
	private TmaSubscriptionBaseModel subscriptionBase;
	private TmaProductOfferingModel productOffering;
	@Mock
	private TmaPoService tmaPoService;
	@Mock
	private Converter<ProductModel, ProductData> productConverter;
	@Mock
	private Converter<TmaBillingAccountModel, TmaBillingAccountData> tmaBillingAccountDataConverter;
	@Mock
	private Converter<TmaSubscribedProductModel, ProductData> tmaSubscribedProductProductDataConverter;
	@Mock
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter;

	@Before
	public void setUp() throws ParseException
	{
		initMocks(this);
		subscriptionBase = createSubscriptionBaseForIdentity(TEST_SUBSCRIBER_ID, TEST_BILLING_SYSTEM_ID);
		when(subscriptionBaseService.getSubscriptionBase(TEST_SUBSCRIBER_ID, TEST_BILLING_SYSTEM_ID)).thenReturn(subscriptionBase);
		productOffering = createParentProductOffering();
		subscribedProductPopulator = new TmaSubscribedProductPopulator(tmaPoService, productConverter,
				tmaBillingAccountDataConverter, tmaSubscribedProductProductDataConverter, tmaSubscriptionAccessRefConverter);
		target = new TmaSubscribedProductData();
	}

	@Test
	public void testPopulateWithSubscriptionBase() throws Exception
	{
		givenSourceModel(subscriptionBase);
		givenSourceModelForPO(productOffering);
		whenPopulateIsRun();
		thenTheTargetIsPopulated();
	}

	@Test
	public void testPopulateWithProductOffering() throws Exception
	{
		givenSourceModel(subscriptionBase);
		givenSourceModelForPO(productOffering);
		whenPopulateIsRun();
		thenTheTargetIsPopulatedForPO();
	}

	@Test
	public void testPopulateWithoutSubscriptionBase() throws Exception
	{
		givenSourceModel(null);
		whenPopulateIsRun();
		theTheTargetCommonDataIsPopulated();
	}

	@Test
	public void testPopulateWithoutProductOffering() throws Exception
	{
		givenSourceModel(subscriptionBase);
		givenSourceModelForPO(null);
		whenPopulateIsRun();
		theTheTargetCommonDataIsPopulatedForPO();
	}

	private void thenTheTargetIsPopulated() throws ParseException
	{
		assertTmaSubscribedProductDataIsPopulatedWithDefaultValues(target);
	}

	private void thenTheTargetIsPopulatedForPO() throws ParseException
	{
		assertTmaSubscribedProductDataIsPopulatedWithDefaultValuesForPO(target);
	}

	private void theTheTargetCommonDataIsPopulated() throws ParseException
	{
		assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues(target);
	}

	private void theTheTargetCommonDataIsPopulatedForPO() throws ParseException
	{
		assertTmaSubscribedProductDataIsPopulatedWithCommonDefaultValues(target);
	}

	private void whenPopulateIsRun()
	{
		subscribedProductPopulator.populate(source, target);
	}

	private void givenSourceModel(final TmaSubscriptionBaseModel subscriptionBase) throws ParseException
	{
		source = generateTmaSubscribedProductModelForSubscriptionBase(new TmaSubscribedProductModel(), subscriptionBase);
	}

	private void givenSourceModelForPO(final TmaProductOfferingModel productOfferingModel) throws ParseException
	{
		source = generateTmaSubscribedProductModelForProductOffering(new TmaSubscribedProductModel(), productOfferingModel,
				subscriptionBase);
	}
}
