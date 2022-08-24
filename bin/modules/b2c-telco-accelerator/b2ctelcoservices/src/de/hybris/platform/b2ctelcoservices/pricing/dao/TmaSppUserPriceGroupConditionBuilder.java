/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveUserPriceGroupsStrategy;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Search query condition builder for {@link PriceRowModel#UG} based on received {@link TmaPriceContext}.
 *
 * @since 1903
 */
public class TmaSppUserPriceGroupConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	private UserService userService;
	private TmaRetrieveUserPriceGroupsStrategy userPriceGroupsStrategy;

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext priceContext)
	{
		if (priceContext == null)
		{
			return false;
		}
		final UserModel user = priceContext.getUser();
		return !ObjectUtils.isEmpty(user);
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final GenericSearchField userSearchField = new GenericSearchField(null, PriceRowModel.USER);

		final GenericCondition userSearchCondition = GenericCondition.createConditionForValueComparison(userSearchField,
				Operator.EQUAL, priceContext.getUser());

		final GenericSearchField userGroupSearchField = new GenericSearchField(null, PriceRowModel.UG);

		final GenericCondition userAndUserGroupNullCondition = GenericConditionList.and(
				GenericCondition.createIsNullCondition(userSearchField),
				GenericCondition.createIsNullCondition(userGroupSearchField));

		final Set<UserPriceGroup> userPriceGroups = getUserPriceGroups(priceContext);

		return CollectionUtils.isNotEmpty(userPriceGroups)
				? GenericConditionList.or(userAndUserGroupNullCondition, userSearchCondition,
				createPriceGroupCondition(userGroupSearchField, userPriceGroups))
				: GenericConditionList.or(userAndUserGroupNullCondition, userSearchCondition);
	}

	protected GenericCondition createPriceGroupCondition(final GenericSearchField userPriceGroupField,
			final Set<UserPriceGroup> userPriceGroups)
	{
		final GenericCondition userPriceGroupFieldCondition = GenericCondition.in(userPriceGroupField, userPriceGroups);
		return GenericConditionList.createConditionList(userPriceGroupFieldCondition);
	}

	protected Set<UserPriceGroup> getUserPriceGroups(final TmaPriceContext priceContext)
	{
		if (priceContext.getUser() != null)
		{
			return getUserPriceGroupsStrategy().getAllUserPriceGroups(priceContext.getUser());

		}

		final UserModel currentSessionUser = getUserService().getCurrentUser();
		if (currentSessionUser != null)
		{
			return getUserPriceGroupsStrategy().getAllUserPriceGroups(currentSessionUser);
		}
		return Collections.emptySet();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected TmaRetrieveUserPriceGroupsStrategy getUserPriceGroupsStrategy()
	{
		return userPriceGroupsStrategy;
	}

	@Required
	public void setUserPriceGroupsStrategy(final TmaRetrieveUserPriceGroupsStrategy userPriceGroupsStrategy)
	{
		this.userPriceGroupsStrategy = userPriceGroupsStrategy;
	}
}
