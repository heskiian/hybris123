/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl.pricing;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;


/**
 * Field Value Provider for indexing the Price Priority.
 *
 * @since 2007
 */
public class TmaPricePriorityValueResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final PriceRowModel priceRowModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (priceRowModel == null || priceRowModel.getPriority() == null)
		{
			return;
		}

		final String fieldName = String.format("%s_%s", indexedProperty.getName(), indexedProperty.getType());
		inputDocument.addField(fieldName, priceRowModel.getPriority());
	}
}
