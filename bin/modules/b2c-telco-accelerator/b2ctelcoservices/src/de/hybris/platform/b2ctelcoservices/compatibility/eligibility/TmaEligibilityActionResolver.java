/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility;

import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;

import java.util.List;
import java.util.Set;


/**
 * Action resolver responsible for handling eligibility policies actions.
 *
 * @since 1810
 */
public interface TmaEligibilityActionResolver
{
	/**
	 * Creates a list of {@link TmaEligibilityContext} for each applicable policy for the given subscription id.
	 *
	 * @param policies
	 *           the applicable policies to be processed
	 * @param subscriptionBaseId
	 *           subscription base Id for which the policies are applicable. It can be empty, meaning that the
	 *           eligibility context params are available for each subscription
	 * @param billingSystemId
	 *           the billing system of the subscription base for which the policies are applicable.
	 * @return the list of eligibility context objects
	 */
	List<TmaEligibilityContext> processEligibilityPolicyActions(final Set<TmaCompatibilityPolicyModel> policies,
			final String subscriptionBaseId, final String billingSystemId);

}
