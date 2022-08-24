/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyConditionValidator;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityConditionEvaluatorType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaPolicyConditionValidator }
 *
 * @since 6.7
 */
public class DefaultTmaPolicyConditionValidator implements TmaPolicyConditionValidator
{
	private TmaPolicyStatementValidationStrategy statementValidationStrategy;

	@Override
	public boolean validatePolicyConditions(final TmaCompatibilityPolicyModel compatibilityPolicy,
			final List<TmaPolicyContext> contexts)
	{
		if (CollectionUtils.isEmpty(compatibilityPolicy.getConditions()))
		{
			return true;
		}

		final TmaCompatibilityConditionEvaluatorType evaluatorType = compatibilityPolicy.getConditionsEvaluatorType();

		return evaluateCondition(compatibilityPolicy.getConditions(), evaluatorType, contexts);
	}

	private Boolean evaluateCondition(final Collection<TmaPolicyConditionModel> policyConditions,
			final TmaCompatibilityConditionEvaluatorType evaluatorType,
			final List<TmaPolicyContext> productListParams)
	{
		boolean conditionEvaluatorResult = TmaCompatibilityConditionEvaluatorType.AND.equals(evaluatorType);

		if (TmaCompatibilityConditionEvaluatorType.AND.equals(evaluatorType))
		{
			for (final TmaPolicyConditionModel policyCondition : policyConditions)
			{
				if (policyCondition instanceof TmaCompositePolicyConditionModel)
				{
					final TmaCompositePolicyConditionModel composedPolicyCondition = (TmaCompositePolicyConditionModel) policyCondition;
					conditionEvaluatorResult = conditionEvaluatorResult && evaluateCondition(
							composedPolicyCondition.getChildren(), composedPolicyCondition.getConditionsEvaluatorType(),
							productListParams);
				}
				else
				{
					boolean isStatementValid = getStatementValidationStrategy().isStatementValid(policyCondition.getStatement(),
							productListParams);
					conditionEvaluatorResult = conditionEvaluatorResult && isStatementValid;
				}

				if (conditionEvaluatorResult == Boolean.FALSE)
				{
					break;
				}
			}
		}

		if (TmaCompatibilityConditionEvaluatorType.OR.equals(evaluatorType))
		{
			for (final TmaPolicyConditionModel policyCondition : policyConditions)
			{
				if (policyCondition instanceof TmaCompositePolicyConditionModel)
				{
					final TmaCompositePolicyConditionModel composedPolicyCondition = (TmaCompositePolicyConditionModel) policyCondition;
					conditionEvaluatorResult = conditionEvaluatorResult || evaluateCondition(
							composedPolicyCondition.getChildren(), composedPolicyCondition.getConditionsEvaluatorType(),
							productListParams);
				}
				else
				{
					boolean isStatementValid = getStatementValidationStrategy().isStatementValid(policyCondition.getStatement(),
							productListParams);
					conditionEvaluatorResult = conditionEvaluatorResult || isStatementValid;
				}
			}
		}

		return conditionEvaluatorResult;
	}

	protected TmaPolicyStatementValidationStrategy getStatementValidationStrategy()
	{
		return statementValidationStrategy;
	}

	@Required
	public void setStatementValidationStrategy(
			final TmaPolicyStatementValidationStrategy statementValidationStrategy)
	{
		this.statementValidationStrategy = statementValidationStrategy;
	}
}
