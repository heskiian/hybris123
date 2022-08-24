/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.hook.TmaCartHookHelper;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.subscription.BillingTimeService;
import de.hybris.platform.subscriptionservices.subscription.SubscriptionCommerceCartService;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;


/**
 * Test for {@link TmaPoAddToCartMethodHook}.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaPoAddToCartMethodHookTest
{
	private static final String BPO_CODE = "BPO_CODE";
	private static final String SPO_CODE = "SPO_CODE";
	private static final String BPO_NAME = "BPO_NAME";

	@Mock
	private TmaPoService tmaPoService;

	@Mock
	private TmaCompatibilityPolicyEngine compatibilityPolicyEngine;

	@Mock
	private TmaCartHookHelper tmaCartHookHelper;


	@Mock
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	@InjectMocks
	private TmaPoAddToCartMethodHook addToCartMethodHook;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = CommerceCartModificationException.class)
	public void testBeforeAddToCartInvalidParentThrowsException() throws CommerceCartModificationException
	{
		final TmaSimpleProductOfferingModel offering = new TmaSimpleProductOfferingModel();
		offering.setCode(SPO_CODE);

		final TmaBundledProductOfferingModel parentOffering = new TmaBundledProductOfferingModel();
		parentOffering.setCode(BPO_CODE);

		final CartModel cart = new CartModel();
		cart.setEntries(Collections.emptyList());

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProduct(offering);
		parameter.setBpoCode(BPO_CODE);
		parameter.setProcessType(TmaProcessType.ACQUISITION.getCode());
		parameter.setCart(cart);

		Mockito.when(tmaPoService.getPoForCode(SPO_CODE)).thenReturn(offering);
		Mockito.when(tmaPoService.getPoForCode(BPO_CODE)).thenReturn(parentOffering);
		Mockito.when(tmaPoService.isValidParent(offering, parentOffering)).thenReturn(false);

		addToCartMethodHook.beforeAddToCart(parameter);
	}

}
