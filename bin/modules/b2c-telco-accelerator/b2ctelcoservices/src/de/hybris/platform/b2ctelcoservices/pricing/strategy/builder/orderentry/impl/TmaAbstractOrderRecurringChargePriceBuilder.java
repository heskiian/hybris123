/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * Builder that creates a list of {@link TmaAbstractOrderPriceModel} from the list of recurring charges set on the price.
 *
 * @since 1907
 */
public class TmaAbstractOrderRecurringChargePriceBuilder extends DefaultTmaAbstractOrderEntryPriceBuilder
{
	@Override
	protected boolean shouldCreatePrices(PriceRowModel price)
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
		final List<TmaAbstractOrderPriceModel> recurringChargePrices = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(price.getRecurringChargeEntries()))
		{
   		price.getRecurringChargeEntries().forEach(recurringChargeEntry -> {
   			final TmaAbstractOrderRecurringChargePriceModel recurringChargePrice = getModelService()
   					.create(TmaAbstractOrderRecurringChargePriceModel.class);
   			recurringChargePrice.setId((String) getKeyGenerator().generate());
   			populatePriceValues(recurringChargeEntry.getPrice(), entry, taxes, recurringChargePrice);
   			populateBillingTime(recurringChargeEntry, recurringChargePrice);
   			recurringChargePrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
   			recurringChargePrice.setCycleStart(recurringChargeEntry.getCycleStart());
   			recurringChargePrice.setCycleEnd(recurringChargeEntry.getCycleEnd());
   			getModelService().save(recurringChargePrice);
   			recurringChargePrices.add(recurringChargePrice);
   		});
		}
		return recurringChargePrices;
	}
}
