/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link TmaComponentProdOfferPriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopPriceAttributeMapper extends TmaAttributeMapper<TmaComponentProdOfferPriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaComponentProdOfferPriceData source,
			final ProductOfferingPrice target, final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getCurrency()) || ObjectUtils.isEmpty(source.getValue()))
		{
			return;
		}
		final Money price = new Money();
		price.setValue(source.getValue().toString());
		price.setUnit(source.getCurrency().getIsocode());
		target.setPrice(price);
	}
}
