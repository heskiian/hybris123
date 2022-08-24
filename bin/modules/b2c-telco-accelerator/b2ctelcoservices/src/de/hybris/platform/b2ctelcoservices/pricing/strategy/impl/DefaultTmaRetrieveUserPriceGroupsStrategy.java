/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveUserPriceGroupsStrategy;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaRetrieveUserPriceGroupsStrategy}.
 *
 * @since 1903
 */
public class DefaultTmaRetrieveUserPriceGroupsStrategy implements TmaRetrieveUserPriceGroupsStrategy
{

	@Override
	public Set<UserPriceGroup> getAllUserPriceGroups(final UserModel user)
	{
		final Set<UserPriceGroup> userPriceGroups = new HashSet<>();
		final UserPriceGroup userPriceGroup = user.getEurope1PriceFactory_UPG();
		if (userPriceGroup != null)
		{
			userPriceGroups.add(userPriceGroup);
		}
		Set<PrincipalGroupModel> userGroups = user.getGroups();
		final Set<PrincipalGroupModel> controlSet = new HashSet<>();
		while (CollectionUtils.isNotEmpty(userGroups))
		{
			final Set<PrincipalGroupModel> nextGroups = new HashSet<>();
			for (PrincipalGroupModel userGroup : userGroups)
			{
				controlSet.add(userGroup);
				final UserPriceGroup priceGroup = userGroup.getProperty(UserGroupModel.USERPRICEGROUP);
				if (priceGroup != null)
				{
					userPriceGroups.add(priceGroup);
				}
				nextGroups.addAll(userGroup.getGroups());
			}
			nextGroups.removeAll(controlSet);
			userGroups = nextGroups;
		}
		return userPriceGroups;
	}
}
