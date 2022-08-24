/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */

package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;

import java.util.List;
import java.util.Set;


/**
 * Engine used to retrieve a list of {@link RuleEvaluationResult} containing actions from valid policies and the list of
 * context necessary to apply the actions
 *
 * @since 1907
 */
public interface TmaPolicyEngine
{
	/**
	 * Retrieves a list of rule evaluation results, each result having an action to be applied and the list of contexts needed
	 * for the action to be applied
	 *
	 * @param contexts
	 * 		the given contexts
	 * @param actionTypes
	 * 		the given action types
	 * @return a list of {@link RuleEvaluationResult}
	 */
	Set<RuleEvaluationResult> findPolicies(final List<TmaPolicyContext> contexts,
			final Set<TmaCompatibilityPolicyActionType> actionTypes);

	/**
	 * Apply each action from the results received as input
	 *
	 * @param ruleEvaluationResults
	 * 		the results containing the actions to be applied
	 */
	void applyActions(final Set<RuleEvaluationResult> ruleEvaluationResults);
}
