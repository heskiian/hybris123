/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */

package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEvaluationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaPolicyEngine}
 *
 * @since 1907
 */
public class DefaultTmaPolicyEngine implements TmaPolicyEngine
{
	private TmaPolicyDao policyDao;
	private TmaCompatibilityPolicyEvaluationStrategy compatibilityPolicyEvaluationStrategy;
	private Map<TmaCompatibilityPolicyActionType, TmaPolicyActionStrategy> policyActionStrategyMap;

	@Override
	public Set<RuleEvaluationResult> findPolicies(final List<TmaPolicyContext> contexts,
			final Set<TmaCompatibilityPolicyActionType> actionTypes)
	{

		final Set<TmaCompatibilityPolicyModel> policies = new HashSet<>();
		if (CollectionUtils.isEmpty(contexts))
		{
			policies.addAll(getPolicyDao().findPoliciesWithoutCondition(actionTypes));
			return getCompatibilityPolicyEvaluationStrategy().evaluatePolicies(policies, contexts);
		}

		contexts.stream().forEach(requestParam -> {
			policies.addAll(getPolicyDao().findCompatibilityPoliciesByConditions(requestParam, actionTypes));
			policies.addAll(getPolicyDao().findPoliciesWithoutCondition(actionTypes, requestParam));
		});

		return getCompatibilityPolicyEvaluationStrategy().evaluatePolicies(policies, contexts);
	}

	@Override
	public void applyActions(final Set<RuleEvaluationResult> ruleEvaluationResults)
	{
		final Map<TmaCompatibilityPolicyActionType, Set<RuleEvaluationResult>> resultsByActionType = new HashMap<>();
		ruleEvaluationResults.forEach(ruleEvaluationResult -> {
			final TmaCompatibilityPolicyActionType actionType = ruleEvaluationResult.getAction().getActionType();
			if (resultsByActionType.containsKey(actionType))
			{
				resultsByActionType.get(actionType).add(ruleEvaluationResult);
			}
			else
			{
				final Set<RuleEvaluationResult> resultsByActionTypeValue = new HashSet<>();
				resultsByActionTypeValue.add(ruleEvaluationResult);
				resultsByActionType.put(actionType, resultsByActionTypeValue);
			}
		});

		resultsByActionType.keySet().forEach(policyActionType -> getPolicyActionStrategyMap().get(policyActionType)
				.processActions(resultsByActionType.get(policyActionType)));

	}

	public TmaPolicyDao getPolicyDao()
	{
		return policyDao;
	}

	@Required
	public void setPolicyDao(final TmaPolicyDao policyDao)
	{
		this.policyDao = policyDao;
	}

	public TmaCompatibilityPolicyEvaluationStrategy getCompatibilityPolicyEvaluationStrategy()
	{
		return compatibilityPolicyEvaluationStrategy;
	}

	@Required
	public void setCompatibilityPolicyEvaluationStrategy(
			final TmaCompatibilityPolicyEvaluationStrategy compatibilityPolicyEvaluationStrategy)
	{
		this.compatibilityPolicyEvaluationStrategy = compatibilityPolicyEvaluationStrategy;
	}

	public Map<TmaCompatibilityPolicyActionType, TmaPolicyActionStrategy> getPolicyActionStrategyMap()
	{
		return policyActionStrategyMap;
	}

	@Required
	public void setPolicyActionStrategyMap(
			final Map<TmaCompatibilityPolicyActionType, TmaPolicyActionStrategy> policyActionStrategyMap)
	{
		this.policyActionStrategyMap = policyActionStrategyMap;
	}
}
