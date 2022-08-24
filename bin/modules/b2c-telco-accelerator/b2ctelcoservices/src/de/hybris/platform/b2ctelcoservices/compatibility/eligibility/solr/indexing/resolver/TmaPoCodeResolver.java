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

import org.apache.log4j.Logger;


/**
 * Resolves SOLR property for the spo given a {@link PriceRowModel}.
 *
 * @since 2105
 */
public class TmaPoCodeResolver implements ValueResolver<PriceRowModel>
{
	private static final Logger LOG = Logger.getLogger(TmaPoCodeResolver.class);

	private TmaSpoSource tmaSpoSource;

	public TmaPoCodeResolver(final TmaSpoSource tmaSpoSource)
	{
		this.tmaSpoSource = tmaSpoSource;
	}

	@Override
	public void resolve(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final Collection<IndexedProperty> props, final PriceRowModel price)
	{
		final ProductModel product = getTmaSpoSource().getProduct(price);

		props.forEach(indexedProperty -> {
			try
			{
				inputDocument.addField(indexedProperty, product.getCode());
			}
			catch (FieldValueProviderException ex)
			{
				LOG.error("Solr indexing error ", ex);
			}
		});
	}

	protected TmaSpoSource getTmaSpoSource()
	{
		return tmaSpoSource;
	}
}
