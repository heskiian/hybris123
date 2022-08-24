/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOfferingPrice;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for isBundle attribute between {@link TmaProductOfferingPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopIsBundleAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (source instanceof TmaCompositeProdOfferPriceData && CollectionUtils
				.isNotEmpty(((TmaCompositeProdOfferPriceData) source).getChildren()))
		{
			target.setIsBundle(true);
			return;
		}
		target.setIsBundle(false);
	}
}
