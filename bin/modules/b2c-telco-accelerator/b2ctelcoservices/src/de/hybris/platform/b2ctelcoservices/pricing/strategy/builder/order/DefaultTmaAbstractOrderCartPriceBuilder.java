/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderComponentPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.TaxValue;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaAbstractOrderPriceBuilder} responsible for computing the price values.
 *
 * @since 2007
 */
public abstract class DefaultTmaAbstractOrderCartPriceBuilder implements TmaAbstractOrderPriceBuilder
{
	private TmaBillingTimeService billingTimeService;
	private TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy;

	/**
	 * Retrieves the tax rate on the given cart price considering the given taxes.
	 *
	 * @param price
	 * 		the price value to be set on the order price
	 * @param order
	 * 		the order for which the price is created
	 * @param taxes
	 * 		the taxes to be applied on the order price
	 */
	protected Double retrieveTaxRate(final Double price, final AbstractOrderModel order,
			final List<TaxValue> taxes)
	{
		return getTmaRetrieveTaxRateStrategy().retrieveTaxRate(price, order, taxes);
	}

	/**
	 * Populates the price values on the given cart price by taking into account the tax rate
	 *
	 * @param price
	 * 		the price value to be set on the order price
	 * @param order
	 * 		the order for which the price is created
	 * @param orderPrice
	 * 		the order price for which price values will be updated
	 * @param taxRate
	 * 		the tax rate to be considered when computing the price values
	 */
	protected void setPriceValues(final Double price, final AbstractOrderModel order,
			final TmaAbstractOrderComponentPriceModel orderPrice, final Double taxRate)
	{
		final Integer digits = order.getCurrency().getDigits();
		if (BooleanUtils.isTrue(order.getNet()))
		{
			orderPrice.setDutyFreeAmount(price);
			double taxIncludedAmount = CoreAlgorithms.round(price + taxRate, digits);
			orderPrice.setTaxIncludedAmount(taxIncludedAmount);
		}
		else
		{
			final double dutyFreeAmount = CoreAlgorithms.round(price - taxRate, digits);
			orderPrice.setDutyFreeAmount(dutyFreeAmount);
			orderPrice.setTaxIncludedAmount(price);
		}

		orderPrice.setTaxRate(taxRate);
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

	protected TmaRetrieveTaxRateStrategy getTmaRetrieveTaxRateStrategy()
	{
		return tmaRetrieveTaxRateStrategy;
	}

	@Required
	public void setTmaRetrieveTaxRateStrategy(final TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		this.tmaRetrieveTaxRateStrategy = tmaRetrieveTaxRateStrategy;
	}
}
