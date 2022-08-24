/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;

import java.util.List;


/**
 * Strategy used for evaluating {@link TmaPolicyStatementModel}.
 *
 * @since 6.7
 */
public interface TmaPolicyStatementValidationStrategy
{
	/**
	 * Returns the list of contexts for which the statement is valid
	 *
	 * @param statementModel
	 * 		the statement to be validated
	 * @param contexts
	 * 		the contexts to be validated against
	 * @return the list of contexts for which the statement is valid
	 */
	List<TmaPolicyContext> getApplicableContexts(final TmaPolicyStatementModel statementModel,
			final List<TmaPolicyContext> contexts);

	/**
	 * Verifies if the statement is valid against the list of contexts
	 *
	 * @param statementModel
	 * 		the statement to be validated
	 * @param contexts
	 * 		the contexts to be validated againnst
	 * @return true if the statement is valid against the contexts, otherwise false
	 */
	boolean isStatementValid(final TmaPolicyStatementModel statementModel, final List<TmaPolicyContext> contexts);

	/**
	 * Validates a statement model against a list of product offerings parameters.
	 *
	 * @param statementModel
	 * 		the given {@link TmaPolicyStatementModel} to be validated
	 * @param contexts
	 * 		product offerings parameters
	 * @return true if the statement is valid, false otherwise
	 * @deprecated since 1911. Use TmaPolicyStatementValidationStrategy#getApplicableContexts(TmaPolicyStatementModel, java.util
	 * .List) instead
	 */
	@Deprecated(since = "1911", forRemoval= true)
	boolean validateStatement(final TmaPolicyStatementModel statementModel, final List<TmaPolicyContext> contexts);



}
