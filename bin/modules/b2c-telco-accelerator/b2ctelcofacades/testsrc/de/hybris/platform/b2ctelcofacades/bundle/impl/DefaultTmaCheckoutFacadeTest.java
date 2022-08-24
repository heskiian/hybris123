/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.bundle.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultTmaCheckoutFacadeTest
{
	private static final String PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY = "subscription-only";
	private static final String PREFIX_DELIVERY_MODE_NOT_ONLY_SUBSCRIPTION = "not-only-subscription";
	private SubscriptionTermData subscriptionTermData;
	private PointOfServiceData deliveryPointOfService;
	private ProductData productData;
	private OrderEntryData orderEntryData;
	private List<OrderEntryData> orderEntryDatas;
	private CartData cartdata;

	private DefaultTmaCheckoutFacade defaultTelcoCheckoutFacade;
	@Mock
	private CartModel cartModel;
	@Mock
	private OrderModel orderModel;
	@Mock
	private DefaultCartService defaultCartService;
	@Mock
	private ModelService modelService;
	@Mock
	private Converter<OrderModel, OrderData> converter;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private CartService cartService;
	@Mock
	private DeliveryService deliveryService;
	@Mock
	private AbstractPopulatingConverter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		Registry.activateMasterTenant();
		defaultTelcoCheckoutFacade = new DefaultTmaCheckoutFacade();
		defaultTelcoCheckoutFacade.setCartService(defaultCartService);
		defaultTelcoCheckoutFacade.setModelService(modelService);
		defaultTelcoCheckoutFacade.setOrderConverter(converter);
		defaultTelcoCheckoutFacade.setCartFacade(cartFacade);
		defaultTelcoCheckoutFacade.setCartService(cartService);
		defaultTelcoCheckoutFacade.setDeliveryService(deliveryService);
		defaultTelcoCheckoutFacade.setZoneDeliveryModeConverter(zoneDeliveryModeConverter);
	}

	@Test
	public void testSetCheapestDeliveryModeForCheckoutNull()
	{
		when(cartFacade.hasSessionCart()).thenReturn(true);
		assertFalse(defaultTelcoCheckoutFacade.setCheapestDeliveryModeForCheckout());
	}

	@Test
	public void testSetCheapestDeliveryModeForSubscriptionCheckout()
	{
		setUpData(true, true);
		final List<DeliveryModeModel> deliveryModeModelList = new ArrayList<DeliveryModeModel>();
		final ZoneDeliveryModeModel zoneDeliveryModeModel1 = Mockito.mock(ZoneDeliveryModeModel.class);
		given(zoneDeliveryModeModel1.getCode()).willReturn(PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY);
		deliveryModeModelList.add(zoneDeliveryModeModel1);
		final ZoneDeliveryModeData zoneDeliveryModeData1 = new ZoneDeliveryModeData();
		zoneDeliveryModeData1.setCode(PREFIX_DELIVERY_MODE_SUBSCRIPTION_ONLY);
		given(zoneDeliveryModeConverter.convert(zoneDeliveryModeModel1)).willReturn(zoneDeliveryModeData1);
		final CartModel cart = Mockito.mock(CartModel.class);
		given(cartService.getSessionCart()).willReturn(cart);
		given(deliveryService.getSupportedDeliveryModeListForOrder(cart)).willReturn(deliveryModeModelList);
		given(cartFacade.hasSessionCart()).willReturn(true);
		given(cartFacade.getSessionCart()).willReturn(cartdata);
		assertFalse(defaultTelcoCheckoutFacade.setCheapestDeliveryModeForCheckout());
	}

	@Test
	public void testSetCheapestDeliveryModeForCheckout()
	{
		setUpData(false, true);
		final List<DeliveryModeModel> deliveryModeModelList = new ArrayList<DeliveryModeModel>();
		final ZoneDeliveryModeModel zoneDeliveryModeModel2 = Mockito.mock(ZoneDeliveryModeModel.class);
		given(zoneDeliveryModeModel2.getCode()).willReturn(PREFIX_DELIVERY_MODE_NOT_ONLY_SUBSCRIPTION);
		deliveryModeModelList.add(zoneDeliveryModeModel2);
		final ZoneDeliveryModeData zoneDeliveryModeData2 = new ZoneDeliveryModeData();
		zoneDeliveryModeData2.setCode(PREFIX_DELIVERY_MODE_NOT_ONLY_SUBSCRIPTION);
		given(zoneDeliveryModeConverter.convert(zoneDeliveryModeModel2)).willReturn(zoneDeliveryModeData2);
		final CartModel cart = Mockito.mock(CartModel.class);
		given(cartService.getSessionCart()).willReturn(cart);
		given(deliveryService.getSupportedDeliveryModeListForOrder(cart)).willReturn(deliveryModeModelList);
		given(cartFacade.hasSessionCart()).willReturn(true);
		given(cartFacade.getSessionCart()).willReturn(cartdata);
		assertFalse(defaultTelcoCheckoutFacade.setCheapestDeliveryModeForCheckout());
	}

	@Test
	public void testCartContainsSubscriptionProductsOnly()
	{
		setUpData(false, false);
		assertTrue(defaultTelcoCheckoutFacade.cartContainsSubscriptionProductsOnly(cartdata));
	}

	private void setUpData(final boolean isSubscriptionProduct, final boolean isProduct)
	{
		subscriptionTermData = new SubscriptionTermData();
		deliveryPointOfService = new PointOfServiceData();
		productData = new ProductData();
		orderEntryData = new OrderEntryData();
		if (isProduct)
		{
			orderEntryData.setProduct(productData);
		}
		if (isSubscriptionProduct)
		{
			productData.setSubscriptionTerm(subscriptionTermData);
			orderEntryData.setDeliveryPointOfService(deliveryPointOfService);
		}
		orderEntryDatas = new ArrayList<>();
		orderEntryDatas.add(orderEntryData);
		cartdata = new CartData();
		cartdata.setEntries(orderEntryDatas);
	}
}
