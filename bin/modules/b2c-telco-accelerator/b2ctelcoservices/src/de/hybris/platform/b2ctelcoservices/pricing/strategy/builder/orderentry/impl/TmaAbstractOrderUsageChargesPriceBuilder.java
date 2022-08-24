/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderTierUsageCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.DefaultTmaAbstractOrderEntryPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.PerUnitUsageChargeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.TierUsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Builder that creates a list of {@link TmaAbstractOrderPriceModel}
 * from the list of usage charges set on the price
 *
 * @since 2007
 * @deprecated since 2007. The usageChargeType is now set on the parent composite charge not on the entry.
 */
@Deprecated(since = "2007")
public class TmaAbstractOrderUsageChargesPriceBuilder extends DefaultTmaAbstractOrderEntryPriceBuilder
{
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
		final List<TmaAbstractOrderPriceModel> usageChargePrices = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(price.getUsageCharges()))
		{
			price.getUsageCharges().forEach(usageCharge ->
			{
				final TmaAbstractOrderCompositePriceModel usageChargePrice;

				if (usageCharge instanceof PerUnitUsageChargeModel)
				{
					usageChargePrice = getModelService().create(TmaAbstractOrderTierUsageCompositePriceModel.class);
				}
				else
				{
					usageChargePrice = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
				}
				final Set<TmaAbstractOrderPriceModel> usageChargeChildPrices = new HashSet<>();
				usageChargePrice.setId((String) getKeyGenerator().generate());
				usageCharge.getUsageChargeEntries().forEach(usageChargeEntry ->
				{
					final TmaAbstractOrderUsageChargePriceModel usageChargeEntryPrice =
							createAbstractOrderUsageChargePrice(usageChargeEntry, entry, taxes);
					usageChargeChildPrices.add(usageChargeEntryPrice);
				});

				usageChargePrice.setChildPrices(usageChargeChildPrices);
				getModelService().save(usageChargePrice);

				usageChargePrices.add(usageChargePrice);
			});
		}
		return usageChargePrices;
	}

	protected TmaAbstractOrderUsageChargePriceModel createAbstractOrderUsageChargePrice(
			final UsageChargeEntryModel usageChargeEntry, final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final UsageChargeModel usageCharge = usageChargeEntry.getUsageCharge();

		final TmaAbstractOrderUsageChargePriceModel usageChargeEntryPrice = getModelService()
				.create(TmaAbstractOrderUsageChargePriceModel.class);
		usageChargeEntryPrice.setId((String) getKeyGenerator().generate());
		usageChargeEntryPrice.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		populatePriceValues(usageChargeEntry.getPrice(), entry, taxes, usageChargeEntryPrice);
		populateBillingTime(usageChargeEntry, usageChargeEntryPrice);

		usageChargeEntryPrice.setUsageUnit(usageCharge.getUsageUnit());
		if (usageCharge instanceof PerUnitUsageChargeModel)
		{
			usageChargeEntryPrice.setUsageChargeType(((PerUnitUsageChargeModel) usageCharge).getUsageChargeType());
		}
		if (usageChargeEntry instanceof TierUsageChargeEntryModel)
		{
			usageChargeEntryPrice.setTierStart(((TierUsageChargeEntryModel) usageChargeEntry).getTierStart());
			usageChargeEntryPrice.setTierEnd(((TierUsageChargeEntryModel) usageChargeEntry).getTierEnd());
		}
		getModelService().save(usageChargeEntryPrice);
		return usageChargeEntryPrice;
	}
}
