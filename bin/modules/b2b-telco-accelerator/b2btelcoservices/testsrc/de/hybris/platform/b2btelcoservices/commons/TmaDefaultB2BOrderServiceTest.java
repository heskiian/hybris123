/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.commons;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BOrderServiceTest;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Replaces {@link DefaultB2BOrderServiceTest}
 *
 * @since 2105
 */
@IntegrationTest(replaces = DefaultB2BOrderServiceTest.class)
public class TmaDefaultB2BOrderServiceTest extends DefaultB2BOrderServiceTest
{
	private static final String SEPARATOR = " ";

	@Resource
	private CommerceCartService commerceCartService;

	@Override
	@Before
	public void before() throws Exception
	{
		super.before();
		importCsv("/b2btelcoservices/test/deliverymode.csv", "UTF-8");
		importCsv("/b2btelcoservices/test/policy.csv", "UTF-8");
		importCsv("/b2btelcoservices/test/testData.impex", "UTF-8");
	}

	@Override
	@Test
	public void shouldFindApprovedOrders() throws Exception
	{
		final String userId = "IC CEO";
		final OrderStatus status = OrderStatus.APPROVED;
		final OrderModel order = createOrderWithStatus(userId, status);
		final UserModel user = order.getUser();
		final Collection<OrderModel> orders = b2bOrderService.getApprovedOrders(user);
		Assert.assertFalse(orders.isEmpty());
	}

	@Override
	@Test
	public void shouldFindErroredOrders() throws Exception
	{
		final String userId = "IC CEO";
		final OrderStatus status = OrderStatus.B2B_PROCESSING_ERROR;
		final OrderModel order = createOrderWithStatus(userId, status);
		final UserModel user = order.getUser();
		final Collection<OrderModel> orders = b2bOrderService.getErroredOrders(user);
		Assert.assertFalse(orders.isEmpty());
	}

	@Override
	@Test
	public void shouldFindPendingApprovalOrders() throws Exception
	{
		final String userId = "IC CEO";
		final OrderStatus status = OrderStatus.PENDING_APPROVAL;
		final OrderModel order = createOrderWithStatus(userId, status);
		final UserModel user = order.getUser();
		final Collection<OrderModel> orders = b2bOrderService.getPendingApprovalOrders(user);
		Assert.assertFalse(orders.isEmpty());
	}

	@Override
	@Test
	public void shouldFindRejectedOrders() throws Exception
	{
		final String userId = "IC CEO";
		final OrderStatus status = OrderStatus.REJECTED;
		final OrderModel order = createOrderWithStatus(userId, status);
		final UserModel user = order.getUser();
		final Collection<OrderModel> orders = b2bOrderService.getRejectedOrders(user);
		Assert.assertFalse(orders.isEmpty());
	}

	@Override
	public OrderModel createOrder(final UserModel user, final int quantity, final OrderStatus status, final ProductModel product)
			throws InvalidCartException
	{
		final String cartId = user.getUid() + SEPARATOR + quantity + SEPARATOR + product.getCode();
		final CartModel cart = getCommerceCartService().getCartForCodeAndUser(cartId, user);

		setDefaultCostCenterOnEntries((B2BCustomerModel) user, cart);
		cart.setStatus(status);

		final B2BCommentModel b2BCommentModel = modelService.create(B2BCommentModel.class);
		b2BCommentModel.setCode("QuoteRequest");
		b2BCommentModel.setComment("Requesting 5% discount.");
		b2BCommentModel.setModifiedDate(new Date());
		this.modelService.save(b2BCommentModel);
		cart.setB2bcomments(Collections.singleton(b2BCommentModel));

		modelService.save(cart);

		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cart);
		parameter.setAddress(cart.getDeliveryAddress());
		parameter.setIsDeliveryAddress(true);
		final CommerceOrderResult result = commerceCheckoutService.placeOrder(parameter);

		return result.getOrder();
	}

	private OrderModel createOrderWithStatus(final String userId, final OrderStatus status) throws InvalidCartException
	{
		final B2BCustomerModel user = login(userId);

		final long quantity = 1;
		final String cartId = user.getUid() + SEPARATOR + quantity + SEPARATOR + "b2bproduct";
		final CartModel cart = commerceCartService.getCartForCodeAndUser(cartId, user);

		final OrderModel orderModel = b2bOrderService.placeOrder(cart, null, null, null);
		orderModel.setStatus(status);
		modelService.save(orderModel);

		return orderModel;
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}
}
