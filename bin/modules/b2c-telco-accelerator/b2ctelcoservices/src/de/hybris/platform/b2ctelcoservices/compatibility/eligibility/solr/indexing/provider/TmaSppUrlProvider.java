/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.provider;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductUrlValueProvider;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;


/**
 * @since 1810
 */
public class TmaSppUrlProvider extends ProductUrlValueProvider
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TmaSppUrlProvider.class);
	private TmaSpoSource spoSource;

	@Override
	public Collection<FieldValue> getFieldValues(IndexConfig cfg, IndexedProperty prop, Object model)
			throws FieldValueProviderException
	{
		LOGGER.debug("resolving " + prop.getName() + " for: " + model + " " + model.getClass());
		return model instanceof PriceRowModel ?
				super.getFieldValues(cfg, prop, spoSource.getProduct((PriceRowModel) model)) :
				Collections.emptyList();
	}

	public TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

	public void setSpoSource(TmaSpoSource spoSource)
	{
		this.spoSource = spoSource;
	}

}
