/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Validator responsible for validating {@link TmaPolicyStatementModel}.
 *
 * @since 6.7
 */
public abstract class AbstractTmaPolicyStatementValidator
{
	/**
	 * Validates a {@link TmaPolicyStatementModel} against a set of product offerings parameters.
	 *
	 * @param statement
	 * 		compatibility policy statement
	 * @param contexts
	 * 		product offerings parameters considered for validation
	 * @return true if the statement is valid for the product offerings list, false otherwise
	 * @deprecated since 1911. Use instead {@link AbstractTmaPolicyStatementValidator#retrieveApplicableContexts(TmaPolicyStatementModel, List)}
	 */
	@Deprecated(since = "1911", forRemoval= true)
	public boolean validatePolicyStatement(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		final List<TmaPolicyContext> applicableContexts = retrieveApplicableContexts(statement, contexts);
		return CollectionUtils.isNotEmpty(applicableContexts);
	}

	/**
	 * Returns the list of contexts for which the statement is valid against
	 *
	 * @param statement
	 * 		the statement to be validated
	 * @param contexts
	 * 		the contexts to be validated against
	 * @return the list of contexts for which the statement is valid against
	 */
	public abstract List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts);

	/**
	 * Checks if the given quantity is between the min and max limits.
	 *
	 * @param quantity
	 * 		quantity to be verified
	 * @param min
	 * 		minimum limit
	 * @param max
	 * 		maximum limit
	 * @return true if the number is between min and max, false otherwise
	 */
	protected boolean checkQuantity(final Integer quantity, final Integer min, final Integer max)
	{
		return max == null ? (min <= quantity) : ((min <= quantity) && (max >= quantity));
	}

}
