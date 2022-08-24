/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ObjectUtils;


/**
 * Unit test for {@link TmaPoVariantSearchDataPopulator}.
 *
 * @since 1810
 */
@UnitTest
public class TmaPoVariantSearchDataPopulatorTest
{
	private static final String CODE = "iphone_x";
	private static final String NAME = "iPhone X";
	private static final String STORAGE_SIZE = "16gb";
	private static final String COLOR = "red";

	private TmaPoVariantSearchDataPopulator populator;
	private SearchResultValueData source;
	private ProductData productData;


	@Before
	public void setUp()
	{
		this.populator = new TmaPoVariantSearchDataPopulator();
		this.productData = new ProductData();
	}

	@Test
	public void testPopulate()
	{
		givenSourceData(CODE, NAME, STORAGE_SIZE, COLOR);
		whenProductDataIsPopulated();
		thenProductDataIsPopulated();
	}

	private void givenSourceData(final String code, final String name, final String storageSize, final String color)
	{
		final Map<String, Object> values = new HashMap<>();
		values.put(TmaPoVariantSearchDataPopulator.BASE_PRODUCT_CODE, code);
		values.put(TmaPoVariantSearchDataPopulator.BASE_PRODUCT_NAME, name);
		values.put(TmaPoVariantSearchDataPopulator.VARIANT_STORAGE_SIZE, storageSize);
		values.put(TmaPoVariantSearchDataPopulator.VARIANT_COLOR, color);
		this.source = new SearchResultValueData();
		this.source.setValues(values);
	}

	private void whenProductDataIsPopulated()
	{
		this.populator.populate(source, productData);
	}

	private void thenProductDataIsPopulated()
	{

		if (!ObjectUtils.isEmpty(productData) && !ObjectUtils.isEmpty(productData.getBaseProduct()))
		{

			assertEquals(CODE, productData.getBaseProduct());
		}
		if (!ObjectUtils.isEmpty(productData) && !ObjectUtils.isEmpty(productData.getBaseProductName()))
		{
			assertEquals(NAME, productData.getBaseProductName());
		}
		if (!ObjectUtils.isEmpty(productData) && !ObjectUtils.isEmpty(productData.getStorageSize()))
		{
			assertEquals(STORAGE_SIZE, productData.getStorageSize());
		}
		if (!ObjectUtils.isEmpty(productData) && !ObjectUtils.isEmpty(productData.getStorageSize()))
		{

			assertEquals(Arrays.asList(COLOR), productData.getColors());
		}
	}
}
