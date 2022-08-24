/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility;

import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Set;


/**
 * Service responsible for computing eligibility rules and then creating the eligibility context.
 *
 * @since 1810
 */
public interface TmaEligibilityPolicyEngine
{
	/**
	 * Identifies the eligibility rules, validates them and considers only the ones which pass the conditions validations
	 * based on the given customer details in order to created customer corresponding eligibility context.
	 *
	 * @param customer
	 *           the customer for which the eligibility context is processed
	 */
	Set<TmaEligibilityContext> createEligibilityContext(final CustomerModel customer);

	/**
	 * Identifies the eligibility policies, validates them and considers only the ones which pass the conditions
	 * validations based on the given customer details.
	 *
	 * @param customer
	 *           Indicating the customer for whom Eligibility policy is fetched
	 * @return Set of applicable eligibility policies.
	 */
	Set<TmaCompatibilityPolicyModel> getApplicableEligibilityPolicies(CustomerModel customer);


}
