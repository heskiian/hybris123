/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
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


/**
 * Resolves Solr Property for the spo-{@link TmaSimpleProductOfferingModel} given an {@link PriceRowModel}.
 *
 * @since 1810
 */
public class TmaPriceProcessResolver implements ValueResolver<PriceRowModel>
{

	public static final String ALL_PROCESS_TYPES = " ";

	@Override
	public void resolve(final InputDocument doc, final IndexerBatchContext batchCtx, final Collection<IndexedProperty> props,
			final PriceRowModel price) throws FieldValueProviderException
	{
		for (final IndexedProperty prop : props)
		{
			final Set<TmaProcessType> processTypes = price.getProcessTypes();
			if (CollectionUtils.isEmpty(processTypes))
			{
				doc.addField(prop, ALL_PROCESS_TYPES);
				continue;
			}
			doc.addField(prop, processTypes.stream().map(TmaProcessType::getCode).collect(Collectors.toList()));
		}
	}
}
