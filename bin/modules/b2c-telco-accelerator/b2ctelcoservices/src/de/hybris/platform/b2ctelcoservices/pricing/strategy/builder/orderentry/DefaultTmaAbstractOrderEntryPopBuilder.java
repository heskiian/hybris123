/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderComponentPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveTaxRateStrategy;
import de.hybris.platform.core.CoreAlgorithms;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;


/**
 * Abstract builder used for creation of a {@link TmaAbstractOrderPriceModel} to be set on the order entry
 *
 * @since 2007
 */
public abstract class DefaultTmaAbstractOrderEntryPopBuilder<T extends TmaProductOfferingPriceModel>
		implements TmaAbstractOrderEntryPopPriceBuilder<T>
{
	private final ModelService modelService;
	private final TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy;

	public DefaultTmaAbstractOrderEntryPopBuilder(ModelService modelService, TmaRetrieveTaxRateStrategy tmaRetrieveTaxRateStrategy)
	{
		this.modelService = modelService;
		this.tmaRetrieveTaxRateStrategy = tmaRetrieveTaxRateStrategy;
	}

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
	 * @param priceComponent
	 * 		the component for which the price is populated
	 */
	protected void populatePriceValues(final Double price, final AbstractOrderEntryModel entry, final List<TaxValue> taxes,
			final TmaAbstractOrderComponentPriceModel priceComponent)
	{
		final Boolean isNet = entry.getOrder().getNet();
		final Integer digits = entry.getOrder().getCurrency().getDigits();

		final Double taxRate = getTmaRetrieveTaxRateStrategy().retrieveTaxRate(price, entry, taxes);
		if (BooleanUtils.isTrue(isNet))
		{
			priceComponent.setDutyFreeAmount(price);
			final double taxIncludedAmount = CoreAlgorithms.round(price + taxRate, digits);
			priceComponent.setTaxIncludedAmount(taxIncludedAmount);
		}
		else
		{
			final double dutyFreeAmount = CoreAlgorithms.round(price - taxRate, digits);
			priceComponent.setDutyFreeAmount(dutyFreeAmount);
			priceComponent.setTaxIncludedAmount(price);
		}
		priceComponent.setTaxRate(taxRate);
		priceComponent.setCurrency(entry.getOrder().getCurrency());
	}

	/**
	 * Populates the {@link TmaAbstractOrderPriceModel#getCatalogCode()} with {@link TmaProductOfferingPriceModel#getId()} value.
	 *
	 * @param source
	 * 		the product offering price based on which the catalog code will be populated
	 * @param target
	 * 		the cart price for which the catalog code will be populated
	 */
	protected void populateCatalogCode(final TmaProductOfferingPriceModel source, final TmaAbstractOrderPriceModel target)
	{
		target.setCatalogCode(source.getId());
	}

	protected void populateName(final TmaProductOfferingPriceModel source, final TmaAbstractOrderPriceModel target)
	{
		target.setName(source.getName());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TmaRetrieveTaxRateStrategy getTmaRetrieveTaxRateStrategy()
	{
		return tmaRetrieveTaxRateStrategy;
	}
}
