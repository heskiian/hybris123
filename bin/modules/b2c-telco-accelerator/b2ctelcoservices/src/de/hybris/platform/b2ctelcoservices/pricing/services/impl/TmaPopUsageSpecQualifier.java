/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopQualifier;
import de.hybris.platform.b2ctelcoservices.services.TmaProductUsageSpecService;

import java.util.HashSet;
import java.util.Set;


/**
 * Implementation of {@link TmaPopQualifier} for qualification of {@link TmaProductUsageSpecificationModel}s between
 * product offering and product offering price.
 *
 * @since 2011
 */
public class TmaPopUsageSpecQualifier implements TmaPopQualifier
{
	private TmaProductUsageSpecService productUsageSpecService;

	public TmaPopUsageSpecQualifier(final TmaProductUsageSpecService productUsageSpecService)
	{
		this.productUsageSpecService = productUsageSpecService;
	}

	/**
	 * Checks that a price qualifies for a product offering based on the {@link TmaProductUsageSpecificationModel}.
	 *
	 * @param price
	 * 		the Product Offering Price
	 * @param productOffering
	 * 		the Product Offering
	 * @return true if valid, false otherwise.
	 */
	@Override
	public boolean qualifies(TmaProductOfferingPriceModel price, TmaProductOfferingModel productOffering)
	{
		final Set<String> productUsageSpecIds = new HashSet<>();
		final Set<TmaProductUsageSpecificationModel> productUsageSpecsOffering =
				getProductUsageSpecService().getProdUsageSpecificationsFor(productOffering);
		final Set<String> priceUsageSpecIds = new HashSet<>();
		final Set<TmaProductUsageSpecificationModel> productUsageSpecsPop = getProductUsageSpecService()
				.getProdUsageSpecificationsFor(price);
		productUsageSpecsOffering
				.forEach(productUsageSpecification -> productUsageSpecIds.add(productUsageSpecification.getId()));
		productUsageSpecsPop
				.forEach(productUsageSpecification -> priceUsageSpecIds.add(productUsageSpecification.getId()));

		return productUsageSpecIds.containsAll(priceUsageSpecIds);
	}

	protected TmaProductUsageSpecService getProductUsageSpecService()
	{
		return productUsageSpecService;
	}
}
