/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.order.TmaCartService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test for {@link TmaPoCartValidationHook}.
 */
@UnitTest
public class TmaPoCartValidationHookTest
{
	@InjectMocks
	private TmaPoCartValidationHook hook;

	private CommerceCartParameter parameter;

	@Mock
	private TmaCartService tmaCartService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		parameter = new CommerceCartParameter();
		parameter.setCart(new CartModel());
	}

	@Test
	public void testAfterAddToCartWithNoBpoGroups()
	{
		final EntryGroup group1 = new EntryGroup();
		group1.setErroneous(Boolean.TRUE);
		group1.setGroupType(GroupType.STANDALONE);

		final ArrayList<CommerceCartModification> modifications = new ArrayList<>();

		Mockito.when(tmaCartService.validateCartForEligibilityRules(parameter.getCart())).thenReturn(false);

		hook.afterValidateCart(parameter, modifications);

		assertTrue(modifications.isEmpty());
	}

	@Test
	public void testAfterAddToCartWithInvalidGroups()
	{
		final EntryGroup group1 = new EntryGroup();
		group1.setErroneous(Boolean.TRUE);
		group1.setGroupType(GroupType.B2CTELCO_BPO);
		group1.setGroupNumber(1);
		final EntryGroup group2 = new EntryGroup();
		group2.setErroneous(Boolean.TRUE);
		group2.setGroupType(GroupType.B2CTELCO_BPO);
		group2.setGroupNumber(2);
		final EntryGroup group3 = new EntryGroup();
		group3.setErroneous(Boolean.FALSE);
		group3.setGroupType(GroupType.B2CTELCO_BPO);
		group3.setGroupNumber(3);

		parameter.getCart().setEntryGroups(Lists.newArrayList(group1, group2, group3));

		final ArrayList<CommerceCartModification> modifications = new ArrayList<>();
		Mockito.when(tmaCartService.validateCartForEligibilityRules(parameter.getCart())).thenReturn(false);
		hook.afterValidateCart(parameter, modifications);

		assertThat(modifications, iterableWithSize(1));
		assertThat(modifications.get(0).getEntryGroupNumbers(), containsInAnyOrder(1, 2));
		assertEquals(CommerceCartModificationStatus.ENTRY_GROUP_ERROR, modifications.get(0).getStatusCode());

	}
}
