/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;

import java.util.Set;


/**
 * Service responsible for operations on  {@link TmaCompatibilityPolicyModel}.
 *
 * @since 1810
 */
public interface TmaPolicyService
{
	/**
	 * Adds a new child condition to a certain parent condition.
	 *
	 * @param parentCondition
	 * 		parent  condition to be updated
	 * @param childCondition
	 * 		child condition to be added
	 */
	void addNewCondition(final TmaCompositePolicyConditionModel parentCondition,
			final TmaPolicyConditionModel childCondition);

	/**
	 * Adds a new child condition to a certain policy.
	 *
	 * @param policyModel
	 * 		parent policy to be updated
	 * @param childCondition
	 * 		child condition to be added
	 */
	void addNewCondition(final TmaCompatibilityPolicyModel policyModel,
			final TmaPolicyConditionModel childCondition);

	/**
	 * Removes a child condition from a certain parent.
	 *
	 * @param parentCondition
	 * 		parent  condition to be updated
	 * @param childCondition
	 * 		child condition to be removed
	 */
	void removeCondition(final TmaCompositePolicyConditionModel parentCondition,
			final TmaPolicyConditionModel childCondition);

	/**
	 * Removes a child condition from a certain policy.
	 *
	 * @param policy
	 * 		parent  policy to be updated
	 * @param condition
	 * 		child condition to be removed
	 */
	void removeCondition(final TmaCompatibilityPolicyModel policy, final TmaPolicyConditionModel condition);

	/**
	 * Returns the set of direct and indirect belonging policies for a condition.
	 *
	 * @param conditionModel
	 * 		condition model for which the parent policies are searched
	 * @return a set of parent {@link TmaCompatibilityPolicyModel}
	 */
	Set<TmaCompatibilityPolicyModel> getAllPolicies(final TmaPolicyConditionModel conditionModel);

}
