/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.price.TmaPriceFacade;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator for a Subscription Product.
 *
 * @since 6.7
 */
public class TmaSubscriptionProductPopulator extends ProductPopulator
{
	private Populator<ProductModel, ProductData> subscriptionProductBasicPopulator;
	private Populator<ProductModel, ProductData> productStockPopulator;
	private Populator<ProductModel, ProductData> productPricePopulator;
	private Populator<ProductModel, ProductData> productDescriptionPopulator;
	private Populator<ProductModel, ProductData> productClassificationPopulator;

	private TmaSubscribedProductFacade tmaSubscribedProductFacade;
	private TmaPriceFacade priceFacade;

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		super.populate(source, target);
		getSubscriptionProductBasicPopulator().populate(source, target);

		if (tmaSubscribedProductFacade.isSubscriptionProduct(source))
		{
			final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
					.withProduct(source)
					.withProcessTypes(new HashSet<>(Collections.singletonList(TmaProcessType.ACQUISITION)))
					.build();
			target.setPrice(getPriceFacade().getMinimumPrice(priceContext));
		}
		else
		{
			getProductPricePopulator().populate(source, target);
		}
		getProductStockPopulator().populate(source, target);
		getProductDescriptionPopulator().populate(source, target);
		getProductClassificationPopulator().populate(source, target);
	}

	protected Populator<ProductModel, ProductData> getSubscriptionProductBasicPopulator()
	{
		return subscriptionProductBasicPopulator;
	}

	@Required
	public void setSubscriptionProductBasicPopulator(final Populator<ProductModel, ProductData> subscriptionProductBasicPopulator)
	{
		this.subscriptionProductBasicPopulator = subscriptionProductBasicPopulator;
	}

	protected Populator<ProductModel, ProductData> getProductStockPopulator()
	{
		return productStockPopulator;
	}

	@Required
	public void setProductStockPopulator(final Populator<ProductModel, ProductData> productStockPopulator)
	{
		this.productStockPopulator = productStockPopulator;
	}

	protected Populator<ProductModel, ProductData> getProductPricePopulator()
	{
		return productPricePopulator;
	}

	@Required
	public void setProductPricePopulator(final Populator<ProductModel, ProductData> productPricePopulator)
	{
		this.productPricePopulator = productPricePopulator;
	}

	protected Populator<ProductModel, ProductData> getProductDescriptionPopulator()
	{
		return productDescriptionPopulator;
	}

	@Required
	public void setProductDescriptionPopulator(final Populator<ProductModel, ProductData> productDescriptionPopulator)
	{
		this.productDescriptionPopulator = productDescriptionPopulator;
	}

	protected Populator<ProductModel, ProductData> getProductClassificationPopulator()
	{
		return productClassificationPopulator;
	}

	@Required
	public void setProductClassificationPopulator(final Populator<ProductModel, ProductData> productClassificationPopulator)
	{
		this.productClassificationPopulator = productClassificationPopulator;
	}

	protected TmaSubscribedProductFacade getTmaSubscribedProductFacade()
	{
		return tmaSubscribedProductFacade;
	}

	@Required
	public void setTmaSubscribedProductFacade(final TmaSubscribedProductFacade tmaSubscribedProductFacade)
	{
		this.tmaSubscribedProductFacade = tmaSubscribedProductFacade;
	}

	protected TmaPriceFacade getPriceFacade()
	{
		return priceFacade;
	}

	@Required
	public void setPriceFacade(final TmaPriceFacade priceFacade)
	{
		this.priceFacade = priceFacade;
	}
}
