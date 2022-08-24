/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEvaluationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyActionValidator;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyConditionValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link TmaCompatibilityPolicyEvaluationStrategy}.
 *
 * @since 6.7
 */
public class DefaultTmaCompatibilityPolicyEvaluationStrategy implements TmaCompatibilityPolicyEvaluationStrategy
{
	private TmaPolicyConditionValidator conditionValidator;
	private TmaPolicyActionValidator actionValidator;

	@Override
	public Set<RuleEvaluationResult> evaluatePolicies(final Set<TmaCompatibilityPolicyModel> policies,
			final List<TmaPolicyContext> contexts)
	{
		final Set<RuleEvaluationResult> ruleEvaluationResults = new HashSet<>();
		policies.stream().filter(policy -> arePolicyConditionsValid(policy, contexts))
				.map(policy -> getActionValidator().getEvaluationResults(policy, contexts)).forEach(ruleEvaluationResults::addAll);
		return ruleEvaluationResults;
	}

	@Override
	public Set<TmaCompatibilityPolicyModel> getApplicablePolicies(final List<TmaCompatibilityPolicyModel> policies,
			final List<TmaPolicyContext> contexts)
	{
		return policies.stream().filter(policyModel -> arePolicyConditionsValid(policyModel, contexts)).collect(Collectors.toSet());
	}

	private List<TmaPolicyActionModel> evaluatePolicyActions(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts)
	{
		return getActionValidator().getInvalidPolicyActions(compatibilityPolicy, contexts);
	}

	private boolean arePolicyConditionsValid(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts)
	{
		return getConditionValidator().validatePolicyConditions(compatibilityPolicy, contexts);
	}

	protected TmaPolicyConditionValidator getConditionValidator()
	{
		return conditionValidator;
	}

	public void setConditionValidator(final TmaPolicyConditionValidator conditionValidator)
	{
		this.conditionValidator = conditionValidator;
	}

	protected TmaPolicyActionValidator getActionValidator()
	{
		return actionValidator;
	}

	public void setActionValidator(final TmaPolicyActionValidator actionValidator)
	{
		this.actionValidator = actionValidator;
	}
}
