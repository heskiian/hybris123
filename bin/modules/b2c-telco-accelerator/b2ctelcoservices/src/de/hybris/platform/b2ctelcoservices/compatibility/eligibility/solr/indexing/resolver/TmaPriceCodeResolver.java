/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


/**
 * Resolves Solr Property for the code (as PK) given an {@link PriceRowModel}.
 *
 * @since 1810
 */
public class TmaPriceCodeResolver implements ValueResolver<PriceRowModel>
{

	private static final Logger LOGGER = LoggerFactory.getLogger(TmaPriceCodeResolver.class);

	@Override
	public void resolve(InputDocument doc, IndexerBatchContext batchCtx, Collection<IndexedProperty> props, PriceRowModel price)
			throws FieldValueProviderException
	{
		LOGGER.debug("resolving spo code for price:" + price + " " + price.getClass());
		// for all properties that are linked to this provider/resolver as defined in the impex file.
		for (IndexedProperty prop : props)
		{
			doc.addField(prop, price.getPk());
		}
	}
}
