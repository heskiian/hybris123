/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyService;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;

import java.util.List;


/**
 * Service handling operations specific to eligibility policies.
 *
 * @since 1810
 */
public interface TmaEligibilityPolicyService extends TmaPolicyService
{

	/**
	 * Retrieves the list of available eligibility policies. Eligibility policies are the ones having
	 * {@link de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType#ALLOW} action type.
	 *
	 * @return a list of {@link TmaCompatibilityPolicyModel}
	 */
	List<TmaCompatibilityPolicyModel> getAvailableEligibilityPolicies();

}
