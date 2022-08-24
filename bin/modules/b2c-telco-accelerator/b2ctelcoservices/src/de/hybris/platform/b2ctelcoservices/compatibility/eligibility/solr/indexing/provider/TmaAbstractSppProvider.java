/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.provider;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.TmaSpoSource;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl.TmaAbstractPropertyFieldValueProvider;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract provider defining common methods and fields to be used for SppProviders.
 *
 * @since 1903
 */
public abstract class TmaAbstractSppProvider extends TmaAbstractPropertyFieldValueProvider
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TmaSppIsBundledProvider.class);
	/**
	 * Resolves Solr Property for the spo-{@link TmaSimpleProductOfferingModel} given an {@link PriceRowModel}.
	 */
	private TmaSpoSource spoSource;

	/**
	 * Creates and returns a list of fieldValues that contains the model's property for an indexed property.
	 *
	 * @param indexedProperty
	 * 		The indexed property
	 * @param model
	 * 		The object whose value is indexed
	 * @return A list of fieldValues that contains the model's property for an indexed property
	 */
	protected abstract Collection<FieldValue> getValues(IndexedProperty indexedProperty, Object model);

	/**
	 * Returns a list of fieldValues that contains the model's value for an indexed property.
	 *
	 * @param indexedProperty
	 * 		The indexed property
	 * @param model
	 * 		The object whose value is indexed
	 * @return A list of fieldValues that contains the model's value for an indexed property
	 * @inheritDoc TmaAbstractPropertyFieldValueProvider
	 */
	@Override
	public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model)
	{
		LOGGER.debug("resolving {} for: {}", indexedProperty.getName(), model.getClass());

		return model instanceof PriceRowModel ?
				getValues(indexedProperty, getSpoSource().getProduct((PriceRowModel) model)) :
				Collections.emptyList();

	}

	public TmaSpoSource getSpoSource()
	{
		return spoSource;
	}

	@Required
	public void setSpoSource(TmaSpoSource spoSource)
	{
		this.spoSource = spoSource;
	}
}
