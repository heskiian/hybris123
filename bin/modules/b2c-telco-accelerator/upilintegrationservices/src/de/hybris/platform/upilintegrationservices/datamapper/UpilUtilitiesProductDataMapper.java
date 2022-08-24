/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.datamapper;

import de.hybris.platform.upilintegrationservices.data.C_UtilitiesProductType;

import java.util.Map;


/**
 * TmaUtilitiesProductDataMapper is a part of extension upilintegrationservices,
 * It is used as Mapper interface for utilities product which will Convert Data object to Map.
 *
 * @since 1911
 */
public interface UpilUtilitiesProductDataMapper
{

	/**
	 * Returns all Utilities Product data for the selected UpilUtilsProductData
	 * {@link de.hybris.platform.upilintegrationservices.data.UpilUtilsProductData} for Each PricePlan new
	 * UtilitiesProduct data will return as Map list of {@link Map<String , Object>}
	 *
	 * @param upilUtilsProductData
	 *           UPIL utilities product data
	 * @return {@link Map<String , Object>} which include some information of utilitieProduct,
	 *         UtilsProductAttributes, UtilsProductDiscounts, UtilsOneTimeCharges, UtilsRecurringCharges &
	 *         UtilsUsageCharges
	 *         Price plan and discounts
	 */
	Map<String, Object> getUtilitiesProductDataMap(final C_UtilitiesProductType upilUtilsProductData);
}
