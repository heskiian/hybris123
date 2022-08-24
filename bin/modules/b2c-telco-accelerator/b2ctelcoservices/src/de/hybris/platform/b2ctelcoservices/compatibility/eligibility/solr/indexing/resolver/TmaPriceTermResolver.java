/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.List;
import java.util.Set;


/**
 * Resolves Solr Property for the term(s) given an {@link PriceRowModel}.
 * When there are no terms defined (meaning the price is applicable for all) an empty element will be added to the Solr doc.
 *
 * @since 1810
 */
public class TmaPriceTermResolver extends AbstractValueResolver<PriceRowModel, Object, Object>
{
	private TmaTermInfoSource<String> tmaTermInfoSource;

	@Override
	protected void addFieldValues(final InputDocument doc, final IndexerBatchContext batchCtx, final IndexedProperty prop,
			final PriceRowModel price, final ValueResolverContext<Object, Object> resolverCtx) throws FieldValueProviderException
	{
		final Set<SubscriptionTermModel> subscriptionTerms = price.getSubscriptionTerms();
		final CurrencyModel currency = price.getCurrency();

		if (prop.isLocalized())
		{
			for (final LanguageModel language : batchCtx.getFacetSearchConfig().getIndexConfig().getLanguages())
			{
				addFieldToDoc(doc, prop, subscriptionTerms, currency, language);
			}
		}
		else
		{
			addFieldToDoc(doc, prop, subscriptionTerms, currency, null);
		}
	}

	private void addFieldToDoc(final InputDocument doc, final IndexedProperty prop,
			final Set<SubscriptionTermModel> subscriptionTerms, final CurrencyModel currency, final LanguageModel language)
			throws FieldValueProviderException
	{
		final List<String> fieldValues = getTmaTermInfoSource().getItem(subscriptionTerms, language, currency);
		// you might be tempted to make the specifier for the solr field consider both currency and language
		// "<currency>_<language>" (e.g. prop_usd_en_string_mv) - the values will never be retrieved from the index
		// unless you provide a specific qualifier provider see de.hybris.platform.solrfacetsearch.search.impl.DefaultFieldNameTranslator
		if (language == null)
		{
			doc.addField(prop, fieldValues);
		}
		else
		{
			doc.addField(prop, fieldValues, language.getIsocode());
		}
	}

	public TmaTermInfoSource<String> getTmaTermInfoSource()
	{
		return tmaTermInfoSource;
	}

	public void setTmaTermInfoSource(final TmaTermInfoSource<String> tmaTermInfoSource)
	{
		this.tmaTermInfoSource = tmaTermInfoSource;
	}
}
