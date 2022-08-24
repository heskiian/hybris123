/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract Field Value Provider for generic operations to be used as base Value Provider
 *
 * @since 6.7
 */
public abstract class TmaAbstractPropertyFieldValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider
{
	protected static final TmaProcessType DEFAULT_CATEGORY_PAGE_PROCESS_TYPE = TmaProcessType.ACQUISITION;

	private FieldNameProvider fieldNameProvider;

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected void addFieldValuesForCurrency(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final CurrencyModel currency, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				currency.getIsocode().toLowerCase());
		fieldValues.addAll(fieldNames.stream().map(fieldName -> new FieldValue(fieldName, value)).collect(Collectors.toList()));
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
