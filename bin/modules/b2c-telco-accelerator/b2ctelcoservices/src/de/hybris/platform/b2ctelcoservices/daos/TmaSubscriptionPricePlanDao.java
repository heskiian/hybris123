/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.util.Set;


/**
 * Data Access Object for price override related operations, ie. prices configured at BPO level which override default
 * prices
 * configured for SPOs.
 *
 * @since 6.7
 */
public interface TmaSubscriptionPricePlanDao
{
	/**
	 * Retrieves all instances of {@link SubscriptionPricePlanModel}s for the {@param priceContext} provided.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return {@link SubscriptionPricePlanModel}s having the configuration provided by the {@param priceContext}
	 */
	Set<SubscriptionPricePlanModel> findApplicableSubscriptionPricePlans(final TmaPriceContext priceContext);

	/**
	 * Retrieves all instances of {@link PriceRowModel}s exclusively for the {@param priceContext} provided.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return {@link PriceRowModel}s having the configuration provided by the {@param priceContext}
	 */
	Set<PriceRowModel> findApplicablePriceRows(final TmaPriceContext priceContext);

	/**
	 * Retrieves all {@link PriceRowModel}s which may be applicable for the context and the {@param priceContext#product}
	 * given, be it either the stand alone price of the product or a price override for which the
	 * {@param priceContext#product} represents the {@link PriceRowModel#getAffectedProductOffering()} of the price.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return {@link PriceRowModel}s for the {@param priceContext#product} having the configuration
	 * provided by the {@param priceContext}
	 */
	Set<PriceRowModel> findAllApplicablePricesForContext(final TmaPriceContext priceContext);

	/**
	 * Filter and retrieves all the instances of {@link PriceRowModel}s and {@link SubscriptionPricePlanModel}s for the
	 * {@param priceContext} provided.
	 *
	 * @param priceContext
	 * 		priceContext to retrieve the configuration from
	 * @return {@link PriceRowModel}s for the {@param priceContext#product} having the configuration provided by the
	 * {@param priceContext}
	 * @deprecated since 2003. Use instead
	 * {@link TmaSubscriptionPricePlanDao#findAllApplicablePricesForContext(TmaPriceContext)}
	 */
	@Deprecated(since = "2003", forRemoval = true)
	Set<PriceRowModel> filterPricesForContext(final TmaPriceContext priceContext);
}
