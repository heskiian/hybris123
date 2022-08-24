/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscribedProductService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscribedProductServiceIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	private static final String EXISTING_SUBSCRIBER_IDENTITY = "02015554321";
	private static final String NEW_BILLING_SUBSCRIPTION_ID = "123456789";
	private final String SUBSCRIBED_PRODUCT_ID = "1000000001";
	private final String TEST_PRODUCT_ID = "TEST_ID";

	@Resource
	private ModelService modelService;
	@Resource
	private TmaSubscribedProductService tmaSubscribedProductService;
	@Resource
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;

	@Test
	public void testFindService()
	{
		final TmaSubscribedProductModel service = tmaSubscribedProductService.findSubscribedProduct(BILLING_SYSTEM_ID,
				BILLING_SUBSCRIPTION_ID);
		assertNotNull(service.getId());
		assertEquals(BILLING_SYSTEM_ID, service.getBillingsystemId());
		assertEquals(BILLING_SUBSCRIPTION_ID, service.getBillingSubscriptionId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindServiceForNonExistentIds()
	{
		tmaSubscribedProductService.findSubscribedProduct(NON_EXISTENT_ID, NON_EXISTENT_ID);
	}

	@Test
	public void testFindServiceByID()
	{
		final Object service = tmaSubscribedProductService.findSubscribedProductById(SUBSCRIBED_PRODUCT_ID);
		assertTrue("Service Found By ID", service instanceof TmaSubscribedProductModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindServiceByNull()
	{
		tmaSubscribedProductService.findSubscribedProductById(null);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindServiceInvalidId()
	{
		tmaSubscribedProductService.findSubscribedProductById(TEST_PRODUCT_ID);
	}

	@Test
	public void testCreateService()
	{
		final TmaSubscribedProductModel createdService = tmaSubscribedProductService
				.createSubscribedProduct(buildTmaSubscribedProductModel(true));
		assertNotNull(createdService.getId());
		assertEquals(BILLING_SYSTEM_ID, createdService.getBillingsystemId());
		assertEquals(NEW_BILLING_SUBSCRIPTION_ID, createdService.getBillingSubscriptionId());
		assertEquals(EXISTING_SUBSCRIBER_IDENTITY, createdService.getSubscriptionBase().getSubscriberIdentity());
	}

	@Test
	public void testCreateServiceWithoutBillingId()
	{
		final TmaSubscribedProductModel createdService = tmaSubscribedProductService
				.createSubscribedProduct(buildTmaSubscribedProductModel(false));
		assertNotNull(createdService.getId());
		assertEquals(BILLING_SYSTEM_ID, createdService.getBillingsystemId());
		assertEquals(EXISTING_SUBSCRIBER_IDENTITY, createdService.getSubscriptionBase().getSubscriberIdentity());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testDeleteService()
	{
		final TmaSubscribedProductModel existingSubscribedProduct = tmaSubscribedProductService.findSubscribedProduct(
				BILLING_SYSTEM_ID,
				BILLING_SUBSCRIPTION_ID);
		assertNotNull(existingSubscribedProduct.getId());
		tmaSubscribedProductService.deleteSubscribedProduct(BILLING_SYSTEM_ID, BILLING_SUBSCRIPTION_ID);
		tmaSubscribedProductService.findSubscribedProduct(BILLING_SYSTEM_ID, BILLING_SUBSCRIPTION_ID);
	}

	private TmaSubscribedProductModel buildTmaSubscribedProductModel(final boolean isBillingIdPresent)
	{
		final TmaSubscribedProductModel subscribedProductModel = modelService.create(TmaSubscribedProductModel.class);
		subscribedProductModel.setBillingsystemId(BILLING_SYSTEM_ID);
		if (isBillingIdPresent)
		{
			subscribedProductModel.setBillingSubscriptionId(NEW_BILLING_SUBSCRIPTION_ID);
		}
		subscribedProductModel
				.setSubscriptionBase(tmaSubscriptionBaseService.getSubscriptionBase(EXISTING_SUBSCRIBER_IDENTITY, BILLING_SYSTEM_ID));
		return subscribedProductModel;
	}
}
