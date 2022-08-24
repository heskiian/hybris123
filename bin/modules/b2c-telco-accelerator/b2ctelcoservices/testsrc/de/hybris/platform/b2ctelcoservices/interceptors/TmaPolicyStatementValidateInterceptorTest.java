/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.type.TypeService;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Unit test for {@link TmaPolicyStatementValidateInterceptor}.
 *
 * @since 6.7
 */
@UnitTest
public class TmaPolicyStatementValidateInterceptorTest
{
	private static final String POLICY_STATEMENT_CODE = "ipTvDeal";

	@InjectMocks
	private TmaPolicyStatementValidateInterceptor policyStatementValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Mock
	private TypeService typeService;

	@Before
	public void setUp()
	{
		initMocks(this);
	}

	@Test
	public void testValidateNullMinValue()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(null, null, false);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown(MessageFormat
					.format("A minimum cardinality value is missing for {0} policy rule and it needs to be configured",
							policyStatementModel.getCode()), exception.getMessage());
		}
	}

	@Test
	public void testValidateMinLessThanZeroValue()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(-2, 0, true);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Min or max cardinality values cannot be less than 0.", exception.getMessage());
		}
	}

	@Test
	public void testValidateMaxLessThanZeroValue()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(2, -3, true);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Min or max cardinality values cannot be less than 0.", exception.getMessage());
		}
	}

	@Test
	public void testValidateInvalidMaxValue()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(2, 1, true);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation expected to fail, but it didn't.", false);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Min cardinality value can take values between 0 and max cardinality value",
					exception.getMessage());
		}
	}

	@Test
	public void testValidateCorrectValues()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(1, 3, true);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);
		}
	}

	@Test
	public void testValidateCorrectEmptyMaxValues()
	{
		TmaPolicyStatementModel policyStatementModel = givenPolicyStatementWithValues(1, null, true);
		try
		{
			whenValidateInterceptorIsCalled(policyStatementModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.assertTrue("Validation expected to pass, but it didn't.", false);
		}
	}

	private TmaPolicyStatementModel givenPolicyStatementWithValues(Integer min, Integer max, boolean isMinOptional)
	{
		final AttributeDescriptorModel attributeDescriptorModel = new AttributeDescriptorModel();
		attributeDescriptorModel.setOptional(isMinOptional);
		given(typeService.getAttributeDescriptor(anyString(), anyString())).willReturn(attributeDescriptorModel);

		final TmaPolicyStatementModel policyStatementModel = new TmaPolicyStatementModel();
		policyStatementModel.setCode(POLICY_STATEMENT_CODE);
		policyStatementModel.setMin(min);
		policyStatementModel.setMax(max);
		return policyStatementModel;
	}

	private void whenValidateInterceptorIsCalled(TmaPolicyStatementModel policyStatementModel) throws InterceptorException
	{
		getPolicyStatementValidateInterceptor().onValidate(policyStatementModel, interceptorContext);
	}

	private void thenExceptionIsThrown(String expected, String errorMessage)
	{
		Assert.assertEquals("[" + policyStatementValidateInterceptor + "]:" + expected + "", errorMessage);
	}

	protected TmaPolicyStatementValidateInterceptor getPolicyStatementValidateInterceptor()
	{
		return policyStatementValidateInterceptor;
	}
}
