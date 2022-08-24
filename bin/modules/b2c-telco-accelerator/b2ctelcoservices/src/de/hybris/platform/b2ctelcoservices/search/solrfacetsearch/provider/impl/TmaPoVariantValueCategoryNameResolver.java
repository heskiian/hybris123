/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.variants.model.VariantValueCategoryModel;


/**
 * Field Value Provider for indexing variant value category name of the product offering variant.
 *
 * @since 1810
 */
public class TmaPoVariantValueCategoryNameResolver extends TmaAbstractPoVariantValueCategoryDetailsResolver
{
	@Override
	protected String getValue(final VariantValueCategoryModel variantValueCategory)
	{
		return variantValueCategory.getName();
	}
}