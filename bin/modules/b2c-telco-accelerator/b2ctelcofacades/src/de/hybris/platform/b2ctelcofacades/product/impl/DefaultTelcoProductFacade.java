/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.product.TelcoProductFacade;
import de.hybris.platform.b2ctelcoservices.services.CompatibilityService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of Telco Product Facade {@link TelcoProductFacade}.
 */
public class DefaultTelcoProductFacade implements TelcoProductFacade
{
	private CompatibilityService compatibilityService;
	private ProductFacade productFacade;

	@Override
	public List<ProductData> getProductReferencesAndFeatureCompatibleProductsForCode(
			final String code,
			final List<ProductReferenceTypeEnum> referenceTypes,
			final List<ProductOption> options,
			final Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		final List<ProductData> productData = new ArrayList<>();
		int currentCount = 0;

		final List<ProductReferenceData> productReferences =
				getProductFacade().getProductReferencesForCode(code, referenceTypes, options, limit);

		currentCount += productReferences.size();
		for (final ProductReferenceData prdata : productReferences)
		{
			productData.add(prdata.getTarget());
		}
		if (limit == null || currentCount >= limit)
		{
			return productData;
		}

		final List<ProductModel> featureCompatibleProducts = compatibilityService
				.getFeatureCompatibleProducts(code, classAttributeAssignment);

		for (final ProductModel product : featureCompatibleProducts)
		{
			if (limit <= currentCount)
			{
				break;
			}
			final ProductData tempProduct = getProductFacade().getProductForCodeAndOptions(product.getCode(), options);
			if (!contains(productData, tempProduct))
			{
				productData.add(tempProduct);
				currentCount++;
			}
		}
		return productData;
	}

	@Override
	public List<ProductData> getProductReferencesAndCompatibleProducts(final String code,
			final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, Integer limit,
			final ClassAttributeAssignmentModel classAttributeAssignment)
	{
		// Initialize the compatible products list with referenced products
		final List<ProductData> compatibleProducts = getProductReferencesAndFeatureCompatibleProductsForCode(code,
				referenceTypes, options, limit, classAttributeAssignment);
		if (limit != null && compatibleProducts.size() < limit)
		{
			return compatibleProducts;
		}

		addVendorCompatibleProducts(code, options, limit, compatibleProducts);
		return compatibleProducts;
	}

	private void addVendorCompatibleProducts(String code, List<ProductOption> options, Integer limit,
			List<ProductData> compatibleProducts)
	{
		final List<ProductModel> vendorCompatibleProducts = getCompatibilityService()
				.getProductsForVendorCompatibility(code);
		int compatibleProductsNumber = compatibleProducts.size();

		for (final ProductModel compatibleProduct : vendorCompatibleProducts)
		{
			if (limit != null && limit <= compatibleProductsNumber)
			{
				return;
			}

			final ProductData compatibleProductData = getProductFacade()
					.getProductForCodeAndOptions(compatibleProduct.getCode(), options);
			if (!contains(compatibleProducts, compatibleProductData))
			{
				compatibleProducts.add(compatibleProductData);
				compatibleProductsNumber++;
			}
		}
	}

	/**
	 * Checks whether the list given contains the product.
	 *
	 * @param products
	 * 		list of existing product data items
	 * @param product
	 * 		product to check whether it is contained in the list
	 * @return true if the given product is contained in the products list
	 */
	protected boolean contains(final List<ProductData> products, final ProductData product)
	{
		final Object exists = CollectionUtils.find(products, productData ->
		{
			final ProductData existingProductData = (ProductData) productData;
			return existingProductData.getCode().equals(product.getCode());
		});
		return exists != null;
	}

	protected CompatibilityService getCompatibilityService()
	{
		return compatibilityService;
	}

	@Required
	public void setCompatibilityService(final CompatibilityService compatibilityService)
	{
		this.compatibilityService = compatibilityService;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	@Required
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

}
