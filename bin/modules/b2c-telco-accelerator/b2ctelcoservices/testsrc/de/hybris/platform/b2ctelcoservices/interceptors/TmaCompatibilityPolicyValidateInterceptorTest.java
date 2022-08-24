/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Unit test for {@link TmaCompatibilityPolicyValidateInterceptor}.
 *
 * @since 1810
 */
@UnitTest
public class TmaCompatibilityPolicyValidateInterceptorTest
{
	private TmaCompatibilityPolicyValidateInterceptor tmaCompatibilityPolicyValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
		this.tmaCompatibilityPolicyValidateInterceptor = new TmaCompatibilityPolicyValidateInterceptor();
	}

	@Test
	public void testValidateOnlineFromAndOnlineToNullDates()
	{
		final TmaCompatibilityPolicyModel compatibilityPolicyModel = givenCompatibilityPolicyWithOnlineValues(null, null);
		try
		{
			getTmaCompatibilityPolicyValidateInterceptor().onValidate(compatibilityPolicyModel, interceptorContext);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);

		}
	}

	@Test
	public void testValidateOnlineFromBeforeOnlineTo()
	{
		final DateTime onlineFrom = new DateTime(2018, 05, 01, 0, 0, 0);
		final DateTime onlineTo = new DateTime(2019, 05, 01, 0, 0, 0);

		final TmaCompatibilityPolicyModel compatibilityPolicyModel = givenCompatibilityPolicyWithOnlineValues(onlineFrom.toDate(),
				onlineTo.toDate());
		try
		{
			getTmaCompatibilityPolicyValidateInterceptor().onValidate(compatibilityPolicyModel, interceptorContext);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);

		}
	}

	@Test
	public void testValidateOnlineFromAfterOnlineTo()
	{
		final DateTime onlineFrom = new DateTime(2019, 05, 01, 0, 0, 0);
		final DateTime onlineTo = new DateTime(2018, 05, 01, 0, 0, 0);

		final TmaCompatibilityPolicyModel compatibilityPolicyModel = givenCompatibilityPolicyWithOnlineValues(onlineFrom.toDate(),
				onlineTo.toDate());
		try
		{
			getTmaCompatibilityPolicyValidateInterceptor().onValidate(compatibilityPolicyModel, interceptorContext);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			Assert.assertEquals("[" + getTmaCompatibilityPolicyValidateInterceptor() + "]:" + "OnlineFrom cannot be after OnlineTo"
					+ " date.", exception.getMessage());

		}
	}

	private TmaCompatibilityPolicyModel givenCompatibilityPolicyWithOnlineValues(final Date onlineFrom, final Date onlineTo)
	{
		TmaCompatibilityPolicyModel compatibilityPolicyModel = new TmaCompatibilityPolicyModel();
		compatibilityPolicyModel.setCode("test_policy_model_code");
		compatibilityPolicyModel.setOnlineFrom(onlineFrom);
		compatibilityPolicyModel.setOnlineTo(onlineTo);
		return compatibilityPolicyModel;
	}

	protected TmaCompatibilityPolicyValidateInterceptor getTmaCompatibilityPolicyValidateInterceptor()
	{
		return tmaCompatibilityPolicyValidateInterceptor;
	}

	public void setTmaCompatibilityPolicyValidateInterceptor(
			TmaCompatibilityPolicyValidateInterceptor tmaCompatibilityPolicyValidateInterceptor)
	{
		this.tmaCompatibilityPolicyValidateInterceptor = tmaCompatibilityPolicyValidateInterceptor;
	}
}
