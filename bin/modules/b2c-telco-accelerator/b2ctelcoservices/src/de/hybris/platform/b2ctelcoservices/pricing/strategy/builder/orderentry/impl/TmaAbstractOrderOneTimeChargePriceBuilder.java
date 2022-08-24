/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPriceBuilder;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Builder that creates a list of {@link TmaAbstractOrderPriceModel}
 * from the list of one time charges set on the price
 *
 * @since 1907
 */
public class TmaAbstractOrderOneTimeChargePriceBuilder extends DefaultTmaAbstractOrderEntryPriceBuilder
{
	private TmaBillingTimeService billingTimeService;

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	private ConfigurationService configurationService;

	@Override
	protected boolean shouldCreatePrices(final PriceRowModel price)
	{
		return price instanceof SubscriptionPricePlanModel;
	}

	@Override
	protected List<TmaAbstractOrderPriceModel> createPrices(final PriceRowModel price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		return createPrices((SubscriptionPricePlanModel) price, entry, taxes);
	}

	protected List<TmaAbstractOrderPriceModel> createPrices(final SubscriptionPricePlanModel price,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final List<TmaAbstractOrderPriceModel> oneTimeChargePrices = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(price.getOneTimeChargeEntries()))
		{
			price.getOneTimeChargeEntries().forEach(oneTimeChargeEntry ->
			{
				final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePrice =
						createAbstractOrderOneTimeChargePrice(oneTimeChargeEntry.getPrice(), oneTimeChargeEntry.getBillingEvent(),
								entry, taxes);
				oneTimeChargePrices.add(oneTimeChargePrice);
			});
		}

		return oneTimeChargePrices;
	}

	/**
	 * @deprecated since 2007. Use instead {@link
	 * de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl.TmaAbstractOrderPriceValueBuilder }
	 */
	@Deprecated(since = "2007")
	protected List<TmaAbstractOrderPriceModel> createPricesForPriceRow(final PriceRowModel price,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePrice =
				createAbstractOrderOneTimeChargePrice(price.getPrice(), getBillingTimeService().getDefaultBillingTime(), entry,
						taxes);
		return Collections.singletonList(oneTimeChargePrice);
	}

	protected TmaAbstractOrderOneTimeChargePriceModel createAbstractOrderOneTimeChargePrice(final Double price,
			final BillingTimeModel billingTime, final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePrice = getModelService()
				.create(TmaAbstractOrderOneTimeChargePriceModel.class);
		oneTimeChargePrice.setId((String) getKeyGenerator().generate());
		populatePriceValues(price, entry, taxes, oneTimeChargePrice);
		oneTimeChargePrice.setBillingTime(billingTime);
		oneTimeChargePrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		getModelService().save(oneTimeChargePrice);
		return oneTimeChargePrice;
	}

	private boolean priceIsSubscriptionPricePlan(PriceRowModel price)
	{
		return price instanceof SubscriptionPricePlanModel;
	}

	protected TmaBillingTimeService getBillingTimeService()
	{
		return billingTimeService;
	}

	@Required
	public void setBillingTimeService(TmaBillingTimeService billingTimeService)
	{
		this.billingTimeService = billingTimeService;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @deprecated since 2007
	 */
	@Required
	@Deprecated(since = "2007")
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
