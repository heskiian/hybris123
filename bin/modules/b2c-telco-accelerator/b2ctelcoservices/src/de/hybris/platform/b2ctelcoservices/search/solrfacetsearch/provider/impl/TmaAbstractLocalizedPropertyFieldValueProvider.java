/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract Field Value Provider for generic operations to be used as base Value Provider for localized solr indexes.
 *
 * @since 6.7
 */
public abstract class TmaAbstractLocalizedPropertyFieldValueProvider extends TmaAbstractPropertyFieldValueProvider
{
	private CommonI18NService commonI18NService;

	// todo - needs to be moved to another class and use composition not inheritance :)
	protected abstract String getLocalizedString(final Object model, final Locale locale);

	protected void addFieldValueForLanguages(final List<FieldValue> fields, final IndexedProperty indexedProperty,
			final Object model)
	{
		getCommonI18NService().getAllLanguages().forEach(languageModel ->
		{
			final Locale locale = getCommonI18NService().getLocaleForLanguage(languageModel);
			addFieldValues(fields, indexedProperty, languageModel, getLocalizedString(model, locale));
		});
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
