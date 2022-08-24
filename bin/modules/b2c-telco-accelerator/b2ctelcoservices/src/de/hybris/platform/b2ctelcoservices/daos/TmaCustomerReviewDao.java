/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customerreview.dao.CustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

import java.util.List;


/**
 * The DAO for the {@link CustomerReviewModel}.
 *
 * @since 1907
 */
public interface TmaCustomerReviewDao extends CustomerReviewDao
{
	/**
	 * Retrieves the {@link CustomerReviewModel}s that belong to a product by offset, limit and language.
	 *
	 * @param product
	 * 		the product which has the reviews
	 * @param offset
	 * 		the offset represents the position in list from where the result list will start.
	 * @param limit
	 * 		the limit represents the number of entries that will be selected for the result list.
	 * @param language
	 * 		the language of the reviews
	 * @return all found {@link CustomerReviewModel}s, or an empty collection if no review can be found
	 */
	List<CustomerReviewModel> getReviewsForProductByLimitOffsetAndLanguage(ProductModel product, int offset, int limit,
			LanguageModel language);

	/**
	 * Retrieves the the number of reviews that exist for a product in a given language.
	 *
	 * @param product
	 * 		the product which has the reviews
	 * @param language
	 * 		the language of the reviews
	 * @return the number of all found {@link CustomerReviewModel}s
	 */
	Integer getNumberOfReviewsForProductAndLanguage(ProductModel product, LanguageModel language);
}
