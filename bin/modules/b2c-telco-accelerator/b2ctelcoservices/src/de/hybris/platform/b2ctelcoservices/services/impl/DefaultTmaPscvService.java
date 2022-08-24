/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPscvService;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link TmaPscvService}.
 *
 * @since 2011
 */
public class DefaultTmaPscvService implements TmaPscvService
{

	@Override
	public Set<TmaProductSpecCharacteristicValueModel> getPscvsUsedFor(final TmaProductOfferingModel productOffering)
	{
		validateParameterNotNull(productOffering, "The product offering can not be null");
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>();
		computePscvsFor(productOffering, pscvs);
		return pscvs;
	}

	@Override
	public Set<TmaProductSpecCharacteristicValueModel> getPscvsUsedFor(final TmaProductOfferingPriceModel productOfferingPrice)
	{
		validateParameterNotNull(productOfferingPrice, "The product offering price can not be null");
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>();
		flattenPopPscvs(productOfferingPrice, pscvs);
		return pscvs;
	}

	@Override
	public Set<TmaProductSpecCharacteristicValueModel> getCommonPscvsFor(final Set<TmaProductOfferingModel> productOfferings)
	{
		final Set<TmaProductSpecCharacteristicValueModel> commonPscvs = getPscvsUsedFor(productOfferings.iterator().next());
		productOfferings.forEach(productOffering -> commonPscvs.retainAll(getPscvsUsedFor(productOffering)));
		return commonPscvs;
	}

	/**
	 * Retrieves a set of all the {@link TmaProductSpecCharacteristicValueModel}s found on a
	 * {@link TmaProductOfferingPriceModel} including the PSCVs on children of {@link TmaCompositeProdOfferPriceModel}.
	 *
	 * @param price
	 * 		the product offering price
	 * @param pscvs
	 * 		the {@link TmaProductSpecCharacteristicValueModel}s found on the price
	 */
	private void flattenPopPscvs(final TmaProductOfferingPriceModel price,
			final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		if (CollectionUtils.isNotEmpty(price.getProductSpecCharacteristicValues()))
		{
			pscvs.addAll(price.getProductSpecCharacteristicValues());
		}

		if (price instanceof TmaCompositeProdOfferPriceModel && CollectionUtils
				.isNotEmpty(((TmaCompositeProdOfferPriceModel) price).getChildren()))
		{
			((TmaCompositeProdOfferPriceModel) price).getChildren()
					.forEach(child -> flattenPopPscvs(child, pscvs));
		}
	}

	/**
	 * Computes the set of PSCVs for a given offering. In case the offering is a {@link TmaBundledProductOfferingModel} it
	 * will consider the PSCVs attached on children.
	 *
	 * @param offering
	 * 		the product offering
	 * @param pscvs
	 * 		the {@link TmaProductSpecCharacteristicValueModel}s found on the offering
	 */
	private void computePscvsFor(final TmaProductOfferingModel offering, final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		if (offering instanceof TmaSimpleProductOfferingModel && CollectionUtils
				.isNotEmpty(offering.getProductSpecCharValueUses()))
		{
			offering.getProductSpecCharValueUses().forEach((TmaProductSpecCharValueUseModel pscvu) ->
					pscvs.addAll(pscvu.getProductSpecCharacteristicValues()));
		}

		if (offering instanceof TmaBundledProductOfferingModel && CollectionUtils
				.isNotEmpty(((TmaBundledProductOfferingModel) offering).getChildren()))
		{
			((TmaBundledProductOfferingModel) offering).getChildren()
					.forEach(child -> computePscvsFor(child, pscvs));
		}
	}
}
