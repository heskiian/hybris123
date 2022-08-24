/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.*;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * The TmaPriceBundledPOPAttributeMapper class maps data for BPO price attribute between
 * {@link SubscriptionPricePlanData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceBundledPOPAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if(!(source instanceof SubscriptionPricePlanData)){
			return;
		}

		final SubscriptionPricePlanData subscriptionPricePlanData = (SubscriptionPricePlanData) source;

		if (CollectionUtils.isNotEmpty(subscriptionPricePlanData.getOneTimeChargeEntries())
				|| CollectionUtils.isNotEmpty(subscriptionPricePlanData.getRecurringChargeEntries())
				|| CollectionUtils.isNotEmpty(subscriptionPricePlanData.getUsageCharges()))
		{
			final List<ProductOfferingPriceWsDTO> bundledPrices = new ArrayList<>();

			bundledPrices.addAll(getOneTimeCharges(subscriptionPricePlanData, context));
			bundledPrices.addAll(getRecurringCharges(subscriptionPricePlanData, context));
			bundledPrices.addAll(getUsageCharges(subscriptionPricePlanData, context));

			target.setBundledPop(bundledPrices);
			target.setIsBundle(true);

		}
		else
		{
			target.setIsBundle(false);

			if (StringUtils.isEmpty(subscriptionPricePlanData.getCurrencyIso()) && subscriptionPricePlanData.getValue() == null)
			{
				return;
			}
			final MoneyWsDTO price = new MoneyWsDTO();
			price.setValue(subscriptionPricePlanData.getValue().toString());
			price.setCurrencyIso(subscriptionPricePlanData.getCurrencyIso());
			target.setPrice(price);
		}
	}


	private Collection<UsagePriceChargeWsDTO> getUsageCharges(final SubscriptionPricePlanData subscriptionPricePlanData,
			final MappingContext context)
	{
		final List<UsagePriceChargeWsDTO> usageCharges = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlanData.getUsageCharges()))
		{
			return usageCharges;
		}

		subscriptionPricePlanData.getUsageCharges().forEach(usageChargeData ->
		{
			final UsagePriceChargeWsDTO charge = getMapperFacade().map(usageChargeData, UsagePriceChargeWsDTO.class, context);
			usageCharges.add(charge);
		});

		return usageCharges;
	}



	private List<OneTimePriceChargeWsDTO> getOneTimeCharges(final SubscriptionPricePlanData subscriptionPricePlan,
			final MappingContext context)
	{
		final List<OneTimePriceChargeWsDTO> oneTimeCharges = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlan.getOneTimeChargeEntries()))
		{
			return oneTimeCharges;
		}

		subscriptionPricePlan.getOneTimeChargeEntries().forEach(oneTimeChargeEntryData ->
		{
			final OneTimePriceChargeWsDTO charge = getMapperFacade().map(oneTimeChargeEntryData, OneTimePriceChargeWsDTO.class,
					context);
			oneTimeCharges.add(charge);
		});

		return oneTimeCharges;
	}


	private List<RecurringPriceChargeWsDTO> getRecurringCharges(final SubscriptionPricePlanData subscriptionPricePlan,
			final MappingContext context)
	{
		final List<RecurringPriceChargeWsDTO> recurringPrices = new ArrayList<>();
		if (CollectionUtils.isEmpty(subscriptionPricePlan.getRecurringChargeEntries()))
		{
			return recurringPrices;
		}

		subscriptionPricePlan.getRecurringChargeEntries().forEach(recurringChargeEntryData ->
		{
			final RecurringPriceChargeWsDTO recurringPrice = getMapperFacade().map(recurringChargeEntryData,
					RecurringPriceChargeWsDTO.class, context);

			if (subscriptionPricePlan.getSubscriptionTerms() != null)
			{
				final SubscriptionTermData subscriptionTermData = subscriptionPricePlan.getSubscriptionTerms().get(0);
				recurringPrice.setRecurringChargePeriodType(subscriptionTermData.getTermOfServiceFrequency().getCode());
				recurringPrice.setRecurringChargePeriodLength((long) subscriptionTermData.getTermOfServiceNumber());
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
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

}
