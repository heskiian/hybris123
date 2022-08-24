/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Money;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import ma.glasnost.orika.MappingContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for price attribute between {@link SubscriptionPricePlanData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPricePriceAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (!(source instanceof SubscriptionPricePlanData))
		{
			return;
		}
		final SubscriptionPricePlanData sppData = (SubscriptionPricePlanData) source;

		if (CollectionUtils.isNotEmpty(sppData.getOneTimeChargeEntries())
				|| CollectionUtils.isNotEmpty(sppData.getRecurringChargeEntries())
				|| CollectionUtils.isNotEmpty(sppData.getUsageCharges()))
		{
			return;
		}

		target.setIsBundle(false);

		if (StringUtils.isEmpty(sppData.getCurrencyIso()) && sppData.getValue() == null)
		{
			return;
		}

		final Money price = new Money();
		price.setValue(sppData.getValue().toString());
		price.setUnit(sppData.getCurrencyIso());
		target.setPrice(price);
	}
}
