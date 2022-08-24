/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaSubscribedProductResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.impl.DefaultTmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for {@link TmaSubscribedProductResourceStrategy}
 *
 * @since 1911
 */
@IntegrationTest
public class TmaSubscribedProductResourceStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER_TEST_VALIDATE_ID = "customer001";
	private static final String USER_TEST_UPDATE_ID = "user1@hybris.com ";
	private static final String CART_ID = "user1Cart";
	private static final String SUBSCRIBED_PRODUCT_CORRECT_ID = "1000000001";
	private static final String OFFERING_GSM_ID = "Z_SMART_800";
	private static final String OFFERING_FIBERLINK_ID = "FIBER_900";

	@Resource
	private TmaSubscribedProductResourceStrategy tmaSubscribedProductResourceStrategy;
	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private UserService userService;
	@Resource
	private DefaultTmaPoService tmaPoService;
	@Resource
	private TmaCustomerInventoryService tmaCustomerInventoryService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_customerInventory.impex", "utf-8");
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testUpdateSubscribedProduct()
	{
		final UserModel user = userService.getUserForUID(USER_TEST_UPDATE_ID);
		final CartModel userCart = commerceCartService.getCartForCodeAndUser(CART_ID, user);
		final CartEntryModel entry = (CartEntryModel) userCart.getEntries().iterator().next();
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);
		final CommerceCartParameter commerceCartParameter = buildCartParameter(SUBSCRIBED_PRODUCT_CORRECT_ID, OFFERING_GSM_ID);

		tmaSubscribedProductResourceStrategy.updateResource(commerceCartParameter, cartModification);
		Assert.assertEquals(TmaSubscribedProductAction.UPDATE, entry.getSubscriptionInfo().getSubscribedProductAction());
		Assert.assertEquals(SUBSCRIBED_PRODUCT_CORRECT_ID, entry.getSubscriptionInfo().getSubscribedProductId());

	}

	@Test
	public void testValidateUpdateActionWithCorrectInputs()
	{
		final TmaCartValidationResult testSubscribedProduct = tmaSubscribedProductResourceStrategy
				.validateResource(buildCartParameter(SUBSCRIBED_PRODUCT_CORRECT_ID, OFFERING_GSM_ID));
		Assert.assertTrue("This subscribed product id should be found in the CPI", testSubscribedProduct.isValid());
	}

	@Test
	public void testValidateWithWrongProductSpecType()
	{
		final TmaCartValidationResult testSubscribedProduct = tmaSubscribedProductResourceStrategy
				.validateResource(buildCartParameter(SUBSCRIBED_PRODUCT_CORRECT_ID, OFFERING_FIBERLINK_ID));
		Assert.assertFalse("The subscribed product and offering have different productSpecTypes", testSubscribedProduct.isValid());
	}

	private CommerceCartParameter buildCartParameter(final String subscribedProductId, final String offeringId)
	{
		final UserModel user = userService.getUserForUID(USER_TEST_VALIDATE_ID);
		final CommerceCartParameter commerceCartParameter = new CommerceCartParameter();
		final TmaProductOfferingModel offering = tmaPoService.getPoForCode(offeringId);
		commerceCartParameter.setAction(TmaSubscribedProductAction.UPDATE);
		final CustomerModel customer = (CustomerModel) userService.getUserForUID(USER_TEST_VALIDATE_ID);
		final Optional<TmaSubscribedProductModel> subscribedProduct =
				tmaCustomerInventoryService.getSubscribedProductById(subscribedProductId, customer);
		commerceCartParameter.setSubscribedProduct(subscribedProduct.get());
		commerceCartParameter.setUser(user);
		commerceCartParameter.setProduct(offering);
		return commerceCartParameter;
	}
}
