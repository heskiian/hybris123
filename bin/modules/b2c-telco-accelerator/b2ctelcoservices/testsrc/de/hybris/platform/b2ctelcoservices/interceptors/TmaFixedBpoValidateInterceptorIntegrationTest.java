/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;


/**
 * Test class for {@link TmaFixedBpoValidateInterceptor}
 *
 * @since 2105
 */
@IntegrationTest
public class TmaFixedBpoValidateInterceptorIntegrationTest extends ServicelayerTest
{
	private static final String BPO_ID = "mobileDeal";
	private static final String FIXED_BPO_ID = "fixed quad play";
	private static final String SIMPLE_PO_ID = "iphone 3gs";

	@Resource
	private TmaFixedBpoValidateInterceptor tmaFixedBpoValidateInterceptor;

	@Mock
	private InterceptorContext interceptorContext;

	@Before
	public void setUp()
	{
		initMocks(this);
	}

	@Test
	public void testValidChild()
	{
		final TmaFixedBundledProductOfferingModel tmaFixedBundledProductOfferingModel = createFixedBundledProductOfferingModel(
				FIXED_BPO_ID, new HashSet<>(Arrays.asList(createSimpleProductOfferingModel(SIMPLE_PO_ID),
						createFixedBundledProductOfferingModel(FIXED_BPO_ID,
								new HashSet<>(Arrays.asList(createSimpleProductOfferingModel(SIMPLE_PO_ID)))))));
		try
		{
			whenValidateInterceptorIsCalled(tmaFixedBundledProductOfferingModel);
		}
		catch (InterceptorException exception)
		{
			Assert.fail("Validation expected to pass, but it didn't");
		}
	}

	@Test
	public void testInvalidChild()
	{
		final TmaFixedBundledProductOfferingModel tmaFixedBundledProductOfferingModel = createFixedBundledProductOfferingModel(
				FIXED_BPO_ID, new HashSet<>(
						Arrays.asList(createSimpleProductOfferingModel(SIMPLE_PO_ID), createBundledProductOfferingModel(BPO_ID),
								createFixedBundledProductOfferingModel(FIXED_BPO_ID, new HashSet<>(
										Arrays.asList(createSimpleProductOfferingModel(SIMPLE_PO_ID),
												createBundledProductOfferingModel(BPO_ID)))))));
		try
		{
			whenValidateInterceptorIsCalled(tmaFixedBundledProductOfferingModel);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (InterceptorException exception)
		{
			thenExceptionIsThrown("Invalid child attached to Fixed Bundle Product Offering. It must be a Simple Product "
					+ "Offering or a Fixed Bundle Product Offering", exception.getMessage());
		}
	}

	private void whenValidateInterceptorIsCalled(final TmaFixedBundledProductOfferingModel tmaFixedBundledProductOfferingModel)
			throws InterceptorException
	{
		getTmaFixedBpoValidateInterceptor().onValidate(tmaFixedBundledProductOfferingModel, interceptorContext);
	}

	private TmaFixedBundledProductOfferingModel createFixedBundledProductOfferingModel(final String poId,
			final Set<TmaProductOfferingModel> children)
	{
		final TmaFixedBundledProductOfferingModel tmaFixedBundledProductOfferingModel = new TmaFixedBundledProductOfferingModel();
		tmaFixedBundledProductOfferingModel.setChildren(children);
		tmaFixedBundledProductOfferingModel.setCode(poId);
		return tmaFixedBundledProductOfferingModel;
	}

	private TmaSimpleProductOfferingModel createSimpleProductOfferingModel(final String poId)
	{
		final TmaSimpleProductOfferingModel tmaSimpleProductOfferingModel = new TmaSimpleProductOfferingModel();
		tmaSimpleProductOfferingModel.setCode(poId);
		return tmaSimpleProductOfferingModel;
	}

	private TmaBundledProductOfferingModel createBundledProductOfferingModel(final String poId)
	{
		final TmaBundledProductOfferingModel tmaBundledProductOfferingModel = new TmaBundledProductOfferingModel();
		tmaBundledProductOfferingModel.setCode(poId);
		return tmaBundledProductOfferingModel;
	}

	private void thenExceptionIsThrown(final String expected, final String errorMessage)
	{
		Assert.assertEquals("[" + tmaFixedBpoValidateInterceptor + "]:" + expected + "", errorMessage);
	}

	protected TmaFixedBpoValidateInterceptor getTmaFixedBpoValidateInterceptor()
	{
		return tmaFixedBpoValidateInterceptor;
	}
}
