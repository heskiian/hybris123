/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.cardinality.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commerceservices.order.CommerceCartService;
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
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class DefaultCartCardinalityValidationMessagesStrategyIntegrationTest extends ServicelayerTest
{
	private static final String COMPATIBILITY_ERROR_MESSAGE = "Compatibility error message.";
	private static final String CARDINALITY_ERROR_MESSAGE = "Cardinality error message.";

	private static final String CARDINALITY = "CARDINALITY";

	private static final String USER = "user1@hybris.com";
	private static final String USER_2 = "user2@hybris.com";

	private static final String USER_NAME_1 = "user1";
	private static final String USER_NAME_2 = "user2";

	private static final String USER_CART_17 = "Cart17";
	private static final String USER_CART_18 = "Cart18";
	private static final String USER_CART_20 = "Cart20";

	@Resource
	private DefaultCartCardinalityValidationMessagesStrategy tmaCartCardinalityValidationMessagesStrategy;

	@Resource
	private TmaCartValidationBuilder tmaCartValidationBuilder;

	@Resource
	private UserService userService;

	@Resource
	private CommerceCartService commerceCartService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_cardinality.impex", "utf-8");
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testSetValidationMessagesOn_nullMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(entryGroup, null);

		assertNull(entryGroup.getValidationMessages());
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testSetValidationMessagesOn_emptyMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(entryGroup, Collections.emptyList());

		assertNull(entryGroup.getValidationMessages());
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testSetValidationMessagesOn_multipleMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		final List<String> messages = createMessages();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(entryGroup, messages);

		final Set<String> validationMessages = entryGroup.getValidationMessages().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet());

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
		assertTrue(validationMessages.contains(CARDINALITY_ERROR_MESSAGE));
	}

	@Test
	public void testSetValidationMessagesOnEntry_nullMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(cartEntry, null);

		assertNull(cartEntry.getCartEntryValidations());
	}

	@Test
	public void testSetValidationMessagesOnEntry_emptyMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(cartEntry, Collections.emptyList());

		assertNull(cartEntry.getCartEntryValidations());
	}

	@Test
	public void testSetValidationMessagesOnEntry_multipleMessages()
	{
		final CartModel cartModel = getCartForCodeAndUser(USER_NAME_2 + USER_CART_20, USER_2);
		final CartEntryModel cartEntryModel = (CartEntryModel) cartModel.getEntries().get(0);

		final List<String> messages = createMessages();
		tmaCartCardinalityValidationMessagesStrategy.setValidationMessagesOn(cartEntryModel, messages);

		final Set<String> validationMessages = cartEntryModel.getCartEntryValidations().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet());

		assertEquals(2, validationMessages.size());
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
		assertTrue(validationMessages.contains(CARDINALITY_ERROR_MESSAGE));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testCleanupValidationMessagesOn_noMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setErroneous(false);
		entryGroup.setValidationMessages(new HashSet<>());

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(new AbstractOrderModel(), entryGroup);

		assertFalse(entryGroup.getErroneous());
		assertTrue(CollectionUtils.isEmpty(entryGroup.getValidationMessages()));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testCleanupValidationMessagesOn_multipleTypes()
	{
		final CartModel cartModel = getCartForCodeAndUser(USER_NAME_1 + USER_CART_17, USER);
		final EntryGroup entryGroup = cartModel.getEntryGroups().get(0);

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(cartModel, entryGroup);

		final Set<String> validationMessages = entryGroup.getValidationMessages().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet());

		assertTrue(entryGroup.getErroneous());
		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testCleanupValidationMessagesOn_onlyCardinalityMessages()
	{
		final CartModel cartModel = getCartForCodeAndUser(USER_NAME_1 + USER_CART_18, USER);
		final EntryGroup entryGroup = cartModel.getEntryGroups().get(0);

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(cartModel, entryGroup);

		assertFalse(entryGroup.getErroneous());
		assertTrue(CollectionUtils.isEmpty(entryGroup.getValidationMessages()));
	}

	@Test
	public void testCleanupValidationMessagesOnEntry_noMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		cartEntry.setCartEntryValidations(new HashSet<>());

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(cartEntry);

		assertTrue(CollectionUtils.isEmpty(cartEntry.getCartEntryValidations()));
	}

	@Test
	public void testCleanupValidationMessagesOnEntry_multipleTypes()
	{
		final CartModel cartModel = getCartForCodeAndUser(USER_NAME_2 + USER_CART_17, USER_2);
		final CartEntryModel cartEntryModel = (CartEntryModel) cartModel.getEntries().get(0);

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(cartEntryModel);

		final Set<String> validationMessages = cartEntryModel.getCartEntryValidations().stream()
				.map(TmaCartValidationModel::getMessage).collect(Collectors.toSet());

		assertEquals(1, validationMessages.size());
		assertTrue(validationMessages.contains(COMPATIBILITY_ERROR_MESSAGE));
	}

	@Test
	public void testCleanupValidationMessagesOnEntry_onlyCardinalityMessages()
	{
		final CartModel cartModel = getCartForCodeAndUser(USER_NAME_2 + USER_CART_18, USER_2);
		final CartEntryModel cartEntryModel = (CartEntryModel) cartModel.getEntries().get(0);

		tmaCartCardinalityValidationMessagesStrategy.cleanupValidationMessagesOn(cartEntryModel);

		assertTrue(CollectionUtils.isEmpty(cartEntryModel.getCartEntryValidations()));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testShouldUpdateValidationMessages_nullValidationMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		final List<String> messages = createMessages();

		assertTrue(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(entryGroup, messages));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testShouldUpdateValidationMessages_nullErrorMessages()
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setValidationMessages(new HashSet<>());
		entryGroup.getValidationMessages().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));

		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(entryGroup, null));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testShouldUpdateValidationMessages_nullValidationAndErrorMessages()
	{
		final EntryGroup entryGroup = null;
		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(entryGroup, null));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testShouldUpdateValidationMessages_equalLists()
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setValidationMessages(new HashSet<>());
		entryGroup.getValidationMessages().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));
		entryGroup.getValidationMessages().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(COMPATIBILITY_ERROR_MESSAGE)));
		final List<String> messages = createMessages();

		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(entryGroup, messages));
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Test
	public void testShouldUpdateValidationMessages_differentLists()
	{
		final EntryGroup entryGroup = new EntryGroup();
		entryGroup.setValidationMessages(new HashSet<>());
		entryGroup.getValidationMessages().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));
		final List<String> messages = createMessages();

		assertTrue(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(entryGroup, messages));
	}

	@Test
	public void testShouldUpdateValidationMessagesOnEntry_nullValidationMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		final List<String> messages = createMessages();

		assertTrue(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(cartEntry, messages));
	}

	@Test
	public void testShouldUpdateValidationMessagesOnEntry_nullErrorMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		cartEntry.setCartEntryValidations(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));

		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(cartEntry, null));
	}

	@Test
	public void testShouldUpdateValidationMessagesOnEntry_nullValidationAndErrorMessages()
	{
		final CartEntryModel cartEntry = new CartEntryModel();

		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(cartEntry, null));
	}

	@Test
	public void testShouldUpdateValidationMessagesOnEntry_equalLists()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		final List<String> messages = createMessages();

		cartEntry.setCartEntryValidations(new HashSet<>());
		cartEntry.getCartEntryValidations().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));
		cartEntry.getCartEntryValidations().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(COMPATIBILITY_ERROR_MESSAGE)));

		assertFalse(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(cartEntry, messages));
	}

	@Test
	public void testShouldUpdateValidationMessagesOnEntry_differentLists()
	{
		final CartEntryModel cartEntry = new CartEntryModel();
		final List<String> messages = createMessages();

		cartEntry.setCartEntryValidations(new HashSet<>());
		cartEntry.getCartEntryValidations().addAll(
				tmaCartValidationBuilder.createCartValidations(CARDINALITY, Collections.singletonList(CARDINALITY_ERROR_MESSAGE)));

		assertTrue(tmaCartCardinalityValidationMessagesStrategy.shouldUpdateValidationMessages(cartEntry, messages));
	}

	private CartModel getCartForCodeAndUser(final String cartId, final String userId)
	{
		final UserModel user = userService.getUserForUID(userId);
		return commerceCartService.getCartForCodeAndUser(cartId, user);
	}

	private List<String> createMessages()
	{
		final List<String> messages = new ArrayList<>();
		messages.add(COMPATIBILITY_ERROR_MESSAGE);
		messages.add(CARDINALITY_ERROR_MESSAGE);

		return messages;
	}
}
