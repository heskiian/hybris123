/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.configurableguidedselling.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultEntryGroupService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration test for {@link DefaultEntryGroupFacade}
 */
@IntegrationTest
public class DefaultEntryGroupFacadeTest extends ServicelayerTest
{
	@Resource
	private DefaultEntryGroupFacade defaultEntryGroupFacade;

	@Resource
	private ModelService modelService;

	@Resource
	UserService userService;

	@Resource
	private CommonI18NService commonI18NService;

	@Resource
	private DefaultEntryGroupService defaultEntryGroupService;

	@Resource
	private CartService cartService;

	private CartModel cart;
	private UserModel user;
	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		user = userService.getCurrentUser();
		currency = commonI18NService.getCurrentCurrency();
		cart = modelService.create(CartModel.class);
		cart.setCurrency(currency);
		cart.setDate(new Date());
		cart.setUser(user);
		modelService.save(cart);
	}

	@Test
	public void testGetRootEntryGroup()
	{
		final EntryGroup rootGroup = entryGroup(1);
		final EntryGroup entryGroup2 = entryGroup(2);
		final EntryGroup entryGroup3 = entryGroup(3);
		final EntryGroup entryGroup4 = entryGroup(4);
		final EntryGroup entryGroup5 = entryGroup(5);
		final List<EntryGroup> entryGroups = Arrays.asList(rootGroup, entryGroup2, entryGroup3, entryGroup4, entryGroup5);

		rootGroup.setChildren(Arrays.asList(entryGroup2, entryGroup5));
		entryGroup2.setChildren(Arrays.asList(entryGroup3, entryGroup4));
		cart.setEntryGroups(Arrays.asList(rootGroup));
		modelService.saveAll(cart);
		defaultEntryGroupService.forceOrderSaving(cart);
		modelService.refresh(cart);
		cartService.setSessionCart(cart);
		final EntryGroup rootEntryGroup = defaultEntryGroupFacade.getRootEntryGroup(3);
		Assert.assertNotNull("rootEntryGroup should be 1", rootEntryGroup.getGroupNumber());
	}

	@Test
	public void testGetCurrentEntryGroup()
	{
		final EntryGroup rootGroup = entryGroup(1);
		final EntryGroup entryGroup2 = entryGroup(2);
		final EntryGroup entryGroup3 = entryGroup(3);
		final EntryGroup entryGroup4 = entryGroup(4);
		final EntryGroup entryGroup5 = entryGroup(5);
		final List<EntryGroup> entryGroups = Arrays.asList(rootGroup, entryGroup2, entryGroup3, entryGroup4, entryGroup5);

		rootGroup.setChildren(Arrays.asList(entryGroup2, entryGroup5));
		entryGroup2.setChildren(Arrays.asList(entryGroup3, entryGroup4));
		cart.setEntryGroups(Arrays.asList(rootGroup));
		modelService.saveAll(cart);
		defaultEntryGroupService.forceOrderSaving(cart);
		modelService.refresh(cart);
		cartService.setSessionCart(cart);
		final EntryGroup currentEntryGroup = defaultEntryGroupFacade.getCurrentEntryGroup(2);
		Assert.assertNotNull("currentEntryGroup should not be null", currentEntryGroup);
		Assert.assertEquals("currentEntryGroup should be 2", Integer.valueOf(2), currentEntryGroup.getGroupNumber());
	}

	protected EntryGroup entryGroup(final Integer number)
	{
		final EntryGroup result = new EntryGroup();
		result.setGroupNumber(number);
		result.setErroneous(Boolean.FALSE);
		return result;
	}
}






