/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;


/**
 * Abstract Field Value Provider for indexing base product details of the product offering variant.
 *
 * @since 1810
 */
public abstract class TmaAbstractPoVariantBaseProductDetailsResolver extends AbstractValueResolver<ProductModel, Object, Object>
{
	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel model,
			final ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
	{
		if (model instanceof TmaPoVariantModel)
		{
			final TmaSimpleProductOfferingModel baseSpo = ((TmaPoVariantModel) model).getTmaBasePo();
			if (baseSpo != null)
			{
				document.addField(indexedProperty, getValue(baseSpo), resolverContext.getFieldQualifier());
			}
		}
	}

	/**
	 * Returns the value to be indexed.
	 */
	protected abstract Object getValue(final TmaSimpleProductOfferingModel baseSpo);
}
