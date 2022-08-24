/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


/**
 * Unit test class for {@linkplain TmaIdentificationValidateInterceptor}
 */
@UnitTest
public class TmaIdentificationValidateInterceptorTest
{
	private static final String VALID_ID = "111";

	private static final String INVALID_ID = "@11#1%";

	@InjectMocks
	private TmaIdentificationValidateInterceptor tmaIdentificationValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.tmaIdentificationValidateInterceptor = new TmaIdentificationValidateInterceptor();
	}

	@Test
	public void testIdentificationValidate()
	{
		final TmaIdentificationModel identificationModel = new TmaIdentificationModel();
		identificationModel.setIdentificationNumber(VALID_ID);
		identificationModel.setIdentificationType(TmaIdentificationType.DRIVING_LICENSE);

		try
		{
			tmaIdentificationValidateInterceptor.onValidate(identificationModel, interceptorContext);
			Assert.assertTrue("Validation passed", true);
		}
		catch (final InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);
		}
	}

	@Test
	public void testInvalidIdentificationValidate()
	{
		final TmaIdentificationModel identificationModel = new TmaIdentificationModel();
		identificationModel.setIdentificationNumber(INVALID_ID);
		identificationModel.setIdentificationType(TmaIdentificationType.DRIVING_LICENSE);
		try
		{
			tmaIdentificationValidateInterceptor.onValidate(identificationModel, interceptorContext);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (final InterceptorException exception)
		{
			Assert.assertNotNull(exception.getMessage());
		}
	}
}
