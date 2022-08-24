/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaFindAbstractOrderEntryPriceStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.orderentry.TmaAbstractOrderEntryPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link TmaFindAbstractOrderEntryPriceStrategy}  to find the price for an order entry
 *
 * @since 1907
 */
public class DefaultTmaFindAbstractOrderEntryPriceStrategy implements TmaFindAbstractOrderEntryPriceStrategy
{
	private TmaCommercePriceService commercePriceService;
	private ModelService modelService;
	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	private KeyGenerator keyGenerator;
	private List<TmaAbstractOrderEntryPriceBuilder> priceBuilders;

	@Override
	public TmaAbstractOrderPriceModel findBasePrice(final AbstractOrderEntryModel entry, final List<TaxValue> taxes)
	{
		final PriceRowModel bestApplicablePrice = getCommercePriceService().getBestApplicablePrice(entry);
		final TmaAbstractOrderCompositePriceModel price = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		final Set<TmaAbstractOrderPriceModel> childPrices = new HashSet<>();
		getPriceBuilders().stream()
				.map(priceBuilder -> priceBuilder.buildPrices(bestApplicablePrice, entry, taxes))
				.forEach(childPrices::addAll);
		price.setChildPrices(childPrices);
		getModelService().save(price);

		return price;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected List<TmaAbstractOrderEntryPriceBuilder> getPriceBuilders()
	{
		return priceBuilders;
	}

	@Required
	public void setPriceBuilders(List<TmaAbstractOrderEntryPriceBuilder> priceBuilders)
	{
		this.priceBuilders = priceBuilders;
	}

	/**
	 * @return
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	/**
	 * @param keyGenerator
	 * @deprecated since 2007
	 */
	@Required
	@Deprecated(since = "2007")
	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}
}

