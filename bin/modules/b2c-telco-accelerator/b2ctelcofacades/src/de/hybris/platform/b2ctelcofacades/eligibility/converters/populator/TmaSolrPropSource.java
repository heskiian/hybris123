/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.eligibility.converters.populator;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;


/**
 * @since 1810
 */
public class TmaSolrPropSource
{
	public <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		return source.getValues() == null ? null : (T) source.getValues().get(propertyName);
	}
}
