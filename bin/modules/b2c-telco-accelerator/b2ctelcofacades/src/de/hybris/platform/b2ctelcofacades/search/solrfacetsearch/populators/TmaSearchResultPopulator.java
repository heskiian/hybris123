/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.search.solrfacetsearch.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;


/**
 * Abstract Search Result Populator to be extended by populators handling the Search Results as retrieved from solr.
 *
 * @since 2007
 */
public abstract class TmaSearchResultPopulator implements Populator<SearchResultValueData, ProductData>
{
	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		return source.getValues() == null ? null : (T) source.getValues().get(propertyName);
	}
}
