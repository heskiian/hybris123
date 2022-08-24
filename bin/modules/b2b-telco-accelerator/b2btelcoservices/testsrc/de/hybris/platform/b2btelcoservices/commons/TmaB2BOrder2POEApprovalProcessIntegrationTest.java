/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.commons;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.impl.B2BOrder2POEApprovalProcessIntegrationTest;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Replaces {@link B2BOrder2POEApprovalProcessIntegrationTest}
 *
 * @since 2105
 */
@IntegrationTest(replaces = B2BOrder2POEApprovalProcessIntegrationTest.class)
public class TmaB2BOrder2POEApprovalProcessIntegrationTest extends B2BOrder2POEApprovalProcessIntegrationTest
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
	@Ignore("Approval Process not started due to TUA overrides, making this test irrelevant.")
	@Test
	public void shouldStart2POEApprovalProcessAndAssertAproval() throws Exception
	{
		super.shouldStart2POEApprovalProcessAndAssertAproval();
	}

	@Override
	@Ignore("Approval Process not started due to TUA overrides, making this test irrelevant.")
	@Test
	public void shouldStart2POEApprovalProcessForCustomerWithoutApproversAndAssertAssignedToAdmin() throws Exception
	{
		super.shouldStart2POEApprovalProcessForCustomerWithoutApproversAndAssertAssignedToAdmin();
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
