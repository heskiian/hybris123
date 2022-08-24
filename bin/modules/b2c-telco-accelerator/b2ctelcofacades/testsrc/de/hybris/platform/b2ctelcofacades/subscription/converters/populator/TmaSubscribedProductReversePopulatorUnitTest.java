/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.NOT_EXISTING_SUBSCRIBER_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_BILLING_SUBSCRIPTION_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_BILLING_SYSTEM_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_PARENT_PO_CODE;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.TEST_SUBSCRIBER_ID;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductModelsPopulatedWithDefaultValues;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.assertTmaSubscribedProductModelsPopulatedWithDefaultValuesForPO;
import static de.hybris.platform.b2ctelcoservices.services.TmaServiceTestDataSetup.generateTmaSubscribedProductData;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;


@UnitTest
public class TmaSubscribedProductReversePopulatorUnitTest
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	@Mock
	private TmaSubscriptionBaseService subscriptionBaseService;
	private TmaSubscribedProductReversePopulator serviceReversePopulator;
	private TmaSubscribedProductData source;
	private TmaSubscribedProductModel target;
	private TmaSubscriptionBaseModel subscriptionBaseModel;
	private TmaProductOfferingModel productOfferingModel;
	@Mock
	private TmaPoService PoService;
	@Mock
	private CatalogVersionService catalogVersionService;
	public static final String PRODUCT_CATALOG = "b2ctelcoProductCatalog";
	public static final String PRODUCT_CATALOG_VERSION = "Online";

	@Before
	public void setUp() throws ParseException
	{
		initMocks(this);
		subscriptionBaseModel = createSubscriptionBase();
		when(subscriptionBaseService.getSubscriptionBase(TEST_SUBSCRIBER_ID, TEST_BILLING_SYSTEM_ID))
				.thenReturn(subscriptionBaseModel);
		serviceReversePopulator = new TmaSubscribedProductReversePopulator();
		serviceReversePopulator.setTmaSubscriptionBaseService(subscriptionBaseService);

		productOfferingModel = createParentProductOffering();
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		when(PoService.getPoForCode(TEST_PARENT_PO_CODE)).thenReturn(productOfferingModel);
		target = new TmaSubscribedProductModel();
	}

	@Test
	public void testPopulate() throws Exception
	{
		givenSourceForId(TEST_BILLING_SYSTEM_ID, TEST_PARENT_PO_CODE);
		whenPopulateIsRun();
		assertTargetIsPopulated();
	}

	@Test
	public void testPopulateForPO() throws Exception
	{
		givenSourceForId(TEST_BILLING_SYSTEM_ID, TEST_PARENT_PO_CODE);
		whenPopulateIsRun();
		assertTargetIsPopulatedForPO();
	}

	@Test
	public void testPopulateWithMissingSubscriptionBase() throws Exception
	{
		expectedException.expect(ModelNotFoundException.class);
		givenSourceForId(NOT_EXISTING_SUBSCRIBER_ID, TEST_PARENT_PO_CODE);
		whenPopulateIsRun();
		assertTargetIsPopulated();
	}

	private void assertTargetIsPopulated() throws ParseException
	{
		assertTmaSubscribedProductModelsPopulatedWithDefaultValues(target, subscriptionBaseModel);
	}

	private void assertTargetIsPopulatedForPO() throws ParseException
	{
		assertTmaSubscribedProductModelsPopulatedWithDefaultValuesForPO(target, productOfferingModel);
	}

	private void whenPopulateIsRun()
	{
		serviceReversePopulator.populate(source, target);
	}

	private void givenSourceForId(final String subscriptionIdentifier, final String parentProductoffering) throws ParseException
	{
		source = generateTmaSubscribedProductData(subscriptionIdentifier, TEST_BILLING_SUBSCRIPTION_ID, parentProductoffering);
	}

	private TmaSubscriptionBaseModel createSubscriptionBase()
	{
		final TmaSubscriptionBaseModel subscriptionBaseModel = new TmaSubscriptionBaseModel();
		subscriptionBaseModel.setSubscriberIdentity(TEST_SUBSCRIBER_ID);
		subscriptionBaseModel.setBillingSystemId(TEST_BILLING_SYSTEM_ID);
		return subscriptionBaseModel;
	}

	public static TmaProductOfferingModel createParentProductOffering()
	{
		final TmaProductOfferingModel productOfferingModel = new TmaBundledProductOfferingModel();
		productOfferingModel.setCode(TEST_PARENT_PO_CODE);
		return productOfferingModel;
	}

}
