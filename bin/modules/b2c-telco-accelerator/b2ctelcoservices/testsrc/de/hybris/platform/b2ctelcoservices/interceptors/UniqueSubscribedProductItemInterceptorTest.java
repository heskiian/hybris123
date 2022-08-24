/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaSubscribedProductDao;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


@UnitTest
public class UniqueSubscribedProductItemInterceptorTest
{
	private static final String EXISTING_SERVICE_ID = "service_id";
	private static final String NEW_SERVICE_ID = "new_service_id";
	private static final String BILLING_SYSTEM_ID = "billing_system_id";
	private static final String BILLING_SUBSCRIPTION_ID = "billing_subscription_id";

	@Mock
	private TmaSubscribedProductDao tmaSubscribedProductDao;
	private UniqueSubscribedProductItemInterceptor interceptor;
	private TmaSubscribedProductModel existingService;
	private TmaSubscribedProductModel serviceToBeValidated;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.existingService = createTmaSubscribedProductModel(EXISTING_SERVICE_ID);
		this.interceptor = new UniqueSubscribedProductItemInterceptor();
		interceptor.setTmaSubscribedProductDao(tmaSubscribedProductDao);
	}

	@Test
	public void testSameServiceExists() throws InterceptorException
	{
		givenExistingService(existingService);
		givenServiceToBeValidated(existingService);
		thenValidationPasses();
	}

	@Test
	public void testServiceNotFound() throws InterceptorException
	{
		givenServiceNotFound();
		givenServiceToBeValidated(createTmaSubscribedProductModel(NEW_SERVICE_ID));
		thenValidationPasses();
	}

	@Test(expected = InterceptorException.class)
	public void testServiceExistsWithDifferentId() throws InterceptorException
	{
		givenExistingService(existingService);
		givenServiceToBeValidated(createTmaSubscribedProductModel(NEW_SERVICE_ID));
		whenValidationIsRun();
	}

	private void givenExistingService(TmaSubscribedProductModel subscribedProductModel)
	{
		Mockito.when(tmaSubscribedProductDao.findSubscribedProduct(BILLING_SYSTEM_ID, BILLING_SUBSCRIPTION_ID)).thenReturn(subscribedProductModel);
	}

	private void givenServiceNotFound()
	{
		Mockito.when(tmaSubscribedProductDao.findSubscribedProduct(BILLING_SYSTEM_ID, BILLING_SUBSCRIPTION_ID)).thenThrow(ModelNotFoundException.class);
	}

	private void givenServiceToBeValidated(TmaSubscribedProductModel service)
	{
		this.serviceToBeValidated = service;
	}

	private void whenValidationIsRun() throws InterceptorException
	{
		interceptor.onValidate(serviceToBeValidated, null);
	}

	private void thenValidationPasses()
	{
		try
		{
			whenValidationIsRun();
		}
		catch (InterceptorException e)
		{
			Assert.assertTrue("Validation expected to pass, but it failed.", false);
		}
	}

	private TmaSubscribedProductModel createTmaSubscribedProductModel(String id)
	{
		TmaSubscribedProductModel subscribedProductModel = new TmaSubscribedProductModel();
		subscribedProductModel.setId(id);
		subscribedProductModel.setBillingsystemId(BILLING_SYSTEM_ID);
		subscribedProductModel.setBillingSubscriptionId(BILLING_SUBSCRIPTION_ID);
		return subscribedProductModel;
	}
}
