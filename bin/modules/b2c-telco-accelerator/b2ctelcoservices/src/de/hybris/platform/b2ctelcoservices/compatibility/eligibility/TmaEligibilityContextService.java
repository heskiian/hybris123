/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility;

import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.Set;


/**
 * Service responsible for updating {@link TmaEligibilityContext}s for the current customer.
 *
 * @since 1810
 */
public interface TmaEligibilityContextService
{
	/**
	 * Updates the {@link TmaEligibilityContext}s for the current customer.
	 *
	 * @param forceRefresh
	 * 		flag which indicates whether the eligibility contexts should be recreated before updating.
	 */
	void updateEligibilityContexts(final boolean forceRefresh);

	/**
	 * Updates the {@link TmaEligibilityContext}s for the current customer.
	 *
	 * @param customer
	 * 		Indicating the customer for whom eligibility context needs refresh
	 * @param forceRefresh
	 *       indicates whether to delete the old eligibility context or not
	 */
	void updateEligibilityContextsByCustomer(final boolean forceRefresh, final CustomerModel customer);

	/**
	 * Verifies if the eligibility context and eligibility rules should be applied.
	 *
	 * @return true if eligibility should be applied, false otherwise
	 */
	boolean shouldApplyEligibility();

	/**
	 * Updates the flag used to decide if the eligibility should be applied or not.
	 *
	 * @param applyEligibilityFlag the new value to be used for update
	 */
	void updateApplyEligibilityFlag(boolean applyEligibilityFlag);

	/**
	 * Retrieves current's customer eligibility contexts
	 *
	 * @return applicable eligibility contexts for the customer
	 */
	Set<TmaEligibilityContextModel> extractEligibilityContext();


	/**
	 * Retrieves current's customer eligibility contexts
	 *
	 * @param customerUid identifies the customer
	 *
	 * @return applicable eligibility contexts for the customer
	 */
	Set<TmaEligibilityContextModel> extractEligibilityContextByCustomer(final String customerUid);
}
