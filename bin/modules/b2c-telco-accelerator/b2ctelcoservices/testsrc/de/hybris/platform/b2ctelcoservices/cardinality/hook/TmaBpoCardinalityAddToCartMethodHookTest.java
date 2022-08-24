/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.hook;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;


/**
 * Test for {@link TmaBpoCardinalityAddToCartMethodHook}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaBpoCardinalityAddToCartMethodHookTest
{
	private static final String FBPO_CODE = "starterMobileDeal";
	private static final String SPO_CODE = "apple_iphone_6";
	private static final String SPO_CODE_2 = "100_mb";

	@Mock
	private TmaPoService tmaPoService;

	@Mock
	private TmaBpoCardinalityService bpoCardinalityService;

	@InjectMocks
	private TmaBpoCardinalityAddToCartMethodHook bpoCardinalityAddToCartMethodHook;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testValidChildrenBeforeAddToCart()
	{
		final CommerceCartParameter parameter = givenCommerceCartParameterWithValidPos();
		try
		{
			whenBeforeAddToCartMethodHookIsCalled(parameter);
		}
		catch (CommerceCartModificationException exception)
		{
			thenExceptionIsThrown(
					"Not all children of offering with code: " + parameter.getProduct().getCode() + " are part of the request",
					exception.getMessage());
		}
	}

	@Test
	public void testInvalidChildrenBeforeAddToCart() throws CommerceCartModificationException
	{
		final CommerceCartParameter parameter = givenCommerceCartParameterWithInvalidPos();
		try
		{
			whenBeforeAddToCartMethodHookIsCalled(parameter);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (CommerceCartModificationException exception)
		{
			thenExceptionIsThrown(
					"Not all children of product offering with code: " + parameter.getProduct().getCode()
							+ " are part of the request.", exception.getMessage());
		}
	}

	@Test
	public void testValidCardinalityBeforeAddToCart()
	{
		final CommerceCartParameter parameter = givenCommerceCartParameterWithValidCardinality();
		try
		{
			whenBeforeAddToCartMethodHookIsCalled(parameter);
		}
		catch (CommerceCartModificationException exception)
		{
			thenExceptionIsThrown(
					"Cardinality is not fulfilled for children of product with code: " + parameter.getProduct().getCode(),
					exception.getMessage());
		}
	}

	@Test
	public void testInvalidCardinalityBeforeAddToCart()
	{
		final CommerceCartParameter parameter = givenCommerceCartParameterWithInvalidCardinality();
		try
		{
			whenBeforeAddToCartMethodHookIsCalled(parameter);
			Assert.fail("Validation expected to fail, but it didn't.");
		}
		catch (CommerceCartModificationException exception)
		{
			thenExceptionIsThrown(
					"Cardinality is not fulfilled for children of product offering with code: " + parameter.getProduct().getCode(),
					exception.getMessage());
		}
	}

	private TmaSimpleProductOfferingModel createSimpleProductOfferingModel(final String poId)
	{
		final TmaSimpleProductOfferingModel tmaSimpleProductOfferingModel = new TmaSimpleProductOfferingModel();
		tmaSimpleProductOfferingModel.setCode(poId);
		return tmaSimpleProductOfferingModel;
	}

	private TmaFixedBundledProductOfferingModel createFixedBundledProductOfferingModel(final String poId,
			final Set<TmaProductOfferingModel> children)
	{
		final TmaFixedBundledProductOfferingModel tmaFixedBundledProductOfferingModel = new TmaFixedBundledProductOfferingModel();
		tmaFixedBundledProductOfferingModel.setCode(poId);
		tmaFixedBundledProductOfferingModel.setChildren(children);
		return tmaFixedBundledProductOfferingModel;
	}

	private CartModel createCartModel()
	{
		final CartModel cartModel = new CartModel();
		return cartModel;
	}

	private Optional<TmaBundledProdOfferOptionModel> createTmaBundledProdOfferOptionModel(final TmaBundledProductOfferingModel bpo,
			final Integer cardinality)
	{
		final TmaBundledProdOfferOptionModel bpoo = new TmaBundledProdOfferOptionModel();
		bpoo.setBundledProductOffering(bpo);
		bpoo.setDefault(cardinality);
		bpoo.setLowerLimit(cardinality);
		bpoo.setUpperLimit(cardinality);
		return Optional.of(bpoo);
	}

	private CommerceCartParameter createCommerceCartParameter(final TmaProductOfferingModel po, final CartModel cart)
	{
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setProduct(po);
		commerceCartParameter.setProcessType(TmaProcessType.ACQUISITION.getCode());
		commerceCartParameter.setCart(cart);
		return commerceCartParameter;
	}


	private CommerceCartParameter givenCommerceCartParameterWithValidPos()
	{
		final CartModel cartModel = createCartModel();

		final TmaSimpleProductOfferingModel offering = createSimpleProductOfferingModel(SPO_CODE);
		final TmaFixedBundledProductOfferingModel parentBundledOffering = createFixedBundledProductOfferingModel(FBPO_CODE,
				new HashSet<>(Arrays.asList(offering)));

		final CommerceCartParameter parameter = createCommerceCartParameter(parentBundledOffering, cartModel);
		parameter.setQuantity(1L);

		final CommerceCartParameter childParameter = createCommerceCartParameter(offering, cartModel);
		childParameter.setQuantity(1L);
		parameter.setChildren(Collections.singletonList(childParameter));

		final Map<TmaProductOfferingModel, Integer> offeringsWithQuantity = new HashMap<>();
		offeringsWithQuantity.put(offering, 1);

		given((TmaFixedBundledProductOfferingModel) tmaPoService.getPoForCode(parameter.getProduct().getCode()))
				.willReturn(parentBundledOffering);
		given(bpoCardinalityService.verifyCardinality((TmaFixedBundledProductOfferingModel) parameter.getProduct(),
				offeringsWithQuantity)).willReturn(true);
		given(tmaPoService.getBundledProdOfferOptionFor((TmaBundledProductOfferingModel) parameter.getProduct(), offering))
				.willReturn(createTmaBundledProdOfferOptionModel(parentBundledOffering, 1));

		return parameter;
	}

	private CommerceCartParameter givenCommerceCartParameterWithInvalidPos()
	{
		final CartModel cartModel = createCartModel();
		final TmaSimpleProductOfferingModel offering = createSimpleProductOfferingModel(SPO_CODE);
		final TmaFixedBundledProductOfferingModel parentOffering = createFixedBundledProductOfferingModel(FBPO_CODE,
				new HashSet<>(Arrays.asList(offering, createSimpleProductOfferingModel(SPO_CODE_2))));

		final CommerceCartParameter parameter = createCommerceCartParameter(parentOffering, cartModel);

		final CommerceCartParameter childParameter = createCommerceCartParameter(offering, cartModel);
		parameter.setChildren(Collections.singletonList(childParameter));

		given(tmaPoService.getPoForCode(parameter.getProduct().getCode())).willReturn(parentOffering);
		return parameter;
	}

	private CommerceCartParameter givenCommerceCartParameterWithValidCardinality()
	{
		final CartModel cartModel = createCartModel();
		final TmaSimpleProductOfferingModel offering = createSimpleProductOfferingModel(SPO_CODE);
		final TmaFixedBundledProductOfferingModel parentBundledOffering = createFixedBundledProductOfferingModel(FBPO_CODE,
				new HashSet<>(Arrays.asList(offering)));

		final CommerceCartParameter parameter = createCommerceCartParameter(parentBundledOffering, cartModel);
		parameter.setQuantity(1L);

		final CommerceCartParameter childParameter = createCommerceCartParameter(offering, cartModel);
		childParameter.setQuantity(1L);
		parameter.setChildren(Collections.singletonList(childParameter));

		final Map<TmaProductOfferingModel, Integer> offeringsWithQuantity = new HashMap<>();
		offeringsWithQuantity.put(offering, 1);

		given((TmaFixedBundledProductOfferingModel) tmaPoService.getPoForCode(parameter.getProduct().getCode()))
				.willReturn(parentBundledOffering);
		given(bpoCardinalityService.verifyCardinality((TmaFixedBundledProductOfferingModel) parameter.getProduct(),
				offeringsWithQuantity)).willReturn(true);
		given(tmaPoService.getBundledProdOfferOptionFor((TmaBundledProductOfferingModel) parameter.getProduct(), offering))
				.willReturn(createTmaBundledProdOfferOptionModel(parentBundledOffering, 1));
		return parameter;
	}

	private CommerceCartParameter givenCommerceCartParameterWithInvalidCardinality()
	{
		final CartModel cartModel = createCartModel();
		final TmaSimpleProductOfferingModel offering = createSimpleProductOfferingModel(SPO_CODE);
		final TmaFixedBundledProductOfferingModel parentBundledOffering = createFixedBundledProductOfferingModel(FBPO_CODE,
				new HashSet<>(Arrays.asList(offering)));
		final CommerceCartParameter parameter = createCommerceCartParameter(parentBundledOffering, cartModel);
		parameter.setQuantity(1L);
		final CommerceCartParameter childParameter = createCommerceCartParameter(offering, cartModel);
		childParameter.setQuantity(1L);
		parameter.setChildren(Collections.singletonList(childParameter));
		given((TmaFixedBundledProductOfferingModel) tmaPoService.getPoForCode(parameter.getProduct().getCode()))
				.willReturn(parentBundledOffering);
		given(tmaPoService.getBundledProdOfferOptionFor((TmaBundledProductOfferingModel) parameter.getProduct(), offering))
				.willReturn(createTmaBundledProdOfferOptionModel(parentBundledOffering, 3));
		return parameter;
	}

	private void whenBeforeAddToCartMethodHookIsCalled(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		getBpoCardinalityAddToCartMethodHook().beforeAddToCart(parameter);
	}

	private void thenExceptionIsThrown(final String expected, final String errorMessage)
	{
		Assert.assertEquals(expected, errorMessage);
	}

	private TmaBpoCardinalityAddToCartMethodHook getBpoCardinalityAddToCartMethodHook()
	{
		return bpoCardinalityAddToCartMethodHook;
	}
}
