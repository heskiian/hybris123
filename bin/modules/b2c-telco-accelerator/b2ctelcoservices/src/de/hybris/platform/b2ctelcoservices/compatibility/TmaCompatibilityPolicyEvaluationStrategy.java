/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;

import java.util.List;
import java.util.Set;


/**
 * Strategy used for evaluating compatibility policies.
 *
 * @since 6.7
 */
public interface TmaCompatibilityPolicyEvaluationStrategy
{

	/**
	 * Evaluates a list of {@link TmaCompatibilityPolicyModel} against a list of contexts and returns the evaluation results
	 * containing the actions to be performed and the contexts applicable for the actions
	 *
	 * @param policies
	 * 		policies to be evaluated
	 * @param contexts
	 * 		the list of input contexts
	 * @return the results containing the actions to be performed and the contexts applicable for the actions
	 */
	Set<RuleEvaluationResult> evaluatePolicies(final Set<TmaCompatibilityPolicyModel> policies,
			final List<TmaPolicyContext> contexts);

	/**
	 * Returns the list of policies having all conditions fulfilled.
	 *
	 * @param policies
	 * 		the list of policies to be evaluated
	 * @param requestParams
	 * 		request parameters agains which the evaluation is made
	 * @return a list of policies that have all conditions true
	 */
	Set<TmaCompatibilityPolicyModel> getApplicablePolicies(final List<TmaCompatibilityPolicyModel> policies,
			final List<TmaPolicyContext> requestParams);
}
