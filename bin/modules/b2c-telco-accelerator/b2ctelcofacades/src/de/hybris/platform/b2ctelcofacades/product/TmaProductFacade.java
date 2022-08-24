/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;


/**
 * Facade handling operations related to {@link CustomerReviewModel}.
 *
 * @since 1907
 */
public interface TmaProductFacade extends ProductFacade
{
	/**
	 * Retrieves a list of reviews of a product by offset, limit and language.
	 *
	 * @param productCode
	 * 		the code of the product.
	 * @param offset
	 * 		the offset represents the position in list from where the result list will start.
	 * @param limit
	 * 		the limit represents the number of entries that will be selected for the result list.
	 * @return the list of reviews.
	 * @throws UnknownIdentifierException
	 * 		is thrown when the product does not exist
	 */
	List<ReviewData> getReviewsByLimitAndOffset(String productCode, Integer offset, Integer limit) throws
			UnknownIdentifierException;

	/**
	 * Creates a review resource for a product.
	 *
	 * @param productCode
	 * 		the code of the product.
	 * @param userId
	 * 		the code of the user.
	 * @param reviewData
	 * 		the resource that will be created.
	 * @return the created review.
	 */
	ReviewData createReview(String productCode, String userId, ReviewData reviewData);

	/**
	 * Computes the total number of reviews based on a product code that is provided.
	 *
	 * @param productCode
	 * 		the code of the product.
	 * @return the number of reviews of a product.
	 */
	Integer getNumberOfReviewsByLanguage(String productCode);

	/**
	 * Determines intermediate BPOs between the provided po code and the root bpo code.
	 *
	 * @param poCode
	 * 		the PO code
	 * @param rootBpoCode
	 * 		the root BPO code
	 * @return the list of intermediary BPOs between the root BPO and the given PO. In case the PO is not part of the root BPO
	 * structure an empty list is returned
	 */
	List<ProductData> getIntermediateBpos(String poCode, String rootBpoCode);
}
