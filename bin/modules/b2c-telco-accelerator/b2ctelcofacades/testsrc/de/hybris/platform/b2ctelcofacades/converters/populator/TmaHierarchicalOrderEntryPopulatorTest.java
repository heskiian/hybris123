/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Integration test for {@link TmaHierarchicalOrderEntryPopulator}
 *
 * @since 2102
 */
@IntegrationTest
public class TmaHierarchicalOrderEntryPopulatorTest extends ServicelayerTest
{
	private static final String USER = "user1@hybris.com";

	private static final String USER_CART_1 = "user1Cart";

	private static final String MOBILE_DEAL_CODE = "mobileDeal";
	private static final String APPLE_IPHONE_6_CODE = "Apple_iPhone_6";
	private static final String UNLIMITED_PREMIUM_PLAN_CODE = "unlimitedPremiumPlan";

	private OrderEntryData target;

	@Resource
	private TmaHierarchicalOrderEntryPopulator tmaHierarchicalOrderEntryPopulator;

	@Resource
	private UserService userService;

	@Resource
	private CommerceCartService commerceCartService;


	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/b2ctelcofacades/test/testHierarchicalOrder.impex", "utf-8");
		target = new OrderEntryData();
	}

	@Test
	public void testPopulate()
	{
		final CartModel cart = getCartForCodeAndUser(USER_CART_1, USER);

		tmaHierarchicalOrderEntryPopulator.populate(cart.getEntries().get(0), target);

		assertEquals(1, target.getEntries().size());
		assertEquals(1, target.getEntries().stream()
				.filter((OrderEntryData entry) -> entry.getProduct().getCode().equals(MOBILE_DEAL_CODE)).count());
		assertEquals(2, target.getEntries().get(0).getEntries().size());
		assertEquals(1, target.getEntries().get(0).getEntries().stream()
				.filter((OrderEntryData entry) -> entry.getProduct().getCode().equals(APPLE_IPHONE_6_CODE)).count());
		assertEquals(1, target.getEntries().get(0).getEntries().stream()
				.filter((OrderEntryData entry) -> entry.getProduct().getCode().equals(UNLIMITED_PREMIUM_PLAN_CODE)).count());
	}

	private CartModel getCartForCodeAndUser(final String cartId, final String userId)
	{
		final UserModel user = userService.getUserForUID(userId);
		return commerceCartService.getCartForCodeAndUser(cartId, user);
	}
}
