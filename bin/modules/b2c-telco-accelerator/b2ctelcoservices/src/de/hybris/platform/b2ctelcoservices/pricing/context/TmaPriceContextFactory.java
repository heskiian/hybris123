/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.context;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Set;


/**
 * Factory responsible for the creation of the {@link TmaPriceContext}.
 *
 * @since 6.7
 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
 */
@Deprecated(since = "1911")
public interface TmaPriceContextFactory
{
	/**
	 * Creates a {@link TmaPriceContext} with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process types applicable for the price to be identified by the price context
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes);

	/**
	 * Creates a {@link TmaPriceContext} with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process types applicable for the price to be identified by the price context
	 * @param subscriptionTerms
	 *           subscription terms applicable for the price to be identified by the price context
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms);

	/**
	 * Creates a {@link TmaPriceContext} with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process types applicable for the price to be identified by the price context
	 * @param currencyModel
	 *           currency applicable for the price to be identified by the price context
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final CurrencyModel currencyModel);

	/**
	 * Creates a {@link TmaPriceContext} with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process types applicable for the price to be identified by the price context
	 * @param subscriptionTerms
	 *           subscription terms applicable for the price to be identified by the price context
	 * @param currencyModel
	 *           currency applicable for the price to be identified by the price context
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms, final CurrencyModel currencyModel);

	/**
	 * Creates a {@link TmaPriceContext} for BPO prices with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process types applicable for the price to be identified by the price context
	 * @param affectedProduct
	 *           product for which the price applies
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createBpoPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final ProductModel affectedProduct);

	/**
	 * Creates a {@link TmaPriceContext} for BPO prices with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process type applicable for the price to be identified by the price context
	 * @param subscriptionTerms
	 *           subscription term applicable for the price to be identified by the price context
	 * @param affectedProduct
	 *           product for which the price applies
	 * @param requiredProducts
	 *           required products for the price to apply
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createBpoPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms, final ProductModel affectedProduct,
			final Set<? extends ProductModel> requiredProducts);

	/**
	 * Creates a {@link TmaPriceContext} for BPO prices with the provided parameters.
	 *
	 * @param productOffering
	 *           product on which the price is configured
	 * @param processTypes
	 *           process type applicable for the price to be identified by the price context
	 * @param subscriptionTerms
	 *           subscription term applicable for the price to be identified by the price context
	 * @param affectedProduct
	 *           product for which the price applies
	 * @param requiredProducts
	 *           required products for the price to apply
	 * @param currency
	 *           currency for which prices to be searched
	 * @return newly created {@link TmaPriceContext} with the configured parameters
	 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
	 */
	@Deprecated(since = "1911")
	TmaPriceContext createBpoPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms, final ProductModel affectedProduct,
			final Set<? extends ProductModel> requiredProducts, CurrencyModel currency);

}
