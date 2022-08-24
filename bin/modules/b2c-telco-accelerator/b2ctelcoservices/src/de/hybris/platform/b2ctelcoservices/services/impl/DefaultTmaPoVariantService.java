/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoVariantService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaPoVariantService}.
 *
 * @since 1810
 */
public class DefaultTmaPoVariantService implements TmaPoVariantService
{
	@Override
	public Optional<VariantValueCategoryModel> getVariantValueCategory(final TmaPoVariantModel variantModel,
			final String variantCategoryCode)
	{
		if (variantModel == null || CollectionUtils.isEmpty(variantModel.getSupercategories()))
		{
			return Optional.empty();
		}
		final List<VariantValueCategoryModel> variantValueCategories = variantModel.getSupercategories().stream()
				.filter(c -> (c instanceof VariantValueCategoryModel)).map(c -> ((VariantValueCategoryModel) c))
				.collect(Collectors.toList());
		return filterByParentCategory(variantValueCategories, variantCategoryCode);
	}

	private Optional<VariantValueCategoryModel> filterByParentCategory(final List<VariantValueCategoryModel> categories,
			final String parentCategoryCode)
	{
		for (final VariantValueCategoryModel variantValueCategory : categories)
		{
			final List<CategoryModel> superCategories = variantValueCategory.getSupercategories().stream()
					.filter(s -> s instanceof VariantCategoryModel)
					.filter(s -> s.getCode().equals(parentCategoryCode)).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(superCategories))
			{
				return Optional.of(variantValueCategory);
			}
		}
		return Optional.empty();
	}
}
