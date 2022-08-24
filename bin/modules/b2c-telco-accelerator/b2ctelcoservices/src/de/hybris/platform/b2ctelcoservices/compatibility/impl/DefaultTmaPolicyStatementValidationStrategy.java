/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaPolicyStatementValidationStrategy}.
 *
 * @since 6.7
 */
public class DefaultTmaPolicyStatementValidationStrategy implements TmaPolicyStatementValidationStrategy
{
	private Map<String, AbstractTmaPolicyStatementValidator> statementValidatorsMap;

	@Override
	public List<TmaPolicyContext> getApplicableContexts(final TmaPolicyStatementModel statementModel,
			final List<TmaPolicyContext> contexts)
	{
		if (getStatementValidatorsMap().containsKey(statementModel.getItemtype()))
		{
			return getStatementValidatorsMap().get(statementModel.getItemtype())
					.retrieveApplicableContexts(statementModel, contexts);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isStatementValid(final TmaPolicyStatementModel statementModel, final List<TmaPolicyContext> contexts)
	{
		return CollectionUtils.isNotEmpty(getApplicableContexts(statementModel, contexts));
	}

	@Override
	public boolean validateStatement(final TmaPolicyStatementModel statementModel, final List<TmaPolicyContext> contexts)
	{
		if (getStatementValidatorsMap().containsKey(statementModel.getItemtype()))
		{
			return getStatementValidatorsMap().get(statementModel.getItemtype())
					.validatePolicyStatement(statementModel, contexts);
		}
		return false;
	}

	protected Map<String, AbstractTmaPolicyStatementValidator> getStatementValidatorsMap()
	{
		return statementValidatorsMap;
	}

	public void setStatementValidatorsMap(
			Map<String, AbstractTmaPolicyStatementValidator> statementValidatorsMap)
	{
		this.statementValidatorsMap = statementValidatorsMap;
	}
}
