/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartMergingException;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartMergingStrategyTest;
import de.hybris.platform.commerceservices.order.strategies.EntryMergeStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test for @{B2ctelcoCommerceCartMergingStrategy}
 */
@UnitTest
public class B2ctelcoCommerceCartMergingStrategyTest extends DefaultCommerceCartMergingStrategyTest
{
	@Mock
	private TmaCustomerInventoryService customerInventoryService;

	@Mock
	private TmaCommerceAddToCartStrategy commerceAddToCartStrategy;

	@Mock
	private TmaUpdateCartStrategy tmaUpdateCartStrategy;

	@InjectMocks
	private B2ctelcoCommerceCartMergingStrategy mergingStrategy;

	private static final String ELIGIBILITY_CONTEXT_IS_NOT_ELIGIBLE = "User is not eligible for one or more cart entries";

	@Mock
	private TmaEligibilityContextService tmaEligibilityContextService;

	@Mock
	private EnumerationService enumerationService;

	@Mock
	private CommerceCartService commerceCartService;

	@Mock
	private BaseSiteService baseSiteService;

	@Mock
	private ModelService modelService;

	@Mock
	private UserService userService;

	@Mock
	private CommerceCartCalculationStrategy commerceCartCalculationStrategy;

	@Mock
	private EntryMergeStrategy entryMergeStrategy;

	@Mock
	private EntryGroupService entryGroupService;

	@Mock
	private CartModel toCart;

	private CartModel fromCart;

	private UserModel userModel;

	@Before
	@Override
	public void setUp() throws CommerceCartModificationException
	{
		MockitoAnnotations.initMocks(this);
		mergingStrategy = new B2ctelcoCommerceCartMergingStrategy(
				customerInventoryService, commerceAddToCartStrategy, tmaUpdateCartStrategy);
		super.setUp();
		mergingStrategy.setCommerceCartService(commerceCartService);
		mergingStrategy.setBaseSiteService(baseSiteService);
		mergingStrategy.setModelService(modelService);
		mergingStrategy.setUserService(userService);
		mergingStrategy.setCommerceCartCalculationStrategy(commerceCartCalculationStrategy);
		mergingStrategy.setEntryMergeStrategy(entryMergeStrategy);
		mergingStrategy.setEntryGroupService(entryGroupService);


		userModel = new UserModel();
		userModel.setUid("selfserviceuser2@hybris.com");

		final BaseSiteModel baseSiteModel = mock(BaseSiteModel.class);
		when(baseSiteModel.getName()).thenReturn("site1");
		given(baseSiteService.getCurrentBaseSite()).willReturn(baseSiteModel);

		fromCart = new CartModel();
		fromCart.setGuid("guidFromCart");
		fromCart.setEntries(Collections.emptyList());
		fromCart.setEntryGroups(Collections.emptyList());
		fromCart.setSite(baseSiteModel);

		when(toCart.getSite()).thenReturn(baseSiteModel);
		when(toCart.getGuid()).thenReturn("guidToCart");

		when(commerceCartService.updateQuantityForCartEntry(any(CommerceCartParameter.class))).then(invocationOnMock ->
		{
			final CommerceCartParameter parameter = (CommerceCartParameter) invocationOnMock.getArguments()[0];
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setQuantity(parameter.getQuantity());
			final AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
			entry.setProcessType(TmaProcessType.DEVICE_ONLY);
			entry.setOrder(parameter.getCart());
			entry.setEntryNumber(Integer.valueOf((int) parameter.getEntryNumber()));
			entry.setQuantity(Long.valueOf(parameter.getQuantity()));
			modification.setEntry(entry);
			return modification;
		});
		when(modelService.clone(any(AbstractOrderEntryModel.class), (Class<? extends AbstractOrderEntryModel>) any(Class.class)))
				.thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
	}

	@Test
	public void shouldMergeCartWithSameProcessTypes() throws CommerceCartMergingException
	{
		final AbstractOrderEntryModel fromCartEntry = new AbstractOrderEntryModel();
		fromCartEntry.setEntryNumber(Integer.valueOf(1));
		fromCartEntry.setQuantity(Long.valueOf(1L));
		fromCartEntry.setProcessType(TmaProcessType.DEVICE_ONLY);
		fromCart.setEntries(Collections.singletonList(fromCartEntry));

		final AbstractOrderEntryModel toCartEntry = new AbstractOrderEntryModel();
		toCartEntry.setEntryNumber(Integer.valueOf(1));
		toCartEntry.setQuantity(Long.valueOf(1L));
		toCart.setEntries(Collections.singletonList(toCartEntry));

		final List<CommerceCartModification> modifications = new ArrayList<>();

		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);

		final Set<TmaProcessType> eligibleProcessesForUser = new HashSet<>();
		eligibleProcessesForUser.add(TmaProcessType.DEVICE_ONLY);
		eligibleProcessesForUser.add(TmaProcessType.ACQUISITION);

		Mockito.when(mergingStrategy.getCustomerInventoryService().retrieveProcesses()).thenReturn(eligibleProcessesForUser);

		mergingStrategy.mergeCarts(fromCart, toCart, modifications);
	}

	@Test
	public void shouldCartBeMergedWithDifferentProcessTypes() throws CommerceCartMergingException
	{
		final AbstractOrderEntryModel fromCartEntry = new AbstractOrderEntryModel();
		fromCartEntry.setEntryNumber(Integer.valueOf(1));
		fromCartEntry.setQuantity(Long.valueOf(1L));
		fromCartEntry.setProcessType(TmaProcessType.DEVICE_ONLY);
		fromCart.setEntries(Collections.singletonList(fromCartEntry));

		final AbstractOrderEntryModel toCartEntry = new AbstractOrderEntryModel();
		toCartEntry.setEntryNumber(Integer.valueOf(1));
		toCartEntry.setQuantity(Long.valueOf(1L));
		toCart.setEntries(Collections.singletonList(toCartEntry));

		final List<CommerceCartModification> modifications = new ArrayList<>();

		Mockito.when(userService.getCurrentUser()).thenReturn(userModel);

		final Set<TmaProcessType> eligibleProcessesForUser = new HashSet<>();
		eligibleProcessesForUser.add(TmaProcessType.RETENTION);

		Mockito.when(mergingStrategy.getCustomerInventoryService().retrieveProcesses()).thenReturn(eligibleProcessesForUser);

		thrown.expect(CommerceCartMergingException.class);
		thrown.expectMessage(ELIGIBILITY_CONTEXT_IS_NOT_ELIGIBLE);
		mergingStrategy.mergeCarts(fromCart, toCart, modifications);
	}
}
