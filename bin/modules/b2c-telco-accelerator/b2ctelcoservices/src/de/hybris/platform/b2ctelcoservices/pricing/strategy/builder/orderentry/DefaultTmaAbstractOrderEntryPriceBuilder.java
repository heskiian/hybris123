/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.ChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;
import de.hybris.platform.util.TaxValue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract builder for defining a template used to create a list
 * of {@link TmaAbstractOrderPriceModel} to be set on the order entry
 *
 * @since 1907
 */
public abstract class DefaultTmaAbstractOrderEntryPriceBuilder implements TmaAbstractOrderEntryPriceBuilder
{
	private ModelService modelService;
	private KeyGenerator keyGenerator;
	private TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy;
	private TmaSubscriptionTermService subscriptionTermService;

	@Override
	public List<TmaAbstractOrderPriceModel> buildPrices(final PriceRowModel price, final AbstractOrderEntryModel entry,
			final List<TaxValue> taxes)
	{
		if (shouldCreatePrices(price))
		{
			return createPrices(price, entry, taxes);
		}
		return Collections.emptyList();
	}

	/**
	 * Verify is the list of prices for order entry should be created
	 *
	 * @param price
	 * 		the priceCharge based on which the order entry priceCharge should be created
	 * @return Returns true if the condition that verifies if prices
	 * should be created is fulfilled, otherwise false
	 */
	protected abstract boolean shouldCreatePrices(final PriceRowModel price);

	/**
	 * Creates a list of {@link TmaAbstractOrderPriceModel} based on the param.
	 *
	 * @param price
	 * 		the priceCharge used to create and populate the prices list
	 * @param entry
	 * 		the entry for which the priceCharge is computed
	 * @param taxes
	 * 		the taxes that should be applied on the order entry priceCharge
	 * @return the created and populated priceCharge list
	 */
	protected abstract List<TmaAbstractOrderPriceModel> createPrices(final PriceRowModel price,
			final AbstractOrderEntryModel entry, final List<TaxValue> taxes);

	/**
	 * Populates the price values for the order entry price charge. If the price is net then the taxes are added
	 * to the duty free amount value, otherwise the taxes are subtracted from the duty free amount value
	 *
	 * @param price
	 * 		the price value to be set on the charge
	 * @param entry
	 * 		the entry for which the price charge is populated
	 * @param taxes
	 * 		the taxes to be applied
	 * @param priceCharge
	 * 		the charge for which the price is populated
	 */
	protected void populatePriceValues(final Double price, final AbstractOrderEntryModel entry, final List<TaxValue> taxes,
			final TmaAbstractOrderChargePriceModel priceCharge)
	{
		final Boolean isNet = entry.getOrder().getNet();
		final Integer digits = entry.getOrder().getCurrency().getDigits();

		final Double taxRate = getTmaRetrieveTaxRateStrategy().retrieveTaxRate(price, entry, taxes);
		if (BooleanUtils.isTrue(isNet))
		{
			priceCharge.setDutyFreeAmount(price);
			final double taxIncludedAmount = CoreAlgorithms.round(price + taxRate, digits);
			priceCharge.setTaxIncludedAmount(taxIncludedAmount);
		}
		else
		{
			final double dutyFreeAmount = CoreAlgorithms.round(price - taxRate, digits);
			priceCharge.setDutyFreeAmount(dutyFreeAmount);
			priceCharge.setTaxIncludedAmount(price);
		}
		priceCharge.setTaxRate(taxRate);
		priceCharge.setCurrency(entry.getOrder().getCurrency());
	}

	/**
	 * Populates the {@link TmaAbstractOrderChargePriceModel} with the billing time information.
	 *
	 * @param chargeEntry
	 * 		the {@link ChargeEntryModel} from which the billing time is retrieved in case it exists
	 * @param priceCharge
	 * 		the charge price for which the billing time will be populated
	 */
	protected void populateBillingTime(final ChargeEntryModel chargeEntry, final TmaAbstractOrderChargePriceModel priceCharge)
	{
		if (chargeEntry.getBillingTime() != null)
		{
			priceCharge.setBillingTime(chargeEntry.getBillingTime());
		}
		else
		{
			priceCharge.setBillingTime(retrieveDefaultBillingTime());
		}
	}

	/**
	 * Retrieves the {@link BillingTimeModel} of the default configured subscription term.
	 *
	 * @return the {@link BillingTimeModel} of the default term if any configured.
	 */
	protected BillingTimeModel retrieveDefaultBillingTime()
	{
		final SubscriptionTermModel defaultSubscriptionTerm = getSubscriptionTermService().getDefaultSubscriptionTerm();
		if (defaultSubscriptionTerm != null)
		{
			return defaultSubscriptionTerm.getBillingPlan().getBillingFrequency();
		}

		return null;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
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

	@Required
	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}

	public void setSubscriptionTermService(final TmaSubscriptionTermService subscriptionTermService)
	{
		this.subscriptionTermService = subscriptionTermService;
	}
}
