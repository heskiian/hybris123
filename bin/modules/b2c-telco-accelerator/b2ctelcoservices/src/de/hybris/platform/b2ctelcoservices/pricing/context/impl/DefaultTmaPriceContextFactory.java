/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.context.impl;


import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContextFactory;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Set;



/**
 * Default implementation of the {@link TmaPriceContextFactory}.
 *
 * @since 6.7
 * @deprecated since 1911. Use instead {@link TmaPriceContextBuilder}.
 */
@Deprecated(since = "1911")
public class DefaultTmaPriceContextFactory implements TmaPriceContextFactory
{

	@Override
	public TmaPriceContext createPriceContext(final ProductModel product,
			final Set<TmaProcessType> processTypes)
	{
		final TmaPriceContext priceContext = createEmptyPriceContext();
		priceContext.setProduct(product);

		priceContext.setProcessTypes(processTypes);
		return priceContext;
	}

	@Override
	public TmaPriceContext createPriceContext(final ProductModel product,
			final Set<TmaProcessType> processTypes, final Set<SubscriptionTermModel> subscriptionTerms)
	{
		final TmaPriceContext priceContext = createPriceContext(product, processTypes);
		priceContext.setSubscriptionTerms(subscriptionTerms);
		return priceContext;
	}

	@Override
	public TmaPriceContext createPriceContext(final ProductModel product,
			final Set<TmaProcessType> processTypes, final CurrencyModel currencyModel)
	{
		final TmaPriceContext priceContext = createPriceContext(product, processTypes);
		priceContext.setCurrency(currencyModel);
		return priceContext;
	}

	@Override
	public TmaPriceContext createPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms, final CurrencyModel currencyModel)
	{
		final TmaPriceContext priceContext = createPriceContext(productOffering, processTypes, subscriptionTerms);
		priceContext.setCurrency(currencyModel);
		return priceContext;
	}

	@Override
	public TmaPriceContext createBpoPriceContext(final ProductModel product,
			final Set<TmaProcessType> processTypes, final ProductModel affectedProduct)
	{
		final TmaPriceContext priceContext = createPriceContext(product, processTypes);
		priceContext.setAffectedProduct(affectedProduct);
		return priceContext;
	}

	@Override
	public TmaPriceContext createBpoPriceContext(final ProductModel product,
			final Set<TmaProcessType> processTypes, final Set<SubscriptionTermModel> subscriptionTerms,
			final ProductModel affectedProduct, final Set<? extends ProductModel> requiredProducts)
	{
		final TmaPriceContext priceContext = createPriceContext(product, processTypes, subscriptionTerms);
		priceContext.setAffectedProduct(affectedProduct);
		priceContext.setRequiredProducts((Set<ProductModel>) requiredProducts);
		return priceContext;
	}

	@Override
	public TmaPriceContext createBpoPriceContext(final ProductModel productOffering, final Set<TmaProcessType> processTypes,
			final Set<SubscriptionTermModel> subscriptionTerms, final ProductModel affectedProduct,
			final Set<? extends ProductModel> requiredProducts, final CurrencyModel currency)
	{
		final TmaPriceContext priceContext = createPriceContext(productOffering, processTypes, subscriptionTerms);
		priceContext.setAffectedProduct(affectedProduct);
		priceContext.setRequiredProducts((Set<ProductModel>) requiredProducts);
		priceContext.setCurrency(currency);
		return priceContext;
	}

	protected TmaPriceContext createEmptyPriceContext()
	{
		return new TmaPriceContext();
	}


}
