/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;

import java.util.List;


/**
 * Facade for {@link TmaPoVariantModel} operations.
 *
 * @since 1810
 */
public interface TmaPoVariantFacade
{
	/**
	 * Groups variants based on base product and on storage size. For each storage size, one or many colors may be available. If a
	 * product is not a variant, that product will be added directly to the result list.
	 *
	 * @param productDataList
	 * 		list of all products
	 *
	 * @return list of products grouped by base product and storage size.
	 */
	List<ProductData> groupByBaseProduct(final List<ProductData> productDataList);
}
