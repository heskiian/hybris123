/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;

import java.util.Set;


/**
 * Service responsible for handling {@link TmaProductSpecCharacteristicValueModel}  related operations.
 *
 * @since 2011
 */
public interface TmaPscvService
{
	/**
	 * Retrieves all the {@link TmaProductSpecCharacteristicValueModel}s of a {@link TmaProductOfferingModel}. In case the
	 * offering is a {@link TmaBundledProductOfferingModel} it will consider the PSCVs attached on children.
	 *
	 * @param productOffering
	 * 		the offering.
	 * @return the PSCVs found on the offering.
	 */
	Set<TmaProductSpecCharacteristicValueModel> getPscvsUsedFor(final TmaProductOfferingModel productOffering);

	/**
	 * Retrieves all the {@link TmaProductSpecCharacteristicValueModel}s of a {@link TmaProductOfferingPriceModel}.
	 *
	 * @param productOfferingPrice
	 * 		the product offering price.
	 * @return the PSCVs found on the product offering price.
	 */
	Set<TmaProductSpecCharacteristicValueModel> getPscvsUsedFor(
			final TmaProductOfferingPriceModel productOfferingPrice);

	/**
	 * Returns the common {@link TmaProductSpecCharacteristicValueModel}s for a set of {@link TmaProductOfferingModel}s
	 *
	 * @param productOfferings
	 * 		the product offerings
	 * @return A set of common {@link TmaProductSpecCharacteristicValueModel}s for the given offerings; empty set otherwise
	 */
	Set<TmaProductSpecCharacteristicValueModel> getCommonPscvsFor(final Set<TmaProductOfferingModel> productOfferings);
}
