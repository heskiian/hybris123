/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.product.TmaPoVariantFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation for {@link TmaPoVariantFacade}.
 *
 * @since 1810
 */
public class DefaultTmaPoVariantFacade implements TmaPoVariantFacade
{
	@Override
	public List<ProductData> groupByBaseProduct(final List<ProductData> productDataList)
	{
		final List<ProductData> groupedProducts = new ArrayList<>();
		final List<ProductData> processedVariants = new ArrayList<>();
		final Map<String, List<ProductData>> variantsMap = groupVariantsByBaseProduct(productDataList);
		productDataList.stream().forEach(p ->
		{
			final List<ProductData> variants = variantsMap.get(getVariantKey(p));
			if (CollectionUtils.isEmpty(variants))
			{
				groupedProducts.add(p);
			}
			else if (!processedVariants.contains(p))
			{
				groupedProducts.add(createBaseProductWithVariants(p, variants));
				processedVariants.addAll(variants);
			}
		});

		return groupedProducts;
	}

	private ProductData createBaseProductWithVariants(final ProductData productData, final List<ProductData> variants)
	{
		variants.stream().forEach(variant ->
		{
			if (!variant.getCode().equalsIgnoreCase(productData.getCode()))
			{
				productData.getColors().addAll(variant.getColors());
			}
		});
		productData.setName(productData.getBaseProductName() + " " + productData.getStorageSize());
		return productData;
	}

	private Map<String, List<ProductData>> groupVariantsByBaseProduct(final List<ProductData> productDataList)
	{
		final Map<String, List<ProductData>> variantsMap = new HashMap<>();
		productDataList.stream().forEach(p ->
		{
			if (isVariant(p))
			{
				addVariantToMap(variantsMap, p);
			}
		});
		return variantsMap;
	}

	private void addVariantToMap(final Map<String, List<ProductData>> variantsMap, final ProductData p)
	{
		final String key = getVariantKey(p);
		final List<ProductData> variants = variantsMap.getOrDefault(key, new ArrayList());
		variants.add(p);
		variantsMap.put(key, variants);
	}

	private String getVariantKey(final ProductData p)
	{
		return isStorageSizeVariant(p) ? (p.getBaseProduct() + p.getStorageSize()) : p.getBaseProduct();
	}

	private boolean isVariant(final ProductData productData)
	{
		return StringUtils.isNotEmpty(productData.getBaseProduct());
	}

	private boolean isStorageSizeVariant(final ProductData productData)
	{
		return isVariant(productData) && StringUtils.isNotEmpty(productData.getStorageSize());
	}
}
