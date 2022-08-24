/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.eligibility.converters.populator;

import de.hybris.platform.b2ctelcofacades.price.TmaPriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionfacades.data.OneTimeChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Populates {@link ProductData} based on {@link SearchResultValueData}.
 *
 * @since 2003
 */
public class TmaSolrSpoPricePopulator<S extends SearchResultValueData, T extends ProductData> implements Populator<S, T>
{
	private static final String ALL_REGION_PRICES = " ";
	private final TmaSolrPropSource propSource;
	private final CommonI18NService i18NService;
	private final TmaPriceDataFactory priceDataFactory;


	public TmaSolrSpoPricePopulator(final TmaSolrPropSource propSource, final CommonI18NService i18NService,
			final TmaPriceDataFactory priceDataFactory)
	{
		this.propSource = propSource;
		this.i18NService = i18NService;
		this.priceDataFactory = priceDataFactory;
	}

	@Override
	public void populate(final S source, final T target) throws ConversionException
	{
		final SubscriptionPricePlanData subscriptionPricePlanData = new SubscriptionPricePlanData();

		final Double recurringCharge = getPropSource().getValue(source, "priceRcFirst");
		if (recurringCharge != null)
		{
			final RecurringChargeEntryData recurringChargeEntryData = new RecurringChargeEntryData();
			final CurrencyModel currency = i18NService.getCurrentCurrency();
			recurringChargeEntryData
					.setPrice(priceDataFactory.create(PriceDataType.FROM, BigDecimal.valueOf(recurringCharge), currency.getIsocode()));
			subscriptionPricePlanData.setRecurringChargeEntries(Collections.singletonList(recurringChargeEntryData));
		}

		final Double oneTimeCharge = getPropSource().getValue(source, "priceOtc");
		if (oneTimeCharge != null)
		{
			final OneTimeChargeEntryData oneTimeChargeEntryData = new OneTimeChargeEntryData();
			final CurrencyModel currency = i18NService.getCurrentCurrency();
			oneTimeChargeEntryData
					.setPrice(priceDataFactory.create(PriceDataType.FROM, BigDecimal.valueOf(oneTimeCharge), currency.getIsocode()));
			subscriptionPricePlanData.setOneTimeChargeEntries(Collections.singletonList(oneTimeChargeEntryData));
		}
		populateRegionData(source, subscriptionPricePlanData);
		target.setProductOfferingPrices(Collections.singletonList(subscriptionPricePlanData));
	}

	private void populateRegionData(final S source, final SubscriptionPricePlanData subscriptionPricePlanData)
	{
		final ArrayList<String> regionIsoCodes = getPropSource().getValue(source, "region");
		if (CollectionUtils.isEmpty(regionIsoCodes) || StringUtils.equals(regionIsoCodes.get(0), ALL_REGION_PRICES))
		{
			return;
		}

		final ArrayList<RegionData> regionDatas = new ArrayList<>();
		for (final String isoCode : regionIsoCodes)
		{
			final RegionData region = new RegionData();
			region.setIsocode(isoCode);
			regionDatas.add(region);
		}
		subscriptionPricePlanData.setRegions(regionDatas);
	}

	protected TmaSolrPropSource getPropSource()
	{
		return propSource;
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	protected TmaPriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}
}
