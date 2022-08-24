/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.context.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Set;


/**
 * Builder class for the creation of the {@link TmaPriceContext}
 *
 * @since 2003
 */
public class TmaPriceContextBuilder
{
	private ProductModel product;
	private ProductModel affectedProduct;
	private Set<? extends ProductModel> requiredProducts;
	private Set<TmaProcessType> processTypes;
	private Set<SubscriptionTermModel> subscriptionTerms;
	private CurrencyModel currency;
	private UserModel user;
	private Set<RegionModel> regions;

	public TmaPriceContext build()
	{
		final TmaPriceContext priceContext = new TmaPriceContext();
		priceContext.setProduct(product);
		priceContext.setAffectedProduct(affectedProduct);
		priceContext.setRequiredProducts((Set<ProductModel>) requiredProducts);
		priceContext.setProcessTypes(processTypes);
		priceContext.setSubscriptionTerms(subscriptionTerms);
		priceContext.setCurrency(currency);
		priceContext.setUser(user);
		priceContext.setRegions(regions);
		return priceContext;
	}

	public static TmaPriceContextBuilder newTmaPriceContextBuilder()
	{
		return new TmaPriceContextBuilder();
	}

	public TmaPriceContextBuilder withProduct(final ProductModel product)
	{
		this.product = product;
		return this;
	}

	public TmaPriceContextBuilder withtAffectedProduct(final ProductModel affectedProduct)
	{
		this.affectedProduct = affectedProduct;
		return this;
	}

	public TmaPriceContextBuilder withRequiredProducts(final Set<? extends ProductModel> requiredProducts)
	{
		this.requiredProducts = requiredProducts;
		return this;
	}

	public TmaPriceContextBuilder withProcessTypes(final Set<TmaProcessType> processTypes)
	{
		this.processTypes = processTypes;
		return this;
	}

	public TmaPriceContextBuilder withSubscriptionTerms(final Set<SubscriptionTermModel> subscriptionTerms)
	{
		this.subscriptionTerms = subscriptionTerms;
		return this;
	}

	public TmaPriceContextBuilder withCurrency(final CurrencyModel currency)
	{
		this.currency = currency;
		return this;
	}

	public TmaPriceContextBuilder withUser(final UserModel user)
	{
		this.user = user;
		return this;
	}

	public TmaPriceContextBuilder withRegions(final Set<RegionModel> regions)
	{
		this.regions = regions;
		return this;
	}

}
