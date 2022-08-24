/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoVariantService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Field Value Provider for indexing variant value category details of the product offering variant.
 *
 * @since 1810
 */
public abstract class TmaAbstractPoVariantValueCategoryDetailsResolver extends AbstractValueResolver<ProductModel, Object, Object>
{
	private TmaPoVariantService tmaPoVariantService;
	private String variantCategoryCode;

	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		if (model instanceof TmaPoVariantModel)
		{
			final Optional<VariantValueCategoryModel> variantValueCategory = getTmaPoVariantService().getVariantValueCategory(
					(TmaPoVariantModel) model, variantCategoryCode);
			if (variantValueCategory.isPresent()) {
				document.addField(indexedProperty, getValue(variantValueCategory.get()), resolverContext.getFieldQualifier());
			}
		}
	}

	protected abstract Object getValue(final VariantValueCategoryModel variantValueCategory);

	protected TmaPoVariantService getTmaPoVariantService()
	{
		return tmaPoVariantService;
	}

	@Required
	public void setTmaPoVariantService(final TmaPoVariantService tmaPoVariantService)
	{
		this.tmaPoVariantService = tmaPoVariantService;
	}

	protected String getVariantCategoryCode()
	{
		return variantCategoryCode;
	}

	@Required
	public void setVariantCategoryCode(final String variantCategoryCode)
	{
		this.variantCategoryCode = variantCategoryCode;
	}
}
