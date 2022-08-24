/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Data Access Object for looking up items related to {@link de.hybris.platform.catalog.model.ProductFeatureModel}.
 *
 * @spring.bean productsFeaturesDao
 */
public interface ProductsFeaturesDao
{
	/**
	 * Returns list of compatible products based on the manufacturer name and classification details provided.
	 * The returned products have the given manufacturer name, belong to the same classification class,
	 * having the same classification attributes.
	 *
	 * @param manufacturerName
	 * 		name of the manufacturer of the products to be returned
	 * @param classificationClassCode
	 * 		code of the classification class to which the products should belong
	 * @param classificationAttributeCode
	 * 		code of the classification attribute of the class
	 * @return {@link List} of {@link ProductModel}s which are considered compatible or
	 * empty {@link List} if no such products can be found.
	 * @since 6.6
	 */
	@Nonnull
	List<ProductModel> findProductsByVendorCompatibility(final String manufacturerName,
			final String classificationClassCode, final String classificationAttributeCode);
}
