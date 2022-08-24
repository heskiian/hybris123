/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;

import java.util.List;


/**
 * Service used to validate {@link TmaPolicyConditionModel}.
 *
 * @since 6.7
 */
public interface TmaPolicyConditionValidator
{

	/**
	 * Validates the policy conditions against a set of product offerings parameters considering the evaluation operator from the
	 * policy.
	 *
	 * @param compatibilityPolicy
	 * 		policy for which conditions are validated
	 * @param contexts
	 * 		product offerings parameters for which the conditions are validated
	 * @return returns true if conditions are valid, false otherwise
	 */
	boolean validatePolicyConditions(final TmaCompatibilityPolicyModel compatibilityPolicy, final List<TmaPolicyContext> contexts);

}
