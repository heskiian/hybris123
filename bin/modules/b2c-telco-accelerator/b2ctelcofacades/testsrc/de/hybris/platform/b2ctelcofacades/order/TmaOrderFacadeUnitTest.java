/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.order.impl.DefaultTmaOrderFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaOrderService;
import de.hybris.platform.b2ctelcoservices.services.PrincipalService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


/**
 * Unit tests for {@link DefaultTmaOrderFacade}.
 *
 * @since 2102
 */
@UnitTest
public class TmaOrderFacadeUnitTest
{
	private static final String ORDER_CODE_1 = "00002001";
	private static final String RELATED_PARTY_ID_1 = "selfserviceuser@hybris.com";

	@Mock
	private CustomerAccountService customerAccountService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private AbstractPopulatingConverter<OrderModel, OrderData> orderConverter;
	@Mock
	private AbstractPopulatingConverter<OrderModel, OrderHistoryData> orderHistoryConverter;
	@Mock
	private UserService userService;
	@Mock
	private PrincipalService principalService;
	@Mock
	private TmaOrderService orderService;
	@Mock
	private CheckoutCustomerStrategy defaultCheckoutCustomerStrategy;

	private OrderModel orderModel;

	private ProductData productData;

	private DefaultTmaOrderFacade defaultTmaOrderFacade;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		defaultTmaOrderFacade = new DefaultTmaOrderFacade(principalService, orderService);
		defaultTmaOrderFacade.setCustomerAccountService(customerAccountService);
		defaultTmaOrderFacade.setOrderConverter(orderConverter);
		defaultTmaOrderFacade.setUserService(userService);
		defaultTmaOrderFacade.setBaseStoreService(baseStoreService);
		defaultTmaOrderFacade.setOrderHistoryConverter(orderHistoryConverter);
		defaultTmaOrderFacade.setCheckoutCustomerStrategy(defaultCheckoutCustomerStrategy);

		orderModel = new OrderModel();
		orderModel.setCode(ORDER_CODE_1);
		final AbstractOrderEntryModel entryModel = new AbstractOrderEntryModel();
		final TmaProductOfferingModel productModel = new TmaProductOfferingModel();
		entryModel.setProduct(productModel);
		final List<AbstractOrderEntryModel> entryModelList = new ArrayList<>();
		entryModelList.add(entryModel);
		orderModel.setEntries(entryModelList);

		final OrderData orderData = Mockito.mock(OrderData.class);
		final List<OrderEntryData> listData = new ArrayList<>();
		final OrderEntryData entryData = new OrderEntryData();
		entryData.setProduct(productData);
		listData.add(entryData);
		given(orderData.getEntries()).willReturn(listData);
		given(orderConverter.convert(orderModel)).willReturn(orderData);

		productData = Mockito.mock(ProductData.class);
	}

	@Test
	public void testGetOrderDetailsForCode()
	{
		given(
				customerAccountService.getOrderForCode(Mockito.any(CustomerModel.class), Mockito.anyString(),
						Mockito.any(BaseStoreModel.class))).willReturn(orderModel);
		defaultTmaOrderFacade.getOrderDetails(ORDER_CODE_1, RELATED_PARTY_ID_1);
		verify(orderConverter).convert(orderModel);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetOrderDetailsForInvalidOrderCode()
	{
		given(
				customerAccountService.getOrderForCode(Mockito.any(CustomerModel.class), Mockito.anyString(),
						Mockito.any(BaseStoreModel.class))).willThrow(ModelNotFoundException.class);

		defaultTmaOrderFacade.getOrderDetails(ORDER_CODE_1, RELATED_PARTY_ID_1);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void testGetOrderDetailsForInvalidUserCode()
	{
		given(
				customerAccountService.getOrderForCode(Mockito.any(CustomerModel.class), Mockito.anyString(),
						Mockito.any(BaseStoreModel.class))).willThrow(UnknownIdentifierException.class);

		defaultTmaOrderFacade.getOrderDetails(ORDER_CODE_1, RELATED_PARTY_ID_1);
	}
}
