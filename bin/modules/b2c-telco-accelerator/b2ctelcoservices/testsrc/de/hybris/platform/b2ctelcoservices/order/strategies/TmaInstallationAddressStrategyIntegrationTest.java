/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.data.TmaAddressPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaInstallationAddressResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test  for the {@link TmaInstallationAddressResourceStrategy} class.
 *
 * @since 1911
 */
@IntegrationTest
public class TmaInstallationAddressStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String CORRECT_ADDRESS_ID = "12341234";
	private static final String WRONG_ADDRESS_ID = "788867AB";
	private static final String NONEXISTENT_ADDRESS_ID = "78855000";
	private static final String USER_ID = "user1@hybris.com";
	private static final String CART_ID = "user1Cart3";

	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private TmaAddressService addressService;
	@Resource
	private TmaInstallationAddressResourceStrategy tmaInstallationAddressResourceStrategy;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testAddAddressToCartEntry()
	{
		final TmaAddressPlace tmaPlace = new TmaAddressPlace();
		tmaPlace.setRole(TmaPlaceRoleType.INSTALLATION_ADDRESS);
		tmaPlace.setAddress(addressService.findAddress(CORRECT_ADDRESS_ID));
		List<TmaPlace> tmaPlaces = new ArrayList<>();
		tmaPlaces.add(tmaPlace);

		final UserModel user = userService.getUserForUID(USER_ID);
		final CartModel userCart = commerceCartService.getCartForCodeAndUser(CART_ID, user);
		final CartEntryModel entry = (CartEntryModel) userCart.getEntries().iterator().next();
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setPlaces(tmaPlaces);
		tmaInstallationAddressResourceStrategy.updateResource(commerceCartParameter, cartModification);
		Assert.assertEquals(CORRECT_ADDRESS_ID, entry.getInstallationAddress().getId());
	}

	@Test
	public void testValidateCorrectAddress()
	{
		tmaInstallationAddressResourceStrategy.validateResource(buildCommerceCartParameter(CORRECT_ADDRESS_ID));
		Assert.assertTrue(true);
	}

	@Test(expected = ModelNotFoundException.class)
	public void testValidateNonexistentAddress()
	{
		tmaInstallationAddressResourceStrategy.validateResource(buildCommerceCartParameter(NONEXISTENT_ADDRESS_ID));
	}

	@Test
	public void testValidateDuplicateInstallationAddress()
	{
		final TmaAddressPlace tmaPlace = new TmaAddressPlace();
		tmaPlace.setRole(TmaPlaceRoleType.INSTALLATION_ADDRESS);
		tmaPlace.setAddress(addressService.findAddress(CORRECT_ADDRESS_ID));

		final CommerceCartParameter commerceCartParameter = buildCommerceCartParameter(CORRECT_ADDRESS_ID);
		commerceCartParameter.getPlaces().add(tmaPlace);

		final TmaCartValidationResult tmaCartValidationResult = tmaInstallationAddressResourceStrategy
				.validateResource(commerceCartParameter);

		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Found multiple addresses with role: INSTALLATION_ADDRESS", tmaCartValidationResult.getMessage());
	}

	private CommerceCartParameter buildCommerceCartParameter(String addressId)
	{
		final TmaAddressPlace tmaPlace = new TmaAddressPlace();
		tmaPlace.setRole(TmaPlaceRoleType.INSTALLATION_ADDRESS);
		tmaPlace.setAddress(addressService.findAddress(addressId));
		List<TmaPlace> tmaPlaces = new ArrayList<>();
		tmaPlaces.add(tmaPlace);

		final UserModel user = userService.getUserForUID(USER_ID);
		CartModel cart = new CartModel();
		cart.setUser(user);

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setPlaces(tmaPlaces);
		commerceCartParameter.setCart(cart);
		commerceCartParameter.setUser(user);
		return commerceCartParameter;
	}
}
