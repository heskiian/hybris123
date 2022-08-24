/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators;

import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.List;


/**
 * Service used to validate {@link de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel}.
 *
 * @since 6.7
 */
public interface TmaPolicyActionValidator
{

	/**
	 * Validates policy actions against a set of contexts
	 *
	 * @param policy
	 * 		policy for which actions are validated
	 * @param contexts
	 * 		contexts against which the policy actions are evaluated
	 * @return the rule evaluation results
	 */
	List<RuleEvaluationResult> getEvaluationResults(final TmaCompatibilityPolicyModel policy,
			final List<TmaPolicyContext> contexts);

	/**
	 * Validates policy actions against a set of product offerings parameters.
	 *
	 * @param compatibilityPolicy
	 * 		policy for which actions are validated
	 * @param contexts
	 * 		product offerings parameters
	 * @return returns a list of invalid policy actions
	 * @deprecated since 1911. Use {@link TmaPolicyActionValidator#getEvaluationResults(TmaCompatibilityPolicyModel, List)}
	 * instead
	 */
	@Deprecated(since = "1911", forRemoval= true)
	List<TmaPolicyActionModel> getInvalidPolicyActions(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts);


}
