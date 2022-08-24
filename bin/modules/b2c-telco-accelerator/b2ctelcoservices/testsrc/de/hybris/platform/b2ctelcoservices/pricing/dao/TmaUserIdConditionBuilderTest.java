/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveUserPriceGroupsStrategy;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test class for @{TmaProcessTypeConditionBuilder}
 *
 * @since 1907
 */
@UnitTest
public class TmaUserIdConditionBuilderTest
{
	private static final String USER_ID = "admin@gmail.com";
	private static final String TEST_USER_GROUP = "TEST_USER_GROUP";
	private UserPriceGroup priceGroup1;

	@InjectMocks
	TmaUserIdConditionBuilder tmaUserIdConditionBuilder = new TmaUserIdConditionBuilder();

	@Mock
	GenericQuery query;

	@Mock
	TmaRetrieveUserPriceGroupsStrategy userPriceGroupsStrategy;

	@Mock
	UserService userService;

	TmaPriceContext priceContext = new TmaPriceContext();

	UserModel user = new UserModel();

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		final Set<TmaProcessType> processTypes = new HashSet<TmaProcessType>();
		processTypes.add(TmaProcessType.MNP);
		priceContext.setProcessTypes(processTypes);
		user.setUid(USER_ID);
		priceContext.setUser(user);
	}

	@Test
	public void createQueryConditionsTest()
	{
		final Set<UserPriceGroup> userPriceGroups = new HashSet<>();
		priceGroup1 = UserPriceGroup.valueOf(TEST_USER_GROUP);
		userPriceGroups.add(priceGroup1);
		user.setEurope1PriceFactory_UPG(priceGroup1);
		Mockito.when(query.getInitialTypeCode()).thenReturn(PriceRowModel._TYPECODE);
		Mockito.when(userPriceGroupsStrategy.getAllUserPriceGroups(user)).thenReturn(userPriceGroups);
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
		final GenericCondition condition = tmaUserIdConditionBuilder.createQueryConditions(query, priceContext);
		assertThat(condition instanceof GenericConditionList).isTrue();
		final GenericConditionList conditionList = (GenericConditionList) condition;
		assertThat(conditionList.getConditionList()).hasSize(3);
		assertThat(conditionList.getOperator()).isEqualTo(Operator.OR);
		assertThat(conditionList.getConditionList().get(0).getOperator()).isEqualTo(Operator.AND);
	}
}
