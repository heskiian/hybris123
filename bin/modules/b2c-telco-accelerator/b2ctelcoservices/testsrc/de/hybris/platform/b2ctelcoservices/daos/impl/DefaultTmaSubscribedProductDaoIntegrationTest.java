/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.commons.BaseCustomerInventoryIntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultTmaSubscribedProductDaoIntegrationTest extends BaseCustomerInventoryIntegrationTest
{
	@Resource
	private DefaultTmaSubscribedProductDao tmaSubscribedProductDao;

	@Test
	public void testFindExistingService()
	{
		final TmaSubscribedProductModel subscribedProductModel = tmaSubscribedProductDao.findSubscribedProduct(BILLING_SYSTEM_ID,
				BILLING_SUBSCRIPTION_ID);
		assertNotNull(subscribedProductModel.getId());
		assertEquals(BILLING_SYSTEM_ID, subscribedProductModel.getBillingsystemId());
		assertEquals(BILLING_SUBSCRIPTION_ID, subscribedProductModel.getBillingSubscriptionId());
	}

	@Test
	public void testFindExistingServiceById()
	{
		final TmaSubscribedProductModel subscribedProductModel = tmaSubscribedProductDao
				.findSubscribedProductById(SUBSCRIBED_PRODUCT_ID);
		assertNotNull(subscribedProductModel.getId());
		assertEquals(SUBSCRIBED_PRODUCT_ID, subscribedProductModel.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindNonExistingServiceId()
	{
		tmaSubscribedProductDao.findSubscribedProductById(NON_EXISTENT_ID);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testFindNonExistingService()
	{
		tmaSubscribedProductDao.findSubscribedProduct(NON_EXISTENT_ID, NON_EXISTENT_ID);
	}
}
