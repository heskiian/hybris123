/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.hook.TmaCartHookHelper;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.EntryGroupService;

import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;



/**
 * Test for {@link TmaPoUpdateCartEntryHook}.
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class TmaPoUpdateCartEntryHookTest
{
	@Mock
	private EntryGroupService entryGroupService;

	@Mock
	private TmaCompatibilityPolicyEngine compatibilityPoliciesEngine;

	@Mock
	private TmaCartHookHelper tmaCartHookHelper;

	@Mock
	private TmaAbstractOrderEntryService abstractOrderEntryService;

	@InjectMocks
	private TmaPoUpdateCartEntryHook hook;

	private CommerceCartParameter parameter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		parameter = new CommerceCartParameter();
	}

	@Test
	public void testSkipAfterUpdateCartEntryWithoutBpoGroup()
	{
		final CartModel cart = new CartModel();

		final UserModel user = new UserModel();
		cart.setUser(user);

		final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
		entry.setEntryNumber(1);
		cart.setEntries(Collections.singletonList(entry));

		final TmaSimpleProductOfferingModel simpleProductOfferingModel = new TmaSimpleProductOfferingModel();

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setCart(cart);
		parameter.setEntryNumber(1L);
		parameter.setQuantity(0L);
		parameter.setProduct(simpleProductOfferingModel);

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setEntry(entry);

		hook.afterUpdateCartEntry(parameter, modification);

		verify(compatibilityPoliciesEngine, never()).verifyCompatibilityPolicies(entry, user);
	}

	@Test
	public void testRunAfterUpdateCartEntryWithBpoGroup()
	{
		final CartModel cart = new CartModel();

		final UserModel user = new UserModel();
		cart.setUser(user);

		parameter.setCart(cart);

		final TmaSimpleProductOfferingModel simpleProductOfferingModel = new TmaSimpleProductOfferingModel();
		parameter.setProduct(simpleProductOfferingModel);

		final TmaBundledProductOfferingModel bundledProductOffering = new TmaBundledProductOfferingModel();

		final AbstractOrderEntryModel bpoEntry = new AbstractOrderEntryModel();
		bpoEntry.setEntryNumber(0);
		bpoEntry.setOrder(cart);
		bpoEntry.setProduct(bundledProductOffering);

		final AbstractOrderEntryModel spoEntry = new AbstractOrderEntryModel();
		spoEntry.setEntryNumber(1);
		spoEntry.setOrder(cart);
		spoEntry.setProduct(simpleProductOfferingModel);
		spoEntry.setBpo(bundledProductOffering);

		bpoEntry.setChildEntries(Collections.singleton(spoEntry));
		spoEntry.setMasterEntry(bpoEntry);

		cart.setEntries(Lists.newArrayList(bpoEntry, spoEntry));

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setEntry(spoEntry);
		modification.setParentEntry(bpoEntry);

		given(abstractOrderEntryService.getRootEntry(bpoEntry)).willReturn(bpoEntry);
		doNothing().when(tmaCartHookHelper).recalculateEntries(bpoEntry);
		hook.afterUpdateCartEntry(parameter, modification);

		verify(compatibilityPoliciesEngine, times(1)).verifyCompatibilityPolicies(bpoEntry, user);

	}

	@Test
	public void testRunAfterUpdateCartEntryForStandalonePo()
	{
		final CartModel cart = new CartModel();
		parameter.setCart(cart);

		final TmaSimpleProductOfferingModel simpleProductOfferingModel = new TmaSimpleProductOfferingModel();
		parameter.setProduct(simpleProductOfferingModel);

		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(1);
		entry1.setOrder(cart);

		cart.setEntries(Lists.newArrayList(entry1));

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setEntry(entry1);

		hook.afterUpdateCartEntry(parameter, modification);

		verify(compatibilityPoliciesEngine, times(1)).verifyCompatibilityPoliciesForStandaloneProducts(cart);
	}

}
