/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price.impl;

import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link de.hybris.platform.commercefacades.product.PriceDataFactory} to always create
 * {@link SubscriptionPricePlanData}.
 *
 * @since 6.7
 */
public class DefaultTmaPriceDataFactory extends DefaultPriceDataFactory implements TmaPriceDataFactory
{
	@Override
	public SubscriptionPricePlanData create(final PriceDataType priceType, final BigDecimal value, final String currencyIso)
	{
		return (SubscriptionPricePlanData) super.create(priceType, value, currencyIso);
	}

	@Override
	public SubscriptionPricePlanData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{
		return (SubscriptionPricePlanData) super.create(priceType, value, currency);
	}

	@Override
	public BigDecimal getValueForPrice(PriceData price)
	{
		if (!(price instanceof SubscriptionPricePlanData))
		{
			return price.getValue();
		}

		final SubscriptionPricePlanData subscriptionPricePlan = (SubscriptionPricePlanData) price;
		final Collection<RecurringChargeEntryData> recurringCharges = subscriptionPricePlan.getRecurringChargeEntries();
		if (CollectionUtils.isNotEmpty(recurringCharges))
		{
			return recurringCharges.iterator().next().getPrice().getValue();
		}

		final BigDecimal priceValue = subscriptionPricePlan.getValue();
		if (priceValue != null)
		{
			return priceValue;
		}

		final Collection<OneTimeChargeEntryData> oneTimeCharges = subscriptionPricePlan.getOneTimeChargeEntries();
		if (CollectionUtils.isNotEmpty(oneTimeCharges))
		{
			return oneTimeCharges.iterator().next().getPrice().getValue();
		}

		throw new IllegalArgumentException("No price value defined for price with value [" + price.getFormattedValue() + "]. "
				+ "One must provide either recurring charge, value or one time charge.");
	}

	@Override
	public PriceDataType getPriceDataTypeForProduct(final ProductModel product)
	{
		return CollectionUtils.isNotEmpty(product.getVariants()) ? PriceDataType.FROM : PriceDataType.BUY;
	}

	@Override
	public String formatPriceValue(BigDecimal value, CurrencyModel currency)
	{
		return formatPrice(value, currency);
	}

	@Override
	protected PriceData createPriceData()
	{
		return new SubscriptionPricePlanData();
	}
}
