/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProcessTypesPolicyStatementModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProcessTypesPolicyStatementModel}
 *
 * @since 1907
 */
public class TmaProcessTypesStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaProcessTypesPolicyStatementModel))
		{
			return contexts;
		}

		final Set<TmaProcessType> processesFromStatement = ((TmaProcessTypesPolicyStatementModel) statement).getProcesses();

		return contexts.stream()
				.filter(context -> processesFromStatement != null && processesFromStatement.contains(context.getProcessType()))
				.collect(Collectors.toList());
	}
}
