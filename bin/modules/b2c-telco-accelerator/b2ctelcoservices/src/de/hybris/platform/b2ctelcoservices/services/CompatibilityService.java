/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Service which returns products based on feature compatibility and vendor compatibility.
 */
public interface CompatibilityService
{

	/**
	 * Return list of compatible products based on featureCompatiblity classification feature.
	 *
	 * @param code
	 * 		product Code
	 * @param classAttributeAssignment
	 *           classification attribute of the classification class for the target item
	 * @return list of matching products
	 */
	@Nonnull
	List<ProductModel> getFeatureCompatibleProducts(String code, ClassAttributeAssignmentModel classAttributeAssignment);

	/**
	 * Returns a list of products which are considered compatible when the name of the manufacturer is the same
	 * as the vendor of the product.
	 *
	 * @param productCode
	 * 		code of product for which compatible products should be returned
	 * @return list of matching products
	 * @since 6.6
	 */
	@Nonnull
	List<ProductModel> getProductsForVendorCompatibility(final String productCode);
}
