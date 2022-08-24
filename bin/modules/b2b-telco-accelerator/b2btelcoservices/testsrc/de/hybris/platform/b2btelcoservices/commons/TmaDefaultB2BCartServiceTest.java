/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.commons;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BCartServiceTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Replaces {@link DefaultB2BCartServiceTest}.
 *
 * @since 2015
 */
@IntegrationTest(replaces = DefaultB2BCartServiceTest.class)
public class TmaDefaultB2BCartServiceTest extends DefaultB2BCartServiceTest
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
	public void shouldCreateCartWithComment() throws Exception
	{
		final String userId = "IC CEO";
		final B2BCustomerModel user = login(userId);

		final ProductModel product = productService.getProductForCode("b2bproduct");
		final String cartId = user.getUid();
		final CartModel cart = getCommerceCartService().getCartForCodeAndUser(cartId, user);

		b2bCartService.addNewEntry(cart, product, 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		for (final AbstractOrderEntryModel entry : entries)
		{
			entry.setProcessType(TmaProcessType.ACQUISITION);
		}
		setDefaultCostCenterOnEntries(user, cart);
		final B2BCommentModel b2BCommentModel = modelService.create(B2BCommentModel.class);
		b2BCommentModel.setCode("QuoteRequest");
		b2BCommentModel.setComment("Requesting 5% discount.");
		b2BCommentModel.setModifiedDate(new Date());
		cart.setB2bcomments(Collections.singleton(b2BCommentModel));
		modelService.saveAll();
		Assert.assertEquals(cart.getB2bcomments().iterator().next(), Collections.singleton(b2BCommentModel).iterator().next());
		calculationService.calculate(cart);

		Assert.assertEquals(cart.getB2bcomments().iterator().next(), Collections.singleton(b2BCommentModel).iterator().next());
		final OrderModel order = b2bOrderService.createOrderFromCart(cart);
		this.modelService.save(order);
		Assert.assertFalse("Order.b2bcomments should have been cloned ", order.getB2bcomments().isEmpty());
	}

	@Override
	@Test
	public void shouldCreateCartWithAddress() throws Exception
	{
		final String userId = "IC CEO";
		final B2BCustomerModel user = login(userId);

		final ProductModel product = productService.getProductForCode("b2bproduct");
		final String cartId = user.getUid();
		final CartModel cart = getCommerceCartService().getCartForCodeAndUser(cartId, user);

		b2bCartService.addNewEntry(cart, product, 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();

		for (final AbstractOrderEntryModel entry : entries)
		{
			entry.setProcessType(TmaProcessType.ACQUISITION);
		}
		setDefaultCostCenterOnEntries(user, cart);

		final AddressModel delivery = new AddressModel();
		delivery.setFirstname(user.getName());
		delivery.setLastname(user.getName());
		delivery.setStreetname("Broadway");
		delivery.setStreetnumber("53rd Street");
		delivery.setTown("NYC");
		delivery.setPostalcode("10019");
		delivery.setOwner(user);
		modelService.save(delivery);
		cart.setDeliveryAddress(delivery);
		final String code = b2bOrderService.createOrderFromCart(cart).getCode();
		Assert.assertNotNull(code);

	}

	@Override
	@Test
	public void shouldCreateCartWithAddress1() throws Exception
	{
		final String userId = "IC CEO";
		final B2BCustomerModel user = login(userId);

		final ProductModel product = productService.getProductForCode("b2bproduct");
		final String cartId = user.getUid();
		final CartModel cart = getCommerceCartService().getCartForCodeAndUser(cartId, user);

		b2bCartService.addNewEntry(cart, product , 1, null);
		final List<AbstractOrderEntryModel> entries = cart.getEntries();

		for (final AbstractOrderEntryModel entry : entries)
		{
			entry.setProcessType(TmaProcessType.ACQUISITION);
		}
		setDefaultCostCenterOnEntries(user, cart);

		final AddressModel delivery = new AddressModel();
		delivery.setFirstname(user.getName());
		delivery.setLastname(user.getName());
		delivery.setStreetname("Broadway");
		delivery.setStreetnumber("53rd Street");
		delivery.setTown("NYC");
		delivery.setPostalcode("10019");
		delivery.setOwner(user);
		cart.setDeliveryAddress(delivery);
		final OrderModel order = b2bOrderService.placeOrder(cart, delivery, null, null);
		Assert.assertNotNull(order);

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

	@Override
	protected void setDefaultCostCenterOnEntries(final B2BCustomerModel user, final CartModel cart)
	{
		final List<B2BCostCenterModel> costCenters = b2bCostCenterService.getCostCentersForUnitBranch(user, cart.getCurrency());
		Assert.assertFalse(costCenters.isEmpty());
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : entries)
		{
			abstractOrderEntryModel.setCostCenter(costCenters.get(0));

			this.modelService.save(abstractOrderEntryModel);
		}
	}

	protected CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}
}
