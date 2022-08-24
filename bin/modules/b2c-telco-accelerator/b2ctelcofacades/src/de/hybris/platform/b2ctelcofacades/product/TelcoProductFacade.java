/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.product;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.List;


/**
 * Telco product facade that provides functionality to return products that a) are 1 to 1 product references and feature
 * compatible products b) are 1 to 1 product references and feature and vendor compatible products.
 */
public interface TelcoProductFacade
{

	/**
	 * Get unique list of product references and feature compatible products with a max limit set by maximunNumber Of
	 * Products First list of product references is returned and then feature compatible products are returned.
	 *
	 * @param code
	 *           product Code
	 * @param referenceTypes
	 *            {@link List} of {@link ProductReferenceTypeEnum}s
	 * @param options
	 *            {@link List} of {@link ProductOption}s
	 * @param limit
	 *           maximum size of returned data
	 * @param classAttributeAssignment
	 *           classification attribute of the classification class for the product
	 * @return {@link List} of {@link ProductData}
	 */
	List<ProductData> getProductReferencesAndFeatureCompatibleProductsForCode(final String code,
			List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssignment);

	/**
	 * Returns a list of products build of: product references, feature and vendor compatible products having the limit
	 * set by maximumNumber.
	 *
	 * @param code
	 * 		code of the product for which the compatible products should be retrieved
	 * @param referenceTypes
	 * 		{@link List} of {@link ProductReferenceTypeEnum}s for which the products should be returned
	 * @param options
	 * 		{@link List} of {@link ProductOption}s for populating information on the returned {@link ProductData}
	 * @param limit
	 * 		maximum number of products to be returned. If null, all available products will be returned.
	 * @param classAttributeAssignment
	 * 		classification attribute
	 * @return {@link List} of {@link ProductData}
	 * @since 6.6
	 */
	List<ProductData> getProductReferencesAndCompatibleProducts(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssignment);
}
