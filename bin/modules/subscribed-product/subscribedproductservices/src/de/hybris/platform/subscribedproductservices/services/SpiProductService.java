/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services;

import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Service responsible for handling {@link SpiProductModel}  related operations.
 *
 * @since 2105
 */
public interface SpiProductService
{
	/**
	 * Returns a {@link SpiProductModel} for the given id.
	 *
	 * @param id
	 * 		identifier of {@link SpiProductModel}
	 * @return the {@link SpiProductModel} found.
	 * @throws ModelNotFoundException
	 * 		if no subscribed product is found.
	 */
	SpiProductModel getProduct(final String id);

	/**
	 * Retrieves a list of {@link SpiProductModel} for a given product context.
	 *
	 * @param spiProductContext
	 * 		the product context.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned products.
	 * @return the list of products found for the given context.
	 */
	List<SpiProductModel> getProducts(final SpiProductContext spiProductContext, final Integer offset, final Integer limit);

	/**
	 * Retrieves the total number of products found for a given productContext.
	 *
	 * @param spiProductContext
	 * 		the product context.
	 * @return the number of products found.
	 */
	Integer getNumberOfProductsFor(final SpiProductContext spiProductContext);

	/**
	 * Saves the given {@link SpiProductModel}.
	 *
	 * @param product
	 * 		the product.
	 */
	void saveProduct(final SpiProductModel product);

	/**
	 * Creates an instance of {@link SpiProductModel} subtype.
	 *
	 * @param productClass
	 * 		the class of the {@link SpiProductModel} subtype.
	 * @return the newly created model.
	 */
	SpiProductModel createProduct(final Class productClass);

	/**
	 * Removes the given {@link SpiProductModel}
	 *
	 * @param product
	 * 		the product.
	 */
	void removeProduct(final SpiProductModel product);
}
