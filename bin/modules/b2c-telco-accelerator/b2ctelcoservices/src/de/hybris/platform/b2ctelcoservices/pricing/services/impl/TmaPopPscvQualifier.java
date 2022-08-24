/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaPopQualifier;
import de.hybris.platform.b2ctelcoservices.services.TmaPscvService;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Implementation of {@link TmaPopQualifier} for qualification of {@link TmaProductSpecCharacteristicValueModel}s between
 * product offering and product offering price.
 *
 * @since 2011
 */
public class TmaPopPscvQualifier implements TmaPopQualifier
{
	private TmaPscvService pscvService;

	public TmaPopPscvQualifier(final TmaPscvService pscvService)
	{
		this.pscvService = pscvService;
	}

	/**
	 * Checks that a price qualifies for a product offering if the {@link TmaProductSpecCharacteristicValueModel}s used on the
	 * price are found in the {@link TmaProductSpecCharacteristicValueModel}s used on the {@link TmaProductOfferingModel}.
	 *
	 * @param price
	 * 		the Product Offering Price
	 * @param productOffering
	 * 		the Product Offering
	 * @return true if it qualifies, false otherwise.
	 */
	@Override
	public boolean qualifies(final TmaProductOfferingPriceModel price,
			TmaProductOfferingModel productOffering)
	{

		final Set<TmaProductSpecCharacteristicValueModel> pscvsOffering =
				getPscvService().getPscvsUsedFor(productOffering);
		final Set<TmaProductSpecCharacteristicValueModel> pscvsPop = getPscvService().getPscvsUsedFor(price);
		final Set<String> productPscvIds = pscvsOffering.stream().map(TmaProductSpecCharacteristicValueModel::getId)
				.collect(Collectors.toSet());
		final Set<String> pricePscvIds = pscvsPop.stream().map(TmaProductSpecCharacteristicValueModel::getId)
				.collect(Collectors.toSet());

		return productPscvIds.containsAll(pricePscvIds);
	}

	protected TmaPscvService getPscvService()
	{
		return pscvService;
	}
}
