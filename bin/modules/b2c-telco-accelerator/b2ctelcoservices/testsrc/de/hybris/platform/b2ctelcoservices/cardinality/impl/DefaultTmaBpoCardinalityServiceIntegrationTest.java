/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.cardinality.TmaBpoCardinalityService;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultTmaBpoCardinalityServiceIntegrationTest extends ServicelayerTest
{
	private static final String USER = "user1@hybris.com";
	private static final String USER_2 = "user2@hybris.com";

	private static final String USER_NAME_1 = "user1";
	private static final String USER_NAME_2 = "user2";

	private static final String QUAD_PLAY_CODE = "quadPlay";
	private static final String QUAD_PLAY_COMPLEX_CODE = "quadPlay_complex";
	private static final String QUAD_PLAY_TWO_CODE = "quadPlay_two";
	private static final String HOME_DEAL_CODE = "homeDeal";
	private static final String MOBILE_DEAL_CODE = "mobileDeal";
	private static final String INTERNET_CODE = "internet";
	private static final String STARTER_HOME_DEAL_CODE = "starterHomeDeal";
	private static final String APPLE_IPHONE_6_CODE = "Apple_iPhone_6";

	private static final String COMPATIBILITY = "COMPATIBILITY";
	private static final String CARDINALITY = "CARDINALITY";

	private static final String COMPATIBILITY_ERROR_MESSAGE = "Compatibility error message.";
	private static final String CARDINALITY_ERROR_MESSAGE = "Cardinality error message.";
	private static final String CARDINALITY_ERROR_WITH_UPPER_LIMIT = "%s must be between %d and %d.";
	private static final String CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT = "%s must be at least %d.";

	private static final String USER_CART_1 = "Cart";
	private static final String USER_CART_2 = "Cart2";
	private static final String USER_CART_3 = "Cart3";
	private static final String USER_CART_4 = "Cart4";
	private static final String USER_CART_5 = "Cart5";
	private static final String USER_CART_6 = "Cart6";
	private static final String USER_CART_7 = "Cart7";
	private static final String USER_CART_8 = "Cart8";
	private static final String USER_CART_9 = "Cart9";
	private static final String USER_CART_10 = "Cart10";
	private static final String USER_CART_11 = "Cart11";
	private static final String USER_CART_12 = "Cart12";
	private static final String USER_CART_13 = "Cart13";
	private static final String USER_CART_14 = "Cart14";
	private static final String USER_CART_19 = "Cart19";

	@Resource
	private TmaBpoCardinalityService tmaBpoCardinalityService;

	@Resource
	private UserService userService;

	@Resource
	private TmaPoService tmaPoService;

	@Resource
	private CommerceCartService commerceCartService;

	@Resource
	private TmaCartValidationBuilder tmaCartValidationBuilder;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_cardinality.impex", "utf-8");
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityEntryGroup_nullCart()
	{
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, null);

		tmaBpoCardinalityService.verifyCardinality(null, entryGroup);
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityEntryGroup_nullEntryGroup()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_1, USER);

		tmaBpoCardinalityService.verifyCardinality(cart, null);
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityEntryGroup_nullCartAndEmptyEntryGroup()
	{
		final EntryGroup entryGroup = null;
		tmaBpoCardinalityService.verifyCardinality(null, entryGroup);
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithMandatoryAndOptionalItems()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_1, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithOnlyMandatoryItems()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_2, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithMandatorySposFromBposWhichHaveCardinalityRule()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_3, USER);
		final EntryGroup entryGroup = createEntryGroup(1, HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithOptionalItemWithoutMandatory()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_4, USER);
		final EntryGroup entryGroup = createEntryGroup(1, HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithoutMandatoryItemsOfRootBpo()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_5, USER);
		final EntryGroup entryGroup = createEntryGroup(1, MOBILE_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_cartWithoutMandatoryItemFromMandatoryBpo()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_6, USER);
		final EntryGroup entryGroup = createEntryGroup(1, HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_upperLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_7, USER);
		final EntryGroup entryGroup = createEntryGroup(1, MOBILE_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Apple iPhone 6", 0, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_nullUpperLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_8, USER);
		final EntryGroup entryGroup = createEntryGroup(1, INTERNET_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_lowerLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_9, USER);
		final EntryGroup entryGroup = createEntryGroup(1, STARTER_HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_complexBpoHierarchySuccess()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_10, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_COMPLEX_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_complexBpoHierarchyFail()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_11, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_COMPLEX_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 500", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Laptop", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_sameSpoInTwoBposSuccess()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_12, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_TWO_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		assertTrue(CollectionUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart, entryGroup)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityEntryGroup_sameSpoInTwoBposFail()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_13, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_TWO_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validationMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 100", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 1000", 1, 1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityEntry_nullEntry()
	{
		final AbstractOrderEntryModel entry = null;

		tmaBpoCardinalityService.verifyCardinality(entry);
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithMandatoryAndOptionalItems()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_1, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithOnlyMandatoryItems()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_2, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithMandatorySposFromBposWhichHaveCardinalityRule()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_3, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithOptionalItemWithoutMandatory()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_4, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(1))
				.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(2))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(3))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithoutMandatoryItemsOfRootBpo()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_5, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(0))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
	}

	@Test
	public void testVerifyCardinalityEntry_cartWithoutMandatoryItemFromMandatoryBpo()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_6, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(2))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	@Test
	public void testVerifyCardinalityEntry_upperLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_7, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertEquals(2, validationMessages.get(cart.getEntries().get(0)).size());
		assertTrue(validationMessages.get(cart.getEntries().get(0))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(0))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Apple iPhone 6", 0, 1)));
	}

	@Test
	public void testVerifyCardinalityEntry_nullUpperLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_8, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_lowerLimitValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_9, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(0))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	@Test
	public void testVerifyCardinalityEntry_complexBpoHierarchySuccess()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_10, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_complexBpoHierarchyFail()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_11, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(3))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 500", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(4))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Laptop", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(2))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Unlimited Premium Plan", 1, 1)));
	}

	@Test
	public void testVerifyCardinalityEntry_sameSpoInTwoBposSuccess()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_12, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));

		assertTrue(validationMessages.values().stream().allMatch(CollectionUtils::isEmpty));
	}

	@Test
	public void testVerifyCardinalityEntry_sameSpoInTwoBposFail()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_13, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0)).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(1))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 100", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(2))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Fiberlink 1000", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityCart_nullCart()
	{
		final AbstractOrderModel abstractOrder = null;
		tmaBpoCardinalityService.verifyCardinality(abstractOrder);
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_cartWithNullForEntryGroups()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_1, USER);
		cart.setEntryGroups(null);

		assertTrue(MapUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_cartWithEmptyListForEntryGroups()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_1, USER);
		cart.setEntryGroups(Collections.emptyList());

		assertTrue(MapUtils.isEmpty(tmaBpoCardinalityService.verifyCardinality(cart)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_multipleEntryGroups()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_14, USER);
		final List<EntryGroup> entryGroups = new ArrayList<>();

		final EntryGroup entryGroup1 = createEntryGroup(1, HOME_DEAL_CODE, null);
		entryGroups.add(entryGroup1);

		final EntryGroup entryGroup2 = createEntryGroup(2, QUAD_PLAY_CODE, null);
		entryGroups.add(entryGroup2);

		final EntryGroup entryGroup3 = createEntryGroup(3, STARTER_HOME_DEAL_CODE, null);
		entryGroups.add(entryGroup3);

		cart.setEntryGroups(entryGroups);

		final Map<EntryGroup, List<String>> validationMessagesMap = tmaBpoCardinalityService.verifyCardinality(cart);

		final List<String> validationMessages1 = validationMessagesMap.get(entryGroup1);

		assertEquals(3, validationMessages1.size());
		assertTrue(validationMessages1.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertTrue(validationMessages1.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertTrue(validationMessages1.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));

		final List<String> validationMessages2 = validationMessagesMap.get(entryGroup2);
		assertTrue(CollectionUtils.isEmpty(validationMessages2));

		final List<String> validationMessages3 = validationMessagesMap.get(entryGroup3);

		assertEquals(1, validationMessages3.size());
		assertTrue(validationMessages3.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_multipleErrors()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_4, USER);
		final EntryGroup entryGroup = createEntryGroup(1, HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final Map<EntryGroup, List<String>> validationMessagesMap = tmaBpoCardinalityService.verifyCardinality(cart);
		final List<String> validationMessages = validationMessagesMap.get(entryGroup);

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_oneError()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_9, USER);
		final EntryGroup entryGroup = createEntryGroup(1, STARTER_HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final Map<EntryGroup, List<String>> validationMessagesMap = tmaBpoCardinalityService.verifyCardinality(cart);
		final List<String> validationMessages = validationMessagesMap.get(entryGroup);

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testVerifyCardinalityCart_noErrors()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_2, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		tmaBpoCardinalityService.verifyCardinality(cart)
				.forEach(((EntryGroup key, List<String> value) -> assertTrue(CollectionUtils.isEmpty(value))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCardinalityForOrder_nullCart()
	{
		final AbstractOrderModel abstractOrder = null;
		tmaBpoCardinalityService.verifyCardinalityForOrder(abstractOrder);
	}

	@Test
	public void testVerifyCardinalityForOrder_multipleBpos()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_14, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessagesMap = tmaBpoCardinalityService
				.verifyCardinalityForOrder(cart);

		final List<String> internetValidationMessages = validationMessagesMap.get(cart.getEntries().get(1));
		final List<String> ipTvDealValidationMessages = validationMessagesMap.get(cart.getEntries().get(2));
		final List<String> landlineValidationMessages = validationMessagesMap.get(cart.getEntries().get(3));

		assertEquals(1, internetValidationMessages.size());
		assertTrue(internetValidationMessages
				.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertEquals(1, ipTvDealValidationMessages.size());
		assertTrue(
				ipTvDealValidationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertEquals(1, landlineValidationMessages.size());
		assertTrue(landlineValidationMessages
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));

		assertTrue(CollectionUtils.isEmpty(validationMessagesMap.get(cart.getEntries().get(6))));
		assertTrue(CollectionUtils.isEmpty(validationMessagesMap.get(cart.getEntries().get(7))));
		assertTrue(CollectionUtils.isEmpty(validationMessagesMap.get(cart.getEntries().get(8))));
		assertTrue(CollectionUtils.isEmpty(validationMessagesMap.get(cart.getEntries().get(9))));
		assertTrue(CollectionUtils.isEmpty(validationMessagesMap.get(cart.getEntries().get(10))));

		final List<String> starterHomeDealValidationMessages = validationMessagesMap.get(cart.getEntries().get(16));

		assertEquals(1, starterHomeDealValidationMessages.size());
		assertTrue(starterHomeDealValidationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	@Test
	public void testVerifyCardinalityForOrder_multipleErrors()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_4, USER_2);

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinalityForOrder(cart).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(3, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(1))
				.contains(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, "Fiberlink 100", 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(2))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV L", 1, 1)));
		assertTrue(validationMessages.get(cart.getEntries().get(3))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "Landline VOIP", 1, 1)));
	}

	@Test
	public void testVerifyCardinalityForOrder_oneError()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_9, USER_2);
		final EntryGroup entryGroup = createEntryGroup(1, STARTER_HOME_DEAL_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final Map<AbstractOrderEntryModel, List<String>> validationMessages = tmaBpoCardinalityService
				.verifyCardinalityForOrder(cart).entrySet().stream()
				.filter((Map.Entry<AbstractOrderEntryModel, List<String>> map) -> CollectionUtils.isNotEmpty(map.getValue())).collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.get(cart.getEntries().get(0))
				.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
	}

	@Test
	public void testVerifyCardinalityForOrder_noErrors()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_2, USER_2);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, null);
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		tmaBpoCardinalityService.verifyCardinalityForOrder(cart)
				.forEach(((AbstractOrderEntryModel key, List<String> value) -> assertTrue(CollectionUtils.isEmpty(value))));
	}

	@Test
	public void testVerifyCardinalityForOrder_cartWithoutEntries()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_19, USER_2);

		assertTrue(MapUtils.isEmpty(tmaBpoCardinalityService.verifyCardinalityForOrder(cart)));
	}

	@Test
	public void testGetValidationMessage_nullCardinalityRule()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(APPLE_IPHONE_6_CODE);

		assertEquals(Strings.EMPTY, tmaBpoCardinalityService.getValidationMessage(null, productOffering));
	}

	@Test
	public void testGetValidationMessage_nullProductOffering()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(APPLE_IPHONE_6_CODE);
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(productOffering, 1, null);

		assertEquals(Strings.EMPTY, tmaBpoCardinalityService.getValidationMessage(bundledProdOfferOption, null));
	}

	@Test
	public void testGetValidationMessage_nullUpperLimit()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(APPLE_IPHONE_6_CODE);
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(productOffering, 1, null);

		assertEquals(String.format(CARDINALITY_ERROR_WITHOUT_UPPER_LIMIT, productOffering.getName(),
				bundledProdOfferOption.getLowerLimit()),
				tmaBpoCardinalityService.getValidationMessage(bundledProdOfferOption, productOffering));
	}

	@Test
	public void testGetValidationMessage_valueUpperLimit()
	{
		final TmaProductOfferingModel productOffering = tmaPoService.getPoForCode(APPLE_IPHONE_6_CODE);
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(productOffering, 2, 3);

		assertEquals(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, productOffering.getName(),
				bundledProdOfferOption.getLowerLimit(), bundledProdOfferOption.getUpperLimit()),
				tmaBpoCardinalityService.getValidationMessage(bundledProdOfferOption, productOffering));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidate_nullCardinalityRule()
	{
		tmaBpoCardinalityService.validate(null, 2);
	}

	@Test
	public void testValidate_quantityLowerThanLowerLimit()
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(null, 1, 2);

		assertFalse(tmaBpoCardinalityService.validate(bundledProdOfferOption, 0));
	}

	@Test
	public void testValidate_quantityHigherThanUpperLimit()
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(null, 1, 2);

		assertFalse(tmaBpoCardinalityService.validate(bundledProdOfferOption, 3));
	}

	@Test
	public void testValidate_quantityBetweenLowerAndUpperLimit()
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(null, 0, 2);

		assertTrue(tmaBpoCardinalityService.validate(bundledProdOfferOption, 1));
	}

	@Test
	public void testValidate_nullUpperLimit()
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = createCardinalityRule(null, 1, null);

		assertTrue(tmaBpoCardinalityService.validate(bundledProdOfferOption, 4));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testUpdateCardinalityValidationMessage_cleanupValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_9, USER);
		final EntryGroup entryGroup = createEntryGroup(1, STARTER_HOME_DEAL_CODE, getValidationMessages());
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validateCardinalityMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);
		tmaBpoCardinalityService.updateCardinalityValidationMessages(cart, entryGroup, validateCardinalityMessages);

		final Set<String> validationMessages = entryGroup.getValidationMessages().stream().map(TmaCartValidationModel::getMessage)
				.collect(Collectors.toSet());

		assertTrue(entryGroup.getErroneous());
		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
		assertFalse(validationMessages.contains(CARDINALITY_ERROR_MESSAGE));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testUpdateCardinalityValidationMessage_erroneousValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_1 + USER_CART_2, USER);
		final EntryGroup entryGroup = createEntryGroup(1, QUAD_PLAY_CODE, getValidationMessages());
		cart.setEntryGroups(Collections.singletonList(entryGroup));

		final List<String> validateCardinalityMessages = tmaBpoCardinalityService.verifyCardinality(cart, entryGroup);
		tmaBpoCardinalityService.updateCardinalityValidationMessages(cart, entryGroup, validateCardinalityMessages);

		final Set<String> validationMessages = entryGroup.getValidationMessages().stream().map(TmaCartValidationModel::getMessage)
				.collect(Collectors.toSet());

		assertTrue(entryGroup.getErroneous());
		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
	}

	@Test
	public void testUpdateCardinalityValidationMessageOnEntry_cleanupValidation()
	{
		final CartModel cart = getCartForCodeAndUser(USER_NAME_2 + USER_CART_9, USER_2);
		((CartEntryModel) cart.getEntries().get(0)).setCartEntryValidations(getValidationMessages());

		final Map<AbstractOrderEntryModel, List<String>> validateCardinalityMessages = tmaBpoCardinalityService
				.verifyCardinality(cart.getEntries().get(0));
		tmaBpoCardinalityService.updateCardinalityValidationMessages(cart.getEntries().get(0),
				validateCardinalityMessages.get(cart.getEntries().get(0)));

		final Set<String> validationMessages = ((CartEntryModel) cart.getEntries().get(0)).getCartEntryValidations().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet());

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(String.format(CARDINALITY_ERROR_WITH_UPPER_LIMIT, "TV S", 2, 2)));
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
		assertFalse(validationMessages.contains(CARDINALITY_ERROR_MESSAGE));
	}

	private CartModel getCartForCodeAndUser(final String cartId, final String userId)
	{
		final UserModel user = userService.getUserForUID(userId);
		return commerceCartService.getCartForCodeAndUser(cartId, user);
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private EntryGroup createEntryGroup(final int groupNumber, final String bpoCode,
			final Set<TmaCartValidationModel> validationMessages)
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setGroupNumber(groupNumber);
		entryGroup.setExternalReferenceId(bpoCode);
		entryGroup.setErroneous(CollectionUtils.isNotEmpty(validationMessages));
		entryGroup.setValidationMessages(validationMessages);
		return entryGroup;
	}

	private Set<TmaCartValidationModel> getValidationMessages()
	{
		final Set<TmaCartValidationModel> validationMessages = new HashSet<>();

		validationMessages.addAll(tmaCartValidationBuilder
				.createCartValidations(COMPATIBILITY, Collections.singletonList(COMPATIBILITY_ERROR_MESSAGE)));
		validationMessages.addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));

		return validationMessages;
	}

	private TmaBundledProdOfferOptionModel createCardinalityRule(final TmaProductOfferingModel productOffering,
			final Integer lowerLimit, final Integer upperLimit)
	{
		final TmaBundledProdOfferOptionModel bundledProdOfferOption = new TmaBundledProdOfferOptionModel();
		bundledProdOfferOption.setProductOffering(productOffering);
		bundledProdOfferOption.setLowerLimit(lowerLimit);
		bundledProdOfferOption.setUpperLimit(upperLimit);

		return bundledProdOfferOption;
	}
}
