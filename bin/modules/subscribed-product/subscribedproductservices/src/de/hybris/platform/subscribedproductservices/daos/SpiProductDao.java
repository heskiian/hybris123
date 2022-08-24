/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;

import java.util.List;
import java.util.Map;


/**
 * Data access object for {@link SpiProductModel}s.
 *
 * @since 2105
 */
public interface SpiProductDao extends GenericDao<SpiProductModel>
{

	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	SpiProductModel findUnique(Map<String, ? extends Object> params);

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
}
