/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyActionValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaPolicyActionValidator }
 *
 * @since 6.7
 */
public class DefaultTmaPolicyActionValidator implements TmaPolicyActionValidator
{
	private TmaPolicyStatementValidationStrategy statementValidationStrategy;

	@Override
	public List<TmaPolicyActionModel> getInvalidPolicyActions(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts)
	{
		final List<TmaPolicyActionModel> invalidPolicyActions = new ArrayList<>();

		for (final TmaPolicyActionModel policyAction : compatibilityPolicy.getActions())
		{
			final boolean isValidStatement = getStatementValidationStrategy().validateStatement(policyAction.getStatement(),
					contexts);

			if (!isValidStatement)
			{
				invalidPolicyActions.add(policyAction);
			}
		}
		return invalidPolicyActions;
	}

	@Override
	public List<RuleEvaluationResult> getEvaluationResults(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts)
	{
		final List<RuleEvaluationResult> ruleEvaluationResults = new ArrayList<>();
		compatibilityPolicy.getActions().forEach(policyAction -> {
			final List<TmaPolicyContext> applicableContexts = getStatementValidationStrategy()
					.getApplicableContexts(policyAction.getStatement(), contexts);
			final RuleEvaluationResult ruleEvaluationResult = new RuleEvaluationResult();
			ruleEvaluationResult.setAction(policyAction);
			ruleEvaluationResult.setContexts(applicableContexts);
			ruleEvaluationResult.setInitialContexts(contexts);
			ruleEvaluationResult.setPolicy(compatibilityPolicy);
			ruleEvaluationResults.add(ruleEvaluationResult);
		});
		return ruleEvaluationResults;
	}

	protected TmaPolicyStatementValidationStrategy getStatementValidationStrategy()
	{
		return statementValidationStrategy;
	}

	@Required
	public void setStatementValidationStrategy(final TmaPolicyStatementValidationStrategy statementValidationStrategy)
	{
		this.statementValidationStrategy = statementValidationStrategy;
	}
}
