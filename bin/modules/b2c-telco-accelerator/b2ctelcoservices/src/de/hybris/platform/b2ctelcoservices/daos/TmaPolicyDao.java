/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;

import java.util.List;
import java.util.Set;
import java.util.function.Function;


/**
 * Data Access Object for operations related to the
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel} type.
 *
 * @since 1810
 */
public interface TmaPolicyDao
{
	/**
	 * Retrieves policy models corresponding to the specified action type and available for the current time.
	 *
	 * @param actionType
	 *           given action type
	 * @return the list of corresponding {@link TmaCompatibilityPolicyModel}
	 */
	List<TmaCompatibilityPolicyModel> findPoliciesByActionType(final TmaCompatibilityPolicyActionType actionType);

	/**
	 * Creates a function for retrieving all corresponding {@link TmaCompatibilityPolicyModel} starting from a
	 * {@link TmaPolicyConditionModel}.
	 *
	 * @return an implementation of {@link Function} to gather the required information.
	 */
	Function<TmaPolicyConditionModel, Set<TmaCompatibilityPolicyModel>> gatherAllPolicies();

	/**
	 * Retrieves policy models without conditions and with actions having the action types sent as parameter
	 *
	 * @param actionTypes
	 *           the given action types
	 * @return a list of {@link TmaCompatibilityPolicyModel}
	 */
	List<TmaCompatibilityPolicyModel> findPoliciesWithoutCondition(final Set<TmaCompatibilityPolicyActionType> actionTypes);

	/**
	 * Retrieves policy models with conditions matching the context sent as parameter and with actions having the action
	 * types sent as parameter
	 *
	 * @param context
	 *           the context for which conditions are matched
	 * @param actionTypes
	 *           the given action types
	 * @return a list of {@link TmaCompatibilityPolicyModel}
	 */
	List<TmaCompatibilityPolicyModel> findCompatibilityPoliciesByConditions(final TmaPolicyContext context,
			final Set<TmaCompatibilityPolicyActionType> actionTypes);


	/**
	 * Retrieves policy models without conditions and with actions having the action types and policy context sent as
	 * parameter
	 *
	 * @param actionTypes
	 *           the given action types
	 * @param context
	 *           policy context is policy selection criteria
	 * @return a list of {@link TmaCompatibilityPolicyModel}
	 */
	List<TmaCompatibilityPolicyModel> findPoliciesWithoutCondition(Set<TmaCompatibilityPolicyActionType> actionTypes,
			TmaPolicyContext context);

}
