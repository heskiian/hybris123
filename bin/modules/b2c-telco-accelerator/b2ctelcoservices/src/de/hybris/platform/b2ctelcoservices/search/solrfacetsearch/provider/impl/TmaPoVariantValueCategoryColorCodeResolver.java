/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.variants.model.VariantValueCategoryModel;


/**
 * Field Value Provider for indexing color code value from the variant value category of the product offering variant.
 *
 * @since 1810
 */
public class TmaPoVariantValueCategoryColorCodeResolver extends TmaAbstractPoVariantValueCategoryDetailsResolver
{
	@Override
	protected String getValue(final VariantValueCategoryModel variantValueCategory)
	{
		return variantValueCategory.getColorCode();
	}
}