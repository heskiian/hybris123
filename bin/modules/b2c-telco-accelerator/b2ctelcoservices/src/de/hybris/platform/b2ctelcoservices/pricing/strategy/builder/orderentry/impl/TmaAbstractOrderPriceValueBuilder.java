/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
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
import de.hybris.platform.util.TaxValue;

import java.util.Collections;
import java.util.List;


/**
 * Builder that creates a {@link TmaAbstractOrderPriceModel}from the price value set on the price
 *
 * @since 2007
 */
public class TmaAbstractOrderPriceValueBuilder extends DefaultTmaAbstractOrderEntryPriceBuilder
{
	private TmaBillingTimeService billingTimeService;
	private ConfigurationService configurationService;

	public TmaAbstractOrderPriceValueBuilder(TmaBillingTimeService billingTimeService, ConfigurationService configurationService)
	{
		this.billingTimeService = billingTimeService;
		this.configurationService = configurationService;
	}

	@Override
	protected boolean shouldCreatePrices(final PriceRowModel price)
	{
		return price.getPrice() != 0;
	}

	@Override
	protected List<TmaAbstractOrderPriceModel> createPrices(final PriceRowModel price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePrice = getModelService()
				.create(TmaAbstractOrderOneTimeChargePriceModel.class);
		populatePriceValues(price.getPrice(), entry, taxes, oneTimeChargePrice);
		oneTimeChargePrice.setBillingTime(getBillingTimeService().getDefaultBillingTime());
		oneTimeChargePrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		oneTimeChargePrice.setCatalogCode(price.getCode());
		getModelService().save(oneTimeChargePrice);
		return Collections.singletonList(oneTimeChargePrice);
	}

	protected TmaBillingTimeService getBillingTimeService()
	{
		return billingTimeService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

}
