/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import java.util.Collection;

import org.apache.log4j.Logger;


/**
 * Resolves SOLR property for the bpo given a {@link PriceRowModel}.
 *
 * @since 2105
 */
public class TmaBpoResolver implements ValueResolver<PriceRowModel>
{
	private static final Logger LOG = Logger.getLogger(TmaBpoResolver.class);

	@Override
	public void resolve(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final Collection<IndexedProperty> props, final PriceRowModel price)
	{
		if (price.getProduct() instanceof TmaFixedBundledProductOfferingModel || price
				.getProduct() instanceof TmaSimpleProductOfferingModel)
		{
			return;
		}

		props.forEach(indexedProperty -> {
			try
			{
				inputDocument.addField(indexedProperty, price.getProduct().getCode());
			}
			catch (FieldValueProviderException ex)
			{
				LOG.error("Solr indexing error ", ex);
			}
		});
	}
}
