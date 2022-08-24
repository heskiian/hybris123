/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.factory.impl;

import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.impl.DefaultTmaFindOrderEntryPriceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.impl.FindPricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.subscription.BillingTimeService;
import de.hybris.platform.subscriptionservices.subscription.impl.FindSubscriptionPricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.util.PriceValue;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;



/**
 * Custom implementation of the {@link FindPricingWithCurrentPriceFactoryStrategy} for applying price override values
 * for the calculation of the cart.
 *
 * @since 6.7
 * @deprecated since 2007. Use instead {@link DefaultTmaFindOrderEntryPriceStrategy}
 */
@Deprecated(since = "2007")
public class DefaultTmaFindPriceStrategy extends FindSubscriptionPricingWithCurrentPriceFactoryStrategy
{
	private BillingTimeService billingTimeService; //NOSONAR
	private ConfigurationService configurationService;//NOSONAR

	@Override
	@Nonnull
	public PriceValue findBasePrice(@Nonnull final AbstractOrderEntryModel entry) throws CalculationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		final PriceRowModel minimumPrice = getCommercePriceService().getBestApplicablePrice(entry);

		if (minimumPrice == null)
		{
			throw new CalculationException("Found no price for order entry " + entry.getInfo());
		}

		final AbstractOrderModel order = entry.getOrder();
		if (minimumPrice instanceof SubscriptionPricePlanModel)
		{
			return new PriceValue(order.getCurrency().getIsocode(), getOneTimeChargePriceValue(
					(SubscriptionPricePlanModel) minimumPrice, getBillingTimeForPayNow()), order.getNet());
		}

		return new PriceValue(order.getCurrency().getIsocode(), minimumPrice.getPrice(), order.getNet());
	}

	/**
	 * Returns the one time charge of the given {@param subscriptionPricePlanModel} for the {@param billingTime}
	 * in case any is found, 0 otherwise.
	 *
	 * @param subscriptionPricePlanModel
	 * 		the price from which the one time charge is determined
	 * @return the one time charge price value
	 */
	protected Double getOneTimeChargePriceValue(final SubscriptionPricePlanModel subscriptionPricePlanModel,
			final BillingTimeModel billingTime)
	{
		final Collection<OneTimeChargeEntryModel> oneTimeChargeEntries = subscriptionPricePlanModel.getOneTimeChargeEntries();
		if (CollectionUtils.isNotEmpty(oneTimeChargeEntries))
		{
			final Optional<OneTimeChargeEntryModel> oneTimeChargeForBillingEvent = oneTimeChargeEntries.stream()
					.filter(chargeEntry -> billingTime.equals(chargeEntry.getBillingEvent())).findFirst();
			if (oneTimeChargeForBillingEvent.isPresent())
			{
				return oneTimeChargeForBillingEvent.get().getPrice();
			}
		}

		return 0.0D;
	}

	protected BillingTimeModel getBillingTimeForPayNow()
	{
		return getBillingTimeService().getDefaultBillingTime();
	}

	@Override
	protected TmaCommercePriceService getCommercePriceService()
	{
		return (TmaCommercePriceService) super.getCommercePriceService();
	}

	public TmaBillingTimeService getBillingTimeService()
	{
		return (TmaBillingTimeService) billingTimeService;
	}

	@Required
	public void setBillingTimeService(BillingTimeService billingTimeService)
	{
		this.billingTimeService = billingTimeService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
