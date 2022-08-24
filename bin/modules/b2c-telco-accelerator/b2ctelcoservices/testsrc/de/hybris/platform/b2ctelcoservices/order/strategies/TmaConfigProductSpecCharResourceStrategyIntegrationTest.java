/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.strategies;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.data.TmaProductSpecCharacteristicConfigItem;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl.TmaConfigProductSpecCharResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for the {@link TmaConfigProductSpecCharResourceStrategy} class.
 *
 * @since 1911
 */
@IntegrationTest
public class TmaConfigProductSpecCharResourceStrategyIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER = "user1@hybris.com";
	private static final String USER_CART = "user1Cart";
	private static final String USER_CART2 = "user1Cart2";
	private static final String MSISDN = "msisdn";
	private static final String GB = "gb";
	private static final String SAMPLE_VALUE = "samplevalue";
	private static final String INCH = "inch";
	private static final String SALSA_S = "salsaS";

	@Resource
	private CommerceCartService commerceCartService;
	@Resource
	private UserService userService;
	@Resource
	private TmaConfigProductSpecCharResourceStrategy tmaConfigProductSpecCharResourceStrategy;
	@Resource
	private TmaPoService tmaPoService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_resourcesCart.impex", "utf-8");
	}

	@Test
	public void testValidationWithoutProductOnParameter()
	{
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem(MSISDN, SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setProductSpecCharacteristicConfigs(pscvs);

		final TmaCartValidationResult tmaCartValidationResult = tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);

		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Invalid product.", tmaCartValidationResult.getMessage());
	}

	@Test
	public void testValidPscvValidationWithProductOnParameterWithInvalidChar()
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem("wrong", SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		parameter.setProductSpecCharacteristicConfigs(pscvs);
		parameter.setProduct(tmaPoService.getPoForCode(SALSA_S));

		final TmaCartValidationResult tmaCartValidationResult = tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);

		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Invalid product specification characteristics", tmaCartValidationResult.getMessage());
	}

	@Test
	public void testPscvValidationWithUnconfigurablePscv()
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem("sim_cards", SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		parameter.setProductSpecCharacteristicConfigs(pscvs);
		parameter.setProduct(tmaPoService.getPoForCode("samsung"));

		final TmaCartValidationResult tmaCartValidationResult = tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);

		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Invalid product specification characteristics", tmaCartValidationResult.getMessage());
	}

	@Test
	public void testAddPscvWithNullValue()
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem(MSISDN, null);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		parameter.setProductSpecCharacteristicConfigs(pscvs);
		parameter.setProduct(tmaPoService.getPoForCode(SALSA_S));

		final TmaCartValidationResult tmaCartValidationResult = tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);

		Assert.assertEquals(Boolean.TRUE, tmaCartValidationResult.isValid());
		Assert.assertNull(tmaCartValidationResult.getMessage());
	}

	@Test
	public void testAddPscvWithNullName()
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem(null, SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		parameter.setProductSpecCharacteristicConfigs(pscvs);

		final TmaCartValidationResult tmaCartValidationResult = tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);

		Assert.assertEquals(Boolean.FALSE, tmaCartValidationResult.isValid());
		Assert.assertEquals("Invalid characteristic: Name missing.", tmaCartValidationResult.getMessage());
	}

	@Test
	public void testAddOnePscvToCartEntry() throws CommerceCartModificationException
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel userCart = commerceCartService.getCartForCodeAndUser(USER_CART, user);
		final CartEntryModel entry = (CartEntryModel) userCart.getEntries().iterator().next();
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem(MSISDN, SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv);
		final CommerceCartParameter parameter = new CommerceCartParameter();

		parameter.setProductSpecCharacteristicConfigs(pscvs);
		parameter.setProduct(tmaPoService.getPoForCode(SALSA_S));

		tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);
		tmaConfigProductSpecCharResourceStrategy.updateResource(parameter, cartModification);

		Assert.assertNotNull("Cart CANNOT be null.", userCart);
		Assert.assertNotNull("Entries CANNOT be null.", userCart.getEntries());
		Assert.assertTrue(CollectionUtils.isNotEmpty(userCart.getEntries().get(0).getProductSpecCharacteristicValues()));
		Assert.assertEquals(1, userCart.getEntries().get(0).getProductSpecCharacteristicValues().size());
		Assert.assertEquals(MSISDN, userCart.getEntries().get(0).getProductSpecCharacteristicValues().iterator().next().getName());
	}

	@Test
	public void testAddTwoPscvsToCartEntry() throws CommerceCartModificationException
	{
		final UserModel user = userService.getUserForUID(USER);
		final CartModel userCart = commerceCartService.getCartForCodeAndUser(USER_CART2, user);
		final CartEntryModel entry = (CartEntryModel) userCart.getEntries().iterator().next();
		final CommerceCartModification cartModification = new CommerceCartModification();
		cartModification.setEntry(entry);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		final TmaProductSpecCharacteristicConfigItem pscv = createPscvItem(INCH, SAMPLE_VALUE);
		final TmaProductSpecCharacteristicConfigItem pscv2 = createPscvItem(GB, SAMPLE_VALUE);
		final Set<TmaProductSpecCharacteristicConfigItem> pscvs = createPscvList(pscv, pscv2);

		parameter.setProductSpecCharacteristicConfigs(pscvs);
		parameter.setProduct(entry.getProduct());

		tmaConfigProductSpecCharResourceStrategy.validateResource(parameter);
		tmaConfigProductSpecCharResourceStrategy.updateResource(parameter, cartModification);

		final Set<String> pscvNames = entry.getProductSpecCharacteristicValues().stream()
				.map(TmaAbstractOrderEntryPscvModel::getName).collect(Collectors.toSet());


		Assert.assertNotNull("Cart CANNOT be null.", userCart);
		Assert.assertNotNull("Entries CANNOT be null.", userCart.getEntries());
		Assert.assertEquals(2, pscvNames.size());
		Assert.assertTrue(pscvNames.containsAll(new HashSet<>(Arrays.asList(INCH, GB))));
	}

	protected TmaProductSpecCharacteristicConfigItem createPscvItem(final String name, final String value)
	{
		final TmaProductSpecCharacteristicConfigItem characteristicConfigItem = new TmaProductSpecCharacteristicConfigItem();
		characteristicConfigItem.setName(name);
		characteristicConfigItem.setValue(value);
		return characteristicConfigItem;
	}

	protected Set<TmaProductSpecCharacteristicConfigItem> createPscvList(final TmaProductSpecCharacteristicConfigItem... pscv)
	{
		return new HashSet<>(Arrays.asList(pscv));
	}
}
