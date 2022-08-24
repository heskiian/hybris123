/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.bundledproductofferingoption;

import de.hybris.platform.b2ctelcofacades.data.TmaBundledProdOfferOptionData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.BundledProductOfferingOption;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at type attribute between {@link TmaBundledProdOfferOptionData} and {@link BundledProductOfferingOption}
 *
 * @since 2011
 */
public class BpoOptionAtTypeAttributeMapper
		extends TmaAttributeMapper<TmaBundledProdOfferOptionData, BundledProductOfferingOption>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaBundledProdOfferOptionData source,
			final BundledProductOfferingOption target, final MappingContext context)
	{
		target.setAttype(target.getClass().getSimpleName());
	}
}
