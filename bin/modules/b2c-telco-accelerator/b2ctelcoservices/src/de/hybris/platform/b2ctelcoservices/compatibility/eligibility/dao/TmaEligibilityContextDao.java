/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao;

import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;

import java.util.Set;


/**
 * Provices access to {@link TmaEligibilityContextModel}
 *
 * @since 1907
 */
public interface TmaEligibilityContextDao
{
	/**
	 * Retrives {@link TmaEligibilityContextModel}
	 *
	 * @param customerId
	 * 	Identifies the customer
	 * @param subscriberIdentity
 * 		Identifies the subscription
	 *
	 * @return Set of applicable eligibility contexts
	 */
	Set<TmaEligibilityContextModel> getEligibilityContext(String customerId, String subscriberIdentity);

	/**
	 * Retrives all eligibility contexts
	 *
	 * @return Set of eligibility contexts
	 */
	Set<TmaEligibilityContextModel> getAllEligibilityContexts();
}
