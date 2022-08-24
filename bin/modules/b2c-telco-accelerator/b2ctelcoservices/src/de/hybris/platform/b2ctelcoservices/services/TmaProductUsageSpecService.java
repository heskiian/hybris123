/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;

import java.util.Set;


/**
 * Service responsible for handling {@link TmaProductUsageSpecificationModel}  related operations.
 *
 * @since 2011
 */
public interface TmaProductUsageSpecService
{
	/**
	 * Retrieves all the {@link TmaCompositeProductUsageSpecModel}s (including children) and
	 * {@link TmaAtomicProductUsageSpecModel}s of a {@link TmaProductOfferingModel}. In case the offering is a
	 * {@link TmaBundledProductOfferingModel} it will consider the {@link TmaProductUsageSpecificationModel} attached on children
	 *
	 * @param productOffering
	 * 		the offering.
	 * @return the product usage specifications found on the offering.
	 */
	Set<TmaProductUsageSpecificationModel> getProdUsageSpecificationsFor(final TmaProductOfferingModel productOffering);

	/**
	 * Retrieves all the {@link TmaCompositeProductUsageSpecModel}s (including children) and
	 * {@link TmaAtomicProductUsageSpecModel}s of a {@link TmaProductOfferingPriceModel}.
	 *
	 * @param productOfferingPrice
	 * 		the product offering price.
	 * @return the product usage specifications found on the offering.
	 */
	Set<TmaProductUsageSpecificationModel> getProdUsageSpecificationsFor(
			final TmaProductOfferingPriceModel productOfferingPrice);

	/**
	 * Retrieves the {@link TmaProductOfferingModel} with the smallest set of
	 * {@link TmaProductUsageSpecificationModel}s.
	 *
	 * @param productOfferings
	 * 		the product offerings
	 * @return the reference product offering.
	 */
	TmaProductOfferingModel getOfferingWithMinimumProductUsageSpecsSize(final Set<TmaProductOfferingModel> productOfferings);
}
