/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.price;

import de.hybris.platform.b2ctelcofacades.data.TmaComponentProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;


/**
 * Facade exposing operations related to the product offering prices.
 *
 * @since 1903.
 */
public interface TmaPriceFacade
{
	/**
	 * Retrieves the lowest price for the {@param priceContext} given, looking at both the stand alone price
	 * and price overrides of the{@link TmaPriceContext#affectedProduct}
	 *
	 * @param priceContext
	 * 		price context for which to retrieve the price
	 * @return minimum price for the given {@param priceContext}
	 * @deprecated since 2007. Use instead {@link TmaPriceFacade#getBestApplicablePrice(TmaPriceContext)}
	 */
	@Deprecated(since = "2007")
	PriceData getMinimumPrice(final TmaPriceContext priceContext);

	/**
	 * Retrieves the best applicable price for a given priceContext.
	 *
	 * @param priceContext
	 * 		the price context for which we retrieve the price.
	 * @return best applicable price for the given {@param priceContext}
	 */
	PriceData getBestApplicablePrice(final TmaPriceContext priceContext);

	/**
	 * Determines the list of price components of type provided by navigating though the structure of the product offering price
	 * given.
	 *
	 * @param productOfferingPrice
	 * 		the given POP
	 * @param componentPriceType
	 * 		the type of the price components that has to be determined
	 * @return the list of price components of the type given
	 */
	<T extends TmaComponentProdOfferPriceData> List<T> getListOfPriceComponents(
			final TmaProductOfferingPriceData productOfferingPrice, Class<T> componentPriceType);

	/**
	 * Returns the {@link PriceDataType} for the {@param product} given.
	 *
	 * @param product
	 * 		product to retrieve the price data type for
	 * @return price data type for the product given
	 */
	PriceDataType getPriceDataType(final ProductModel product);
}
