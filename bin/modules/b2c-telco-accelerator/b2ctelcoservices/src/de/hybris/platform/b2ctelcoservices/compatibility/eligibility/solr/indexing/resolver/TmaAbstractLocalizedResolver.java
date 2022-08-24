/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.ValueResolver;

import java.util.Collection;


/**
 * Resolves SOLR localized properties.
 *
 * @since 2105.
 */
public abstract class TmaAbstractLocalizedResolver implements ValueResolver<ItemModel>
{
	private CommonI18NService commonI18NService;

	public TmaAbstractLocalizedResolver(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void resolve(final InputDocument inputDocument, final IndexerBatchContext batchContext,
			final Collection<IndexedProperty> props, final ItemModel item)
	{
		final FacetSearchConfig facetSearchConfig = batchContext.getFacetSearchConfig();
		final IndexConfig indexConfig = facetSearchConfig.getIndexConfig();
		final Collection<LanguageModel> languages = indexConfig.getLanguages();

		for (final LanguageModel language : languages)
		{
			props.stream().filter(IndexedProperty::isLocalized).forEach(indexedProperty ->
					addFieldValue(inputDocument, indexedProperty, item, language));
		}
	}

	protected abstract void addFieldValue(final InputDocument document, final IndexedProperty indexedProperty,
			final ItemModel item, final LanguageModel language);

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}
}
