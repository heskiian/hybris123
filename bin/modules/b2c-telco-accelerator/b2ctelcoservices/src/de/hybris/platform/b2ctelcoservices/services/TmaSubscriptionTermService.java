/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.List;
import java.util.Set;


/**
 * Service for operations related to the {@link SubscriptionTermModel}.
 *
 * @since 6.7
 */
public interface TmaSubscriptionTermService
{
	/**
	 * Returns all subscription terms.
	 *
	 * @return {@link List} of {@link SubscriptionTermModel}
	 */
	List<SubscriptionTermModel> getAllSubscriptionTerms();

	/**
	 * Returns all subscription terms found in the price plans configured on given spo and bpo.
	 * Note: if a price plan has no subscription terms, then all subscription terms will be considered.
	 *
	 * @param spo
	 * 		simple product offering
	 * @param bpo
	 * 		bundled product offering
	 * @param processType
	 * 		the process type for which price plans will be considered
	 * @return {@link Set} of {@link SubscriptionTermModel}
	 */
	Set<SubscriptionTermModel> getApplicableSubscriptionTerms(final TmaProductOfferingModel spo,
			final TmaBundledProductOfferingModel bpo, final TmaProcessType processType);

	/**
	 * Returns {@link SubscriptionTermModel} corresponding to the given id.
	 *
	 * @param id
	 * 		subscription term unique id
	 * @return {@link SubscriptionTermModel} found or null in case of no object model is found
	 */
	SubscriptionTermModel getSubscriptionTerm(final String id);

	/**
	 * Determines default subscription term.
	 *
	 * @return the default subscription term configured
	 */
	SubscriptionTermModel getDefaultSubscriptionTerm();

	/**
	 * Returns the subscription terms configured on the given price row if there are any,
	 * otherwise it returns all existing subscription terms
	 *
	 * @param priceRow
	 * 		the price row
	 * @return the subscription terms configured on the given price row if there are any,
	 * otherwise it returns all existing subscription terms
	 */
	List<SubscriptionTermModel> getSubscriptionTermsFor(final PriceRowModel priceRow);
}
