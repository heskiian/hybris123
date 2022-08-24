/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Resolves SOLR localized description property for the product given a {@link PriceRowModel}.
 *
 * @since 2105
 */
public class TmaPoDescriptionResolver extends TmaAbstractLocalizedResolver
{
	private static final Logger LOG = Logger.getLogger(TmaPoDescriptionResolver.class);
	private TmaSpoSource tmaSpoSource;

	public TmaPoDescriptionResolver(final TmaSpoSource tmaSpoSource, final CommonI18NService commonI18NService)
	{
		super(commonI18NService);
		this.tmaSpoSource = tmaSpoSource;
	}

	@Override
	protected void addFieldValue(final InputDocument document, final IndexedProperty indexedProperty, final ItemModel item,
			final LanguageModel language)
	{
		final String description =
				getTmaSpoSource().getProduct((PriceRowModel) item)
						.getDescription(getCommonI18NService().getLocaleForLanguage(language));
		if (!StringUtils.isBlank(description))
		{
			try
			{
				document.addField(indexedProperty, description, language.getIsocode());
			}
			catch (final FieldValueProviderException ex)
			{
				LOG.error("Solr indexing error ", ex);
			}
		}
	}

	protected TmaSpoSource getTmaSpoSource()
	{
		return tmaSpoSource;
	}
}
