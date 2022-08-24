/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoVariantService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link DefaultTmaPoVariantService}.
 *
 * @since 1810
 */
@UnitTest
public class DefaultTmaPoVariantServiceUnitTest
{
	private static final String COLOR = "color";
	private static final String STORAGE_SIZE = "storage_size";
	private TmaPoVariantService variantService;
	private TmaPoVariantModel variantModel;
	private String variantCategoryCode;
	private Optional<VariantValueCategoryModel> result;

	@Before
	public void setUp()
	{
		this.variantService = new DefaultTmaPoVariantService();
	}

	@Test
	public void givenNullVariant_WhenVariantValueCategoryIsFetched_ThenReturnEmpty()
	{
		givenVariant(null);
		whenVariantValueCategoryIsFetched();
		thenVariantValueCategoryIs(Optional.empty());
	}

	@Test
	public void givenVariantWithoutSupercategories_WhenVariantValueCategoryIsFetched_ThenReturnEmpty()
	{
		givenVariant(createVariant(null));
		whenVariantValueCategoryIsFetched();
		thenVariantValueCategoryIs(Optional.empty());
	}

	@Test
	public void givenVariantWithSupercategories_WhenVariantValueCategoryIsFetched_ThenReturnCategory()
	{
		final VariantValueCategoryModel colorCategory = createCategory(COLOR);
		final VariantValueCategoryModel storageCategory = createCategory(STORAGE_SIZE);

		givenVariant(createVariant(Arrays.asList(colorCategory, storageCategory)));
		givenVariantCategoryCode(STORAGE_SIZE);
		whenVariantValueCategoryIsFetched();
		thenVariantValueCategoryIs(Optional.of(storageCategory));
	}

	@Test
	public void givenVariantWithoutTheRequiredSupercategory_WhenVariantValueCategoryIsFetched_ThenReturnEmpty()
	{
		final VariantValueCategoryModel colorCategory = createCategory(COLOR);

		givenVariant(createVariant(Arrays.asList(colorCategory)));
		givenVariantCategoryCode(STORAGE_SIZE);
		whenVariantValueCategoryIsFetched();
		thenVariantValueCategoryIs(Optional.empty());
	}

	private void givenVariant(final TmaPoVariantModel variantModel)
	{
		this.variantModel = variantModel;
	}

	private void givenVariantCategoryCode(final String variantCategoryCode)
	{
		this.variantCategoryCode = variantCategoryCode;
	}

	private void whenVariantValueCategoryIsFetched()
	{
		this.result = this.variantService
				.getVariantValueCategory(variantModel, variantCategoryCode);
	}

	private void thenVariantValueCategoryIs(final Optional<Object> expected)
	{
		assertEquals(expected, result);
	}

	private TmaPoVariantModel createVariant(final List<CategoryModel> supercategories)
	{
		final TmaPoVariantModel variant = mock(TmaPoVariantModel.class);
		when(variant.getSupercategories()).thenReturn(supercategories);
		return variant;
	}

	private VariantValueCategoryModel createCategory(String... variantCategories)
	{
		final List<CategoryModel> supercategories = new ArrayList<>();
		Arrays.stream(variantCategories).forEach(code -> {
			VariantCategoryModel variantCategory = mock(VariantCategoryModel.class);
			when(variantCategory.getCode()).thenReturn(code);
			supercategories.add(variantCategory);
		});
		final VariantValueCategoryModel variantValueCategory = mock(VariantValueCategoryModel.class);
		when(variantValueCategory.getSupercategories()).thenReturn(supercategories);
		return variantValueCategory;
	}
}
