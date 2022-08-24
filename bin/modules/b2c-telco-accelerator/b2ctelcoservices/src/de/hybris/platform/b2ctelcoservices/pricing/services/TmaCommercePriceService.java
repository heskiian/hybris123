/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.services;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.price.SubscriptionCommercePriceService;

import java.util.Set;


/**
 * Price service for retrieving applicable prices considering a {@link TmaPriceContext}. Dependency to
 * {@link SubscriptionCommercePriceService} has been marked as deprecated in 1903.
 *
 * @since 6.7
 */
public interface TmaCommercePriceService extends SubscriptionCommercePriceService
{
	/**
	 * Retrieves the minimum {@link PriceRowModel} which may be applicable for the given context, be it either the
	 * standalone price of the offering or a price override.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return minimum {@link PriceRowModel} applicable for the given context
	 * @deprecated since 2007. Replaced with getBestApplicablePrice(de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext)
	 */
	@Deprecated(since = "2007")
	PriceRowModel getMinimumPrice(final TmaPriceContext priceContext);

	/**
	 * Retrieves the best {@link PriceRowModel} applicable for the given context, be it either the
	 * standalone price of the offering or a price override.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return best {@link PriceRowModel} applicable for the given context
	 */
	PriceRowModel getBestApplicablePrice(final TmaPriceContext priceContext);

	/**
	 * Retrieves the value of the minimum price which may be applicable for the given context, be it either the
	 * standalone price of the offering or a price override. The priorities for retrieving the value are the following:
	 * <ul>
	 * <li>if the price has recurring charges, return the first recurring charge</li>
	 * <li>if the price has a value, return the value</li>
	 * <li>if the price has one time charges, return the first one time charge</li>
	 * </ul>
	 *
	 * @param priceContext
	 * 		price context to retrieve the price value from
	 * @return price value considering the priorities mentioned above
	 */
	Double getMinimumPriceValue(final TmaPriceContext priceContext);

	/**
	 * Retrieves the minimum {@link PriceRowModel} which may be applicable for the given {@param orderEntryModel},
	 * considering the context in which the offering has been bought (either standalone or as part of a bundle).
	 *
	 * @param orderEntryModel
	 * 		the order entry for which the price will be computed
	 * @return minimum {@link PriceRowModel} applicable for the given order entry
	 * @deprecated since 2007. Replaced with getBestApplicablePrice(de.hybris.platform.core.model.order.AbstractOrderEntryModel)
	 */
	@Deprecated(since = "2007")
	PriceRowModel getMinimumPrice(final AbstractOrderEntryModel orderEntryModel);

	/**
	 * Retrieves the best {@link PriceRowModel} applicable for the given {@param orderEntryModel},
	 * considering the context in which the offering has been bought (either standalone or as part of a bundle).
	 *
	 * @param orderEntryModel
	 * 		the order entry for which the price will be computed
	 * @return best {@link PriceRowModel} applicable for the given order entry
	 */
	PriceRowModel getBestApplicablePrice(final AbstractOrderEntryModel orderEntryModel);

	/**
	 * Retrieves all instances of {@link PriceRowModel}s and {@link SubscriptionPricePlanModel}s for the
	 * {@param priceContextData} and {@param productModel} provided.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return {@link PriceRowModel}s retrieved from the configuration provided by the {@param priceContextData}
	 */
	Set<PriceRowModel> filterPricesbyPriceContext(TmaPriceContext priceContext);
}
