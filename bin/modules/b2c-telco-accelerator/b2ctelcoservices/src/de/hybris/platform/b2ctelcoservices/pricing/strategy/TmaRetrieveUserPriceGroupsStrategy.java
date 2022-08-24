/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;

import java.util.Set;


/**
 * Strategy to retrieve the user price groups based on the user
 *
 * @since 1903
 */
public interface TmaRetrieveUserPriceGroupsStrategy
{
	Set<UserPriceGroup> getAllUserPriceGroups(final UserModel userModel);
}
