/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import java.util.Collection;


/**
 * Resolves SOLR properties for the product given a {@link PriceRowModel}.
 *
 * @since 2105
 */
public class TmaPoPropertyResolver implements ValueResolver<PriceRowModel>
{
	private TmaSpoSource tmaSpoSource;

	public TmaPoPropertyResolver(final TmaSpoSource tmaSpoSource)
	{
		this.tmaSpoSource = tmaSpoSource;
	}

	@Override
	public void resolve(InputDocument inputDocument, IndexerBatchContext indexerBatchContext,
			Collection<IndexedProperty> props, PriceRowModel price) throws FieldValueProviderException
	{
		final ProductModel product = getTmaSpoSource().getProduct(price);
		for (final IndexedProperty indexedProperty : props)
		{
			inputDocument.addField(indexedProperty, product.getProperty(indexedProperty.getName()));
		}
	}

	protected TmaSpoSource getTmaSpoSource()
	{
		return tmaSpoSource;
	}
}
