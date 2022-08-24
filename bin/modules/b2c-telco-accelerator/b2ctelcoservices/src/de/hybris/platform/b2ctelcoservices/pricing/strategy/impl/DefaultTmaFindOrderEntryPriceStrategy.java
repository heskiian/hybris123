/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaComponentProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaOneTimeProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProdOfferPriceAlterationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.impl.FindPricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;


/**
 * Custom implementation for finding the base price for a given entry.
 *
 * @since 2007
 */
public class DefaultTmaFindOrderEntryPriceStrategy extends FindPricingWithCurrentPriceFactoryStrategy
{
	private static final int ONE_HUNDRED = 100;
	private TmaCommercePriceService commercePriceService; //NOSONAR
	private TmaBillingTimeService billingTimeService; //NOSONAR

	public DefaultTmaFindOrderEntryPriceStrategy(final TmaCommercePriceService commercePriceService,
			final TmaBillingTimeService billingTimeService)
	{
		this.commercePriceService = commercePriceService;
		this.billingTimeService = billingTimeService;
	}

	/**
	 * Determines the base price for the given {@param entry}.
	 * The logic iterates over the entire {@link PriceRowModel#getProductOfferingPrice()} hierarchy and
	 * sums up the one time charges with pay on checkout price event.
	 *
	 * @param entry
	 * 		the entry for which the base price is computed
	 * @return the base price amount
	 * @throws CalculationException
	 * 		in case no price is available for the given {@param entry}
	 */
	@Override
	public PriceValue findBasePrice(AbstractOrderEntryModel entry) throws CalculationException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("entry", entry);
		final PriceRowModel bestApplicablePrice = getCommercePriceService().getBestApplicablePrice(entry);

		if (bestApplicablePrice == null)
		{
			throw new CalculationException("Found no price for order entry " + entry.getInfo());
		}

		final AbstractOrderModel order = entry.getOrder();

		if (bestApplicablePrice.getProductOfferingPrice() != null)
		{
			return new PriceValue(order.getCurrency().getIsocode(), getPriceAmount(
					bestApplicablePrice.getProductOfferingPrice(), getBillingTimeService().getDefaultBillingTime(), new ArrayList<>()),
					order.getNet());
		}

		return new PriceValue(order.getCurrency().getIsocode(), bestApplicablePrice.getPrice(), order.getNet());
	}

	/**
	 * Returns the price amount of the given {@param productOfferingPrice} for the {@param billingTime}
	 *
	 * @param productOfferingPrice
	 * 		the price from which the amount of all one time charges is determined
	 * @param billingTime
	 * 		the billing time against which the {@param productOfferingPrice} price event is checked
	 * @param discounts
	 * 		the discounts to be applied to the price
	 * @return the price amount for the given {@param billingTime}
	 */
	protected Double getPriceAmount(final TmaProductOfferingPriceModel productOfferingPrice,
			final BillingTimeModel billingTime, final List<TmaProdOfferPriceAlterationModel> discounts)
	{
		Double priceAmount = 0.0;
		if (doesPriceMatchEvent(productOfferingPrice, billingTime))
		{
			priceAmount += applyDiscounts(((TmaComponentProdOfferPriceModel) productOfferingPrice).getValue(), discounts);
		}

		if (productOfferingPrice instanceof TmaCompositeProdOfferPriceModel)
		{
			for (TmaProductOfferingPriceModel childPop : ((TmaCompositeProdOfferPriceModel) productOfferingPrice).getChildren())
			{
				if (childPop instanceof TmaProdOfferPriceAlterationModel &&  billingTime.getCode()
						.equalsIgnoreCase(childPop.getPriceEvent().getCode()))
				{
					discounts.add((TmaProdOfferPriceAlterationModel) childPop);
				}
			}
			for (TmaProductOfferingPriceModel childPop : ((TmaCompositeProdOfferPriceModel) productOfferingPrice).getChildren())
			{
				priceAmount += getPriceAmount(childPop, billingTime, new ArrayList<>(discounts));
			}
		}
		return priceAmount;
	}

	/**
	 * Applies a list of discounts to the given price input. The discount list is iterated in reverse order.
	 *
	 * @param price
	 * 		The price to apply the discounts to.
	 * @param discounts
	 * 		The discounts to be applied.
	 * @return the discounted price.
	 */
	private Double applyDiscounts(Double price, final List<TmaProdOfferPriceAlterationModel> discounts)
	{
		if (CollectionUtils.isEmpty(discounts))
		{
			return price;
		}
		for (ListIterator iterator = discounts.listIterator(discounts.size()); iterator.hasPrevious(); )
		{
			final TmaProdOfferPriceAlterationModel discount = (TmaProdOfferPriceAlterationModel) iterator.previous();
			if (discount.getIsPercentage())
			{
				price = price - price * (discount.getValue() / ONE_HUNDRED);
			}
			else
			{
				price = price - discount.getValue();
			}
			if (price <= 0)
			{
				return 0.0;
			}
		}
		return price;
	}

	/**
	 * Determines whether the price event for the given {@param productOfferingPrice} matches the given {@param billingTimeModel}
	 *
	 * @param productOfferingPrice
	 * 		the price from which the amount of all one time charges is determined
	 * @param billingTimeModel
	 * 		the billing time against which the {@param productOfferingPrice} price event is checked
	 * @return <code>true</code> if the price event matches, <code>false</code> otherwise
	 */
	private boolean doesPriceMatchEvent(final TmaProductOfferingPriceModel productOfferingPrice,
			final BillingTimeModel billingTimeModel)
	{
		return productOfferingPrice instanceof TmaOneTimeProdOfferPriceChargeModel && billingTimeModel.getCode()
				.equalsIgnoreCase(productOfferingPrice.getPriceEvent().getCode());
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	protected TmaBillingTimeService getBillingTimeService()
	{
		return billingTimeService;
	}

}
