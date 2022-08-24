/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.PriceData;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for price attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 2007
 */
public class PopPriceValueAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getProductOfferingPrice()))
		{
			return;
		}

		target.setIsBundle(false);

		if (StringUtils.isEmpty(source.getCurrencyIso()) && source.getValue() == null)
		{
			return;
		}

		final Money price = new Money();
		price.setValue(source.getValue().toString());
		price.setUnit(source.getCurrencyIso());
		target.setPrice(price);
	}
}