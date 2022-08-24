/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;


/**
 * Service responsible for verifying if a list of several product offerings are eligible to be bought together from
 * compatibility policies point of view.
 *
 * @since 6.7
 */
public interface TmaCompatibilityPolicyEngine
{
	/**
	 * Verifies compatibility policies identified for the product offerings within a given entry group from an order. In case of
	 * any invalid compatibility policy found, the actions are applied by either setting an error message on the entry group or
	 * adding a new auto picked entry.
	 *
	 * @param orderModel
	 * 		the current order
	 * @param entryGroup
	 * 		the group whose product offerings entries are verified
	 * @deprecated since 2102 - use {@link #verifyCompatibilityPolicies(AbstractOrderEntryModel, UserModel)} instead
	 */
	@Deprecated(since = "2102")
	void verifyCompatibilityPolicies(final AbstractOrderModel orderModel, final EntryGroup entryGroup);

	/**
	 * Verifies compatibility policies identified for the product offerings within provided entry and its children. In case of
	 * any invalid compatibility policy found, the actions are applied by either setting an error message on the entry group or
	 * adding a new auto picked entry.
	 *
	 * @param entryModel
	 * 		The entry which is verified
	 * @param userModel
	 * 		The user
	 */
	void verifyCompatibilityPolicies(final AbstractOrderEntryModel entryModel, final UserModel userModel);

	/**
	 * Verifies compatibility policies identified for all standalone products from the cart. In case of
	 * any invalid compatibility policy found, the actions are applied by either setting an error message on cart or by
	 * adding a new auto picked entry.
	 *
	 * @param orderModel
	 * 		the current order
	 */
	void verifyCompatibilityPoliciesForStandaloneProducts(final AbstractOrderModel orderModel);
}
