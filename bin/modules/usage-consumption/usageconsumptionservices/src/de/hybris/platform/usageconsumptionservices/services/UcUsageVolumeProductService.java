/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptionservices.services;


import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;

import java.util.Date;
import java.util.List;


/**
 * Service responsible for handling {@link UcUsageVolumeProductModel}  related operations.
 *
 * @since 2108
 */
public interface UcUsageVolumeProductService
{
	/**
	 * Retrieves a list of {@link UcUsageVolumeProductModel} for a given context.
	 *
	 * @param ucUsageVolumeProductContext
	 * 		the context.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned usage volume products.
	 * @return the list of usage volume products found for the given context.
	 */
	List<UcUsageVolumeProductModel> getUsageVolumeProducts(final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final Integer offset, final Integer limit);

	/**
	 * Retrieves the total number of usage volume products found for a given context.
	 *
	 * @param ucUsageVolumeProductContext
	 * 		the context.
	 * @return the number of usage volume products found.
	 */
	Integer getNumberOfUsageVolumeProductFor(final UcUsageVolumeProductContext ucUsageVolumeProductContext);

	/**
	 * Retrieves the most recent effective date from ConsumptionSummaries attached on UsageVolumeProducts
	 *
	 * @param usageVolumeProducts
	 * 		the list of usage volume products
	 * @return the most recent effective date
	 */
	Date getMostRecentDate(final List<UcUsageVolumeProductModel> usageVolumeProducts);
}
