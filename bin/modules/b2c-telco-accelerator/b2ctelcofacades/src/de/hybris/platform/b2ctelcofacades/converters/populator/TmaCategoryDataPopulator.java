/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Populates {@link CategoryData} with details from {@link CategoryModel}.
 *
 * @since 1903
 */

public class TmaCategoryDataPopulator implements Populator<CategoryModel, CategoryData>
{
	@Override
	public void populate(CategoryModel categoryModel, CategoryData categoryData) throws ConversionException
	{
		categoryData.setType(categoryModel.getItemtype());
	}
}
