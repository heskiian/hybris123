/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


/**
 * Test class for {@link PriceIdPrepareInterceptor}
 *
 * @since 1903
 *
 */
@UnitTest
public class PriceIdPrepareInterceptorTest
{

	private static final String TEST_PRICE_ID = "TestPriceUID";

	private PriceIdPrepareInterceptor priceIdPrepareInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private KeyGenerator priceKeyGenerator;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.priceIdPrepareInterceptor = new PriceIdPrepareInterceptor();
		priceKeyGenerator = Mockito.mock(KeyGenerator.class);
		priceIdPrepareInterceptor.setPriceIDGenerator(priceKeyGenerator);
		Mockito.when(priceKeyGenerator.generate()).thenReturn(TEST_PRICE_ID);
	}

	@Test
	public void testPriceIdForNewModel() throws Exception
	{
		final PDTRowModel pdtRowModel = new PDTRowModel();
		Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(true);

		getPriceIdPrepareInterceptor().onPrepare(pdtRowModel, interceptorContext);
		Assert.assertNotNull(pdtRowModel.getCode());
	}

	@Test
	public void testPriceIdForModifiedModel() throws Exception
	{
		final PDTRowModel priceRowModel = new PDTRowModel();
		Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(false);

		getPriceIdPrepareInterceptor().onPrepare(priceRowModel, interceptorContext);
		Assert.assertNull(priceRowModel.getCode());
	}

	public PriceIdPrepareInterceptor getPriceIdPrepareInterceptor()
	{
		return priceIdPrepareInterceptor;
	}

	public void setPriceIdPrepareInterceptor(final PriceIdPrepareInterceptor priceIdPrepareInterceptor)
	{
		this.priceIdPrepareInterceptor = priceIdPrepareInterceptor;
	}
}
