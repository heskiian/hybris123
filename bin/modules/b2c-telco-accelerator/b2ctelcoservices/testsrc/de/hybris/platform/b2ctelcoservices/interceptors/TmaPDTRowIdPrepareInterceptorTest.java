/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
 * Test class for {@link TmaPDTRowIdPrepareInterceptor}
 *
 * @since 1907
 *
 */
@UnitTest
public class TmaPDTRowIdPrepareInterceptorTest
{

	private static final String TEST_PRICE_ID = "TestPriceUID";

	private TmaPDTRowIdPrepareInterceptor priceIdPrepareInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private KeyGenerator priceKeyGenerator;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.priceIdPrepareInterceptor = new TmaPDTRowIdPrepareInterceptor();
		priceKeyGenerator = Mockito.mock(KeyGenerator.class);
		priceIdPrepareInterceptor.setTmaPDTRowIdGenerator(priceKeyGenerator);
		Mockito.when(priceKeyGenerator.generate()).thenReturn(TEST_PRICE_ID);
	}

	@Test
	public void testPDTRowIdForNewModel() throws Exception
	{
		final PDTRowModel pdtRowModel = new PDTRowModel();
		Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(true);

		getPDTRowIdPrepareInterceptor().onPrepare(pdtRowModel, interceptorContext);
		Assert.assertNotNull(pdtRowModel.getCode());
	}

	@Test
	public void testPDTRowIdForModifiedModel() throws Exception
	{
		final PDTRowModel priceRowModel = new PDTRowModel();
		Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(false);

		getPDTRowIdPrepareInterceptor().onPrepare(priceRowModel, interceptorContext);
		Assert.assertNull(priceRowModel.getCode());
	}

	public TmaPDTRowIdPrepareInterceptor getPDTRowIdPrepareInterceptor()
	{
		return priceIdPrepareInterceptor;
	}

	public void setPriceIdPrepareInterceptor(final TmaPDTRowIdPrepareInterceptor priceIdPrepareInterceptor)
	{
		this.priceIdPrepareInterceptor = priceIdPrepareInterceptor;
	}
}
