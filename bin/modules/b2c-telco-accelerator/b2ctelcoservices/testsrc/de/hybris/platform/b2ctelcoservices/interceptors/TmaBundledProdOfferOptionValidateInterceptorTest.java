/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Unit test of {@link TmaBundledProdOfferOptionValidateInterceptor}.
 *
 * @since 2011
 */
@UnitTest
public class TmaBundledProdOfferOptionValidateInterceptorTest
{
	private static final String PRODUCT_OFFERING_CODE_1 = "PRODUCT_0001";
	private static final String PRODUCT_OFFERING_CODE_2 = "PRODUCT_0002";
	private static final String PRODUCT_OFFERING_CODE_3 = "PRODUCT_0002";
	private static final String PRODUCT_OFFERING_CODE_4 = "PRODUCT_0002";

	@InjectMocks
	private TmaBundledProdOfferOptionValidateInterceptor bundledProdOfferOptionValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
	}

	@Test
	public void testValidateLowerLessThanZeroValue()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, -1, 1, 1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Lower and upper cardinality must be higher or equal to 0", exception.getMessage());
		}
	}

	@Test
	public void testValidateUpperLessThanZeroValue()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 1, -1, 1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Lower and upper cardinality must be higher or equal to 0", exception.getMessage());
		}
	}

	@Test
	public void testValidateDefaultLessThanZeroValue()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 1, 1, -1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Default cardinality must be higher or equal to 0", exception.getMessage());
		}
	}

	@Test
	public void testValidateLowerGreaterThanUpper()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 2, 1, 1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Lower cardinality must be less or equal to upper cardinality", exception.getMessage());
		}
	}

	@Test
	public void testValidateDefaultLessThanLower()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 2, 3, 1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Default cardinality must be higher or equal to lower cardinality", exception.getMessage());
		}
	}

	@Test
	public void testValidateDefaultGreaterThanUpper()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 2, 4, 5);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Default cardinality must be lower or equal to upper cardinality", exception.getMessage());
		}
	}

	@Test
	public void testValidateNullUpperValue()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));

		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 2, null, 5);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Default cardinality must be lower or equal to upper cardinality", exception.getMessage());
		}
	}

	@Test
	public void testWrongExceptionThrown()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = givenBundledProdOfferOptionWithValues(
				bundledProductOffering, productOffering, 2, 3, 4);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to pass, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			Assert.assertNotEquals("Default cardinality must be higher or equal to lower cardinality", exception.getMessage());
		}
	}

	@Test
	public void testValidateCorrectValues()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 0, 1, 0);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.assertTrue("Validation passed", true);
		}
		catch (InterceptorException exception)
		{
			Assert.fail("Validation expected to pass, but it didn't.");
		}
	}

	@Test
	public void testInvalidPoAttachedToTheBpoOption()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, givenProductOffering(PRODUCT_OFFERING_CODE_4), 0,
						1, 0);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("The PO for which a BundledProdOfferOption has been defined is not part of the configured BPO",
					exception.getMessage());
		}
	}

	@Test
	public void testValidateNullPoOnBpoOption()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, null, 0, 1, 0);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("The PO for which a BundledProdOfferOption has been defined is not part of the configured BPO",
					exception.getMessage());
		}
	}

	@Test
	public void testValidateNullPoOnBpo()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaBundledProductOfferingModel bundledProductOffering = givenBundledProductOfferingWithValues(null);
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 0, 1, 0);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("The PO for which a BundledProdOfferOption has been defined is not part of the configured BPO",
					exception.getMessage());
		}
	}

	@Test
	public void testCorrectLimitValuesForFixedBpo()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaFixedBundledProductOfferingModel bundledProductOffering = givenFixedBundledProductOfferingModel(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 1, 1, 1);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
		}
		catch (InterceptorException exception)
		{
			Assert.fail("Validation expected to pass, but it didn't.");
		}
	}

	@Test
	public void testIncorrectLimitValuesForFixedBpo()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaFixedBundledProductOfferingModel bundledProductOffering = givenFixedBundledProductOfferingModel(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 1, 3, 2);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Lower, upper and default cardinality for a Fixed Bundled Product Offering must be equal",
					exception.getMessage());
		}
	}

	@Test
	public void testLimitValuesGreaterThanZeroForFixedBpo()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaFixedBundledProductOfferingModel bundledProductOffering = givenFixedBundledProductOfferingModel(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 2, 2, 2);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
		}
		catch (InterceptorException exception)
		{
			Assert.fail("Validation expected to pass, but it didn't.");
		}
	}

	@Test
	public void testLimitValuesLessThanOneForFixedBpo()
	{
		final TmaProductOfferingModel productOffering = givenProductOffering(PRODUCT_OFFERING_CODE_1);
		final TmaFixedBundledProductOfferingModel bundledProductOffering = givenFixedBundledProductOfferingModel(
				givenProductOfferings(productOffering));
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel =
				givenBundledProdOfferOptionWithValues(bundledProductOffering, productOffering, 0, 0, 0);
		try
		{
			whenValidateInterceptorIsCalled(bundledProdOfferOptionModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Lower, upper and default cardinality for a Fixed Bundled Product Offering must be "
							+ "greater then 0",
					exception.getMessage());
		}
	}

	private void whenValidateInterceptorIsCalled(final TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel)
			throws InterceptorException
	{
		getBundledProdOfferOptionValidateInterceptor().onValidate(tmaBundledProdOfferOptionModel, interceptorContext);
	}

	private void thenExceptionIsThrown(final String expected, final String errorMessage)
	{
		Assert.assertEquals("[" + bundledProdOfferOptionValidateInterceptor + "]:" + expected + "", errorMessage);
	}

	private TmaBundledProdOfferOptionValidateInterceptor getBundledProdOfferOptionValidateInterceptor()
	{
		return bundledProdOfferOptionValidateInterceptor;
	}

	private TmaBundledProdOfferOptionModel givenBundledProdOfferOptionWithValues(
			final TmaBundledProductOfferingModel bundledProductOffering, final TmaProductOfferingModel productOffering,
			final Integer lowerLimit, final Integer upperLimit, final Integer defaultValue)
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOptionModel = new TmaBundledProdOfferOptionModel();
		bundledProdOfferOptionModel.setBundledProductOffering(bundledProductOffering);
		bundledProdOfferOptionModel.setProductOffering(productOffering);
		bundledProdOfferOptionModel.setLowerLimit(lowerLimit);
		bundledProdOfferOptionModel.setUpperLimit(upperLimit);
		bundledProdOfferOptionModel.setDefault(defaultValue);
		return bundledProdOfferOptionModel;
	}

	private TmaProductOfferingModel givenProductOffering(final String poCode)
	{
		final TmaProductOfferingModel productOffering = new TmaProductOfferingModel();
		productOffering.setCode(poCode);
		return productOffering;
	}

	private Set<TmaProductOfferingModel> givenProductOfferings(final TmaProductOfferingModel productOffering)
	{
		return new HashSet<>(Arrays.asList(productOffering,
				givenProductOffering(PRODUCT_OFFERING_CODE_2), givenProductOffering(PRODUCT_OFFERING_CODE_3)));
	}

	private TmaBundledProductOfferingModel givenBundledProductOfferingWithValues(
			final Set<TmaProductOfferingModel> productOfferings)
	{
		final TmaBundledProductOfferingModel bundledProductOffering = new TmaBundledProductOfferingModel();
		bundledProductOffering.setChildren(productOfferings);
		return bundledProductOffering;
	}

	private TmaFixedBundledProductOfferingModel givenFixedBundledProductOfferingModel(
			final Set<TmaProductOfferingModel> productOfferings)
	{
		final TmaFixedBundledProductOfferingModel fixedBundledProductOffering = new TmaFixedBundledProductOfferingModel();
		fixedBundledProductOffering.setChildren(productOfferings);
		return fixedBundledProductOffering;
	}
}
