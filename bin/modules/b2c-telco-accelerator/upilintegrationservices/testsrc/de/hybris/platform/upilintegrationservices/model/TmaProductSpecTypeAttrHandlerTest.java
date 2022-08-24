/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ObjectUtils;


/**
 * Test class for {@link TmaProductSpecTypeAttrHandler}.
 * 
 * @since 1911
 */
@UnitTest
public class TmaProductSpecTypeAttrHandlerTest
{
	private TmaProductSpecTypeAttrHandler tmaProductSpecTypeAttrHandler;

	@Before
	public void dynamicAttributeHandlerTest()
	{
		MockitoAnnotations.initMocks(this);
		tmaProductSpecTypeAttrHandler = new TmaProductSpecTypeAttrHandler();
	}

	@Test
	public void subscriptionPricePlanIsNull()
	{
		assertEquals(null, tmaProductSpecTypeAttrHandler.get(null));
	}

	@Test
	public void instanceOfTmaBundledProductOfferingModelTest()
	{

		final TmaProductSpecTypeModel productSpecType = tmaProductSpecTypeAttrHandler.get(setUp(true));
		Assert.assertTrue(!ObjectUtils.isEmpty(productSpecType));
	}

	@Test
	public void instanceOfTmaSimpleProductOfferingModelTest()
	{
		final TmaProductSpecTypeModel productSpecType = tmaProductSpecTypeAttrHandler.get(setUp(false));
		Assert.assertTrue(!ObjectUtils.isEmpty(productSpecType));

	}

	private SubscriptionPricePlanModel setUp(final boolean isBundle)
	{
		final SubscriptionPricePlanModel subscriptionPricePlan = mock(SubscriptionPricePlanModel.class);
		final TmaProductSpecificationModel poSpecification = mock(TmaProductSpecificationModel.class);
		final TmaProductSpecTypeModel productSpecType = mock(TmaProductSpecTypeModel.class);
		if (isBundle)
		{
			final TmaBundledProductOfferingModel product = mock(TmaBundledProductOfferingModel.class);
			given(subscriptionPricePlan.getProduct()).willReturn(product);
			final TmaSimpleProductOfferingModel affectedProduct = mock(TmaSimpleProductOfferingModel.class);
			given(subscriptionPricePlan.getAffectedProductOffering()).willReturn(affectedProduct);
			given(affectedProduct.getProductSpecification()).willReturn(poSpecification);
			given(poSpecification.getType()).willReturn(productSpecType);
		}
		else
		{
			final TmaSimpleProductOfferingModel product = mock(TmaSimpleProductOfferingModel.class);
			given(subscriptionPricePlan.getProduct()).willReturn(product);
			given(product.getProductSpecification()).willReturn(poSpecification);
			given(poSpecification.getType()).willReturn(productSpecType);
		}
		return subscriptionPricePlan;
	}
}
