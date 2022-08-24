/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaProcessTypeResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link TmaProcessTypeResourceStrategy} class.
 *
 * @since 2007
 */
@IntegrationTest
public class TmaProcessTypeResourceStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER = "user1@hybris.com";
	private static final String USER_CART = "user1Cart";
	private static final int UNLIMITED_PLAN_PLUS_ENTRY_NUMBER = 1;
	private static final String VALID_PROCESS_TYPE = "ACQUISITION";
	private static final String INVALID_PROCESS_TYPE = "ABC";
	private static final String NOT_ELIGIBLE_PROCESS_TYPE = "RETENTION";

	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private UserService userService;
	@Resource
	private TmaProcessTypeResourceStrategy tmaProcessTypeResourceStrategy;
	@Resource
	private TmaCustomerInventoryService tmaCustomerInventoryService;
	@Resource
	private EnumerationService enumerationService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testUpdateProcessTypeToCartEntry() throws CommerceCartModificationException
	{
		final UserModel user = userService.getUserForUID(USER);
		userService.setCurrentUser(user);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final AbstractOrderEntryModel entry = getEntryByNumber(cart.getEntries(), UNLIMITED_PLAN_PLUS_ENTRY_NUMBER);

		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setProcessType(VALID_PROCESS_TYPE);
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		tmaProcessTypeResourceStrategy.updateResource(commerceCartParameter, cartModification);
		Assert.assertNotNull(entry.getProcessType());
		Assert.assertEquals(VALID_PROCESS_TYPE, entry.getProcessType().getCode());
	}

	@Test
	public void testValidationWithNotEligibleProcessType()
	{
		final UserModel user = userService.getUserForUID(USER);
		userService.setCurrentUser(user);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProcessType(
				enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, NOT_ELIGIBLE_PROCESS_TYPE).getCode());
		parameter.setUser(user);
		final TmaCartValidationResult tmaCartValidationResult = tmaProcessTypeResourceStrategy.validateResource(parameter);
		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals(String.format("Process type %s %s", parameter.getProcessType(), "is not eligible for the given user"),
				tmaCartValidationResult.getMessage());
	}

	@Test
	public void testValidationWithInValidProcessType()
	{
		final UserModel user = userService.getUserForUID(USER);
		userService.setCurrentUser(user);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProcessType(INVALID_PROCESS_TYPE);
		parameter.setUser(user);
		final TmaCartValidationResult tmaCartValidationResult = tmaProcessTypeResourceStrategy.validateResource(parameter);
		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals(String.format("Invalid processType: %s", parameter.getProcessType()),
				tmaCartValidationResult.getMessage());
	}

	private AbstractOrderEntryModel getEntryByNumber(final List<AbstractOrderEntryModel> entries, final int entryNumber)
	{
		return entries.stream().filter(entry -> entry.getEntryNumber().equals(entryNumber)).findFirst().orElse(null);
	}
}
