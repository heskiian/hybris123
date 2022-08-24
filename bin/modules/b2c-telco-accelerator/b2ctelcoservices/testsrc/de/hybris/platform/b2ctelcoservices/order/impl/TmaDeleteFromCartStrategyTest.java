/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link TmaDeleteFromCartStrategyTest}
 *
 * @since 2102
 */
@IntegrationTest
public class TmaDeleteFromCartStrategyTest extends ServicelayerTest
{
	private static final String USER_ID = "user1@hybris.com";
	private static final String CART_ID = "user1Cart";

	@Resource
	private TmaDeleteFromCartStrategy tmaRemoveEntryStrategy;

	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private UserService userService;

	@Resource
	private CommonI18NService commonI18NService;

	private CartModel cartModel;
	private UserModel user;

	@Before
	public void setUp() throws CommerceCartModificationException, ImpExException
	{
		importCsv("/test/impex/test_deleteHierarchicalCartEntry.impex", "utf-8");

		user = userService.getUserForUID(USER_ID);
		cartModel = commerceCartService.getCartForCodeAndUser(CART_ID, user);
		commonI18NService.setCurrentCurrency(cartModel.getCurrency());
	}

	@Test
	public void shouldDeleteANestedBPO() throws CommerceCartModificationException
	{
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setCart(cartModel);
		commerceCartParameter.setQuantity(0);
		commerceCartParameter.setEntryNumber(1);

		final List<CommerceCartModification> commerceCartModifications = tmaRemoveEntryStrategy.processCartAction(
				Collections.singletonList(commerceCartParameter));

		Assert.assertEquals(3, commerceCartModifications.size());

		final CartModel changedCart = commerceCartService.getCartForCodeAndUser(CART_ID, user);
		Assert.assertEquals(4, changedCart.getEntries().size());

		Assert.assertEquals(1, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("quadPlay")).count());
		Assert.assertEquals(1, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("homeDeal")).count());
		Assert.assertEquals(1, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("internet")).count());
		Assert.assertEquals(1, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("int_100")).count());
		Assert.assertEquals(0, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("mobileDeal")).count());
		Assert.assertEquals(0, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("Apple_iPhone_6")).count());
		Assert.assertEquals(0, changedCart.getEntries().stream()
				.filter(abstractOrderEntryModel -> abstractOrderEntryModel.getProduct().getCode().equals("unlimitedPremiumPlan"))
				.count());
	}
}
