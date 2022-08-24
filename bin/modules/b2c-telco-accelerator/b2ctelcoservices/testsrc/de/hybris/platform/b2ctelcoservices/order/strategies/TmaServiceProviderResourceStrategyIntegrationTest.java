/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaServiceProviderResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link TmaServiceProviderResourceStrategy} class.
 *
 * @since 2003
 */
@IntegrationTest
public class TmaServiceProviderResourceStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String VALID_PROCESS_TYPE_CODE = "SWITCH_SERVICE_PROVIDER";
	private static final String INVALID_PROCESS_TYPE_CODE = "ACQUISITION";
	private static final String USER = "user1@hybris.com";
	private static final String USER_CART = "user1Cart3";
	private static final String FORMER_SUPPLIER = "Test Former Supplier";
	private static final int IPHONE_ENTRY_NUMBER = 1;
	@Resource
	private TmaServiceProviderResourceStrategy tmaServiceProviderResourceStrategy;
	@Resource
	private UserService userService;
	@Resource
	private CommerceCartService commerceCartService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testSetFormerSupplierToCartEntry() throws CommerceCartModificationException
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel cart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final AbstractOrderEntryModel entry = getEntryByNumber(cart.getEntries(), IPHONE_ENTRY_NUMBER);

		final CommerceCartParameter commerceCartParameter = buildCommerceCartParameter(VALID_PROCESS_TYPE_CODE, FORMER_SUPPLIER);
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		tmaServiceProviderResourceStrategy.updateResource(commerceCartParameter, cartModification);
		Assert.assertNotNull(cartModification.getEntry().getServiceProvider());
	}

	private CommerceCartParameter buildCommerceCartParameter(final String processType, final String serviceProvider)
	{
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		commerceCartParameter.setProcessType(processType);
		commerceCartParameter.setServiceProvider(serviceProvider);
		return commerceCartParameter;
	}

	private AbstractOrderEntryModel getEntryByNumber(final List<AbstractOrderEntryModel> entries, final int entryNumber)
	{
		return entries.stream().filter(entry -> entry.getEntryNumber().equals(entryNumber)).findFirst().orElse(null);
	}
}
