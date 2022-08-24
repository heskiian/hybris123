/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaAppointmentResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;


@IntegrationTest
public class TmaAppointmentResourceStrategyIntegrationTest extends ServicelayerTest
{
	private static final String USER = "user1@hybris.com";
	private static final String USER_CART = "user1Cart3";
	private static final String APPOINTMENT_ID = "appointment_1234";
	private static final int IPHONE_ENTRY_NUMBER = 1;
	private static final int INTERNET_PLAN_ENTRY_NR = 2;

	@Resource
	private TmaAppointmentResourceStrategy tmaAppointmentResourceStrategy;
	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testAppointmentIsNotSetOnOrderWithSpo()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final AbstractOrderEntryModel entry = getEntryByNumber(cart.getEntries(), IPHONE_ENTRY_NUMBER);
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		tmaAppointmentResourceStrategy.updateResource(parameter, cartModification);

		assertNull(entry.getAppointmentReference());
	}

	@Test
	public void testAppointmentIsSetOnOrderWithSpo()
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final AbstractOrderEntryModel entry = getEntryByNumber(cart.getEntries(), INTERNET_PLAN_ENTRY_NR);
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProduct(entry.getProduct());
		parameter.setAppointmentId(APPOINTMENT_ID);

		tmaAppointmentResourceStrategy.updateResource(parameter, cartModification);

		assertNotNull(entry.getAppointmentReference());
		assertEquals(APPOINTMENT_ID, entry.getAppointmentReference());
	}

	private AbstractOrderEntryModel getEntryByNumber(List<AbstractOrderEntryModel> entries, int entryNumber)
	{
		return entries.stream().filter(entry -> entry.getEntryNumber().equals(entryNumber)).findFirst().orElse(null);
	}
}
