/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OneTimePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsagePriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RecurringPriceCharge;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This attribute Mapper class maps data for bundledPop attribute between {@link SubscriptionPricePlanData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceBundledPopAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (!(source instanceof SubscriptionPricePlanData))
		{
			return;
		}
		final SubscriptionPricePlanData sppData = (SubscriptionPricePlanData) source;

		if (CollectionUtils.isEmpty(sppData.getOneTimeChargeEntries()) && CollectionUtils
				.isEmpty(sppData.getRecurringChargeEntries())
				&& CollectionUtils.isEmpty(sppData.getUsageCharges()))
		{
			target.setIsBundle(false);
			return;
		}

		final List<ProductOfferingPrice> bundledPrices = new ArrayList<>();
		bundledPrices.addAll(getOneTimeCharges(sppData, context));
		bundledPrices.addAll(getRecurringCharges(sppData, context));
		bundledPrices.addAll(getUsageCharges(sppData, context));

		target.setBundledPop(bundledPrices);
		target.setIsBundle(true);
	}

	private Collection<UsagePriceCharge> getUsageCharges(SubscriptionPricePlanData subscriptionPricePlanData,
			MappingContext context)
	{
		final List<UsagePriceCharge> usageCharges = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlanData.getUsageCharges()))
		{
			return usageCharges;
		}

		subscriptionPricePlanData.getUsageCharges().forEach(usageChargeData ->
		{
			final UsagePriceCharge charge = getMapperFacade().map(usageChargeData, UsagePriceCharge.class, context);
			if (subscriptionPricePlanData.getSubscriptionTerms() != null)
			{
				final SubscriptionTermData subscriptionTermData = subscriptionPricePlanData.getSubscriptionTerms().get(0);
				charge.setRecurringChargePeriodType(subscriptionTermData.getTermOfServiceFrequency().getCode());
				charge.setRecurringChargePeriodLength(subscriptionTermData.getTermOfServiceNumber());
			}
			usageCharges.add(charge);
		});

		return usageCharges;
	}

	private List<OneTimePriceCharge> getOneTimeCharges(SubscriptionPricePlanData subscriptionPricePlan, MappingContext context)
	{
		final List<OneTimePriceCharge> oneTimeCharges = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlan.getOneTimeChargeEntries()))
		{
			return oneTimeCharges;
		}

		subscriptionPricePlan.getOneTimeChargeEntries().forEach(oneTimeChargeEntryData ->
		{
			final OneTimePriceCharge charge = getMapperFacade().map(oneTimeChargeEntryData, OneTimePriceCharge.class, context);
			oneTimeCharges.add(charge);
		});

		return oneTimeCharges;
	}

	private List<RecurringPriceCharge> getRecurringCharges(SubscriptionPricePlanData subscriptionPricePlan,
			MappingContext context)
	{
		final List<RecurringPriceCharge> recurringPrices = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlan.getRecurringChargeEntries()))
		{
			return recurringPrices;
		}

		subscriptionPricePlan.getRecurringChargeEntries().forEach(recurringChargeEntryData ->
		{
			final RecurringPriceCharge recurringPrice = getMapperFacade().map(recurringChargeEntryData,
					RecurringPriceCharge.class, context);

			if (subscriptionPricePlan.getSubscriptionTerms() != null)
			{
				final SubscriptionTermData subscriptionTermData = subscriptionPricePlan.getSubscriptionTerms().get(0);
				recurringPrice.setRecurringChargePeriodType(subscriptionTermData.getTermOfServiceFrequency().getCode());
				recurringPrice.setRecurringChargePeriodLength(subscriptionTermData.getTermOfServiceNumber());
			}

			recurringPrices.add(recurringPrice);
		});

		return recurringPrices;

	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
