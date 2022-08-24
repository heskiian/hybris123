/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for {@link DefaultTmaPoVariantFacade}.
 *
 * @since 1810
 */
@UnitTest
public class DefaultTmaPoVariantFacadeTest
{
	private static final String BASE_PRODUCT_IPHONE_X = "iphone_x";
	private static final String BASE_PRODUCT_IPHONE_8 = "iphone_8";

	private static final String SAMSUNG_S8 = "samsung_s8";

	private static final String GB_32 = "16 gb";
	private static final String GB_64 = "64 gb";

	private static final String WHITE = "white";
	private static final String RED = "red";


	private DefaultTmaPoVariantFacade tmaPoVariantFacade;
	private List<ProductData> inputProductData;
	private List<ProductData> groupedProductData;

	@Before
	public void setUp()
	{
		this.tmaPoVariantFacade = new DefaultTmaPoVariantFacade();
		this.inputProductData = new ArrayList();
	}

	@Test
	public void givenEmptyProductDataList_thenReturnEmptyList()
	{
		givenProducts();
		whenProductDataIsGrouped();
		thenGroupedProductDataIs(new ArrayList());
	}

	@Test
	public void givenProductDataList_thenReturnGroupedList()
	{
		ProductData p1 = createVariantProduct("iPhone_x_red_32gb", "iPhone x_red 32gb", BASE_PRODUCT_IPHONE_X, GB_32, RED);
		ProductData p2 = createVariantProduct("iPhone_x_white_32gb", "iPhone x white 32gb", BASE_PRODUCT_IPHONE_X, GB_32, WHITE);
		ProductData p3 = createVariantProduct("iPhone_8_red_32gb", "iPhone 8 red 32gb", BASE_PRODUCT_IPHONE_8, GB_32, RED);
		ProductData p4 = createVariantProduct("iPhone_8_red_64gb", "iPhone 8 red 64gb", BASE_PRODUCT_IPHONE_8, GB_64, RED);
		ProductData p5 = createVariantProduct("iPhone_8_white_64gb", "iPhone 8 white 64gb", BASE_PRODUCT_IPHONE_8, GB_64, WHITE);
		ProductData samsungS8 = createProduct(SAMSUNG_S8, "Samsung S8");

		givenProducts(p1, p2, samsungS8, p3, p4, p5);
		whenProductDataIsGrouped();

		//combine expected variants in a single product
		p1.setName(BASE_PRODUCT_IPHONE_X + " " + GB_32);
		p1.setColors(Arrays.asList(RED, WHITE));
		p3.setName(BASE_PRODUCT_IPHONE_8 + " " + GB_32);
		p3.setColors(Arrays.asList(RED));
		p4.setName(BASE_PRODUCT_IPHONE_8 + " " + GB_64);
		p4.setColors(Arrays.asList(RED, WHITE));
		final List<ProductData> expectedGrouped = Arrays.asList(p1, samsungS8, p3, p4);

		thenGroupedProductDataIs(expectedGrouped);
	}

	private void givenProducts(ProductData... data)
	{
		this.inputProductData.addAll(Arrays.asList(data));
	}

	private void whenProductDataIsGrouped()
	{
		this.groupedProductData = this.tmaPoVariantFacade.groupByBaseProduct(inputProductData);
	}

	private void thenGroupedProductDataIs(final List<ProductData> expectedGrouped)
	{
		assertEquals(expectedGrouped, groupedProductData);
	}

	private ProductData createProduct(final String code, final String name)
	{
		final ProductData productData = new ProductData();
		productData.setCode(code);
		productData.setName(name);
		return productData;
	}

	private ProductData createVariantProduct(final String code, final String name, final String baseProduct,
			final String storageSize,
			final String... colors)
	{
		final ProductData productData = createProduct(code, name);
		productData.setBaseProduct(baseProduct);
		productData.setStorageSize(storageSize);
		final ArrayList colorsList = new ArrayList();
		colorsList.add(colors);
		productData.setColors(colorsList);
		return productData;
	}
}
