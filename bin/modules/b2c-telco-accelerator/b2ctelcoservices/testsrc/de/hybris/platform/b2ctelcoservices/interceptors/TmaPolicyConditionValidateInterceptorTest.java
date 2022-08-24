/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Unit test for {@link TmaPolicyConditionValidateInterceptor}.
 *
 * @since 1810
 */
@UnitTest
public class TmaPolicyConditionValidateInterceptorTest
{
	@InjectMocks
	private TmaPolicyConditionValidateInterceptor policyConditionValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
	}

	@Test
	public void testPolicyConditionCreation()
	{
		final TmaPolicyConditionModel policyConditionModel = givenPolicyConditionModel(TmaPolicyConditionModel._TYPECODE);
		try
		{
			whenValidateInterceptorIsCalled(policyConditionModel);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown(
					MessageFormat.format("{0} instances cannot be created. Define instead {1} instances.", TmaPolicyConditionModel._TYPECODE,
							TmaAtomicPolicyConditionModel._TYPECODE), exception.getMessage());
		}
	}

	@Test
	public void testAtomicPolicyConditonCreation()
	{
		final TmaPolicyConditionModel policyConditionModel = givenPolicyConditionModel(TmaAtomicPolicyConditionModel._TYPECODE);
		try
		{
			whenValidateInterceptorIsCalled(policyConditionModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);
		}
	}

	@Test
	public void testCompositePolicyConditonCreation()
	{
		final TmaPolicyConditionModel policyConditionModel = givenPolicyConditionModel(TmaCompositePolicyConditionModel._TYPECODE);
		try
		{
			whenValidateInterceptorIsCalled(policyConditionModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);
		}
	}


	private TmaPolicyConditionModel givenPolicyConditionModel(String typecode)
	{
		switch (typecode)
		{
			case TmaAtomicPolicyConditionModel._TYPECODE:
				return new TmaAtomicPolicyConditionModel();

			case TmaCompositePolicyConditionModel._TYPECODE:
				return new TmaCompositePolicyConditionModel();

			default:
				return new TmaPolicyConditionModel();
		}
	}


	private void whenValidateInterceptorIsCalled(TmaPolicyConditionModel policyConditionModel) throws InterceptorException
	{
		getPolicyConditionValidateInterceptor().onValidate(policyConditionModel, interceptorContext);
	}

	private void thenExceptionIsThrown(String expected, String errorMessage)
	{
		Assert.assertEquals("[" + getPolicyConditionValidateInterceptor() + "]:" + expected + "", errorMessage);
	}

	protected TmaPolicyConditionValidateInterceptor getPolicyConditionValidateInterceptor()
	{
		return policyConditionValidateInterceptor;
	}
}
