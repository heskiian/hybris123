/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Resolves Solr Property for the channel(s) given an {@link PriceRowModel}.
 * When there is no channel defined (meaning the price is applicable for all) an empty element will be added to the Solr doc.
 *
 * @since 1810
 */
public class TmaPriceChannelResolver implements ValueResolver<PriceRowModel>
{

	@Override
	public void resolve(InputDocument doc, IndexerBatchContext batchCtx, Collection<IndexedProperty> props, PriceRowModel price)
			throws FieldValueProviderException
	{
		for (final IndexedProperty indexedProperty : props)
		{
			final Set<PriceRowChannel> distributionChannels = price.getDistributionChannels();
			if (CollectionUtils.isNotEmpty(distributionChannels))
			{
				doc.addField(indexedProperty, distributionChannels.stream().map(PriceRowChannel::getCode).collect(Collectors.toList()));
				continue;
			}
			doc.addField(indexedProperty, StringUtils.EMPTY);
		}
	}
}
