/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Populates {@link ProductData} with variant related data extracted from {@link SearchResultValueData}.
 *
 * @since 1810
 */
public class TmaPoVariantSearchDataPopulator implements Populator<SearchResultValueData, ProductData>
{
	private static final Logger LOG = Logger.getLogger(TmaPoVariantSearchDataPopulator.class);
	protected static final String BASE_PRODUCT = "baseProduct";
	protected static final String BASE_PRODUCT_CODE = "baseProductCode";
	protected static final String BASE_PRODUCT_NAME = "baseProductName";
	protected static final String VARIANT_COLOR = "variantColor";
	protected static final String VARIANT_STORAGE_SIZE = "storageSize";

	@Override
	public void populate(final SearchResultValueData source, final ProductData productData)
	{
		if (source.getValues().get(BASE_PRODUCT_CODE) != null)
		{
			productData.setBaseProduct(source.getValues().get(BASE_PRODUCT_CODE).toString());
		}
		if (source.getValues().get(BASE_PRODUCT_NAME) != null)
		{
			productData.setBaseProductName(source.getValues().get(BASE_PRODUCT_NAME).toString());
		}
		if (source.getValues().get(VARIANT_STORAGE_SIZE) != null)
		{
			productData.setStorageSize(source.getValues().get(VARIANT_STORAGE_SIZE).toString());
		}

		populateColors(source, productData);
	}

	private void populateColors(final SearchResultValueData source, final ProductData productData)
	{
		final Object value = getValue(source, VARIANT_COLOR);
		final List<String> colors = new ArrayList<>();
		if (value != null)
		{
			colors.add(value.toString());
		}
		productData.setColors(colors);
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		return source.getValues() == null ? null : (T) source.getValues().get(propertyName);
	}
}
