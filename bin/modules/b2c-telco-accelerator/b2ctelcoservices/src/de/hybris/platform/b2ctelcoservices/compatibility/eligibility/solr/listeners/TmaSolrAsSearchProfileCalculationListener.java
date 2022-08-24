/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.listeners;


import de.hybris.platform.adaptivesearchsolr.listeners.SolrAsSearchProfileCalculationListener;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Search profile calculation listener that accommodates the adaptive search profile for TUA indexes.
 *
 * @since 2011
 */
public class TmaSolrAsSearchProfileCalculationListener extends SolrAsSearchProfileCalculationListener
{

	private static final Logger LOG = LoggerFactory.getLogger(TmaSolrAsSearchProfileCalculationListener.class);

	private static final String COLLAPSE_FIELDS_STRING_QUERY_FORMAT = "{"
			+ "!collapse field=spo_string "
			+ "sort=\"%s desc\""
			+ "}";
	private static final String PRICE_PRIORITY = "pricePriority_int";

	private final CommerceCommonI18NService i18nService;

	public TmaSolrAsSearchProfileCalculationListener(final CommerceCommonI18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	/**
	 * The search query is updated by adding collapse results option.
	 * {@inheritDoc}
	 */
	@Override
	public void beforeSearch(final FacetSearchContext facetSearchContext) throws FacetSearchException
	{
		if (facetSearchContext.getFacetSearchConfig().getSearchConfig().isLegacyMode())
		{
			LOG.warn("Adaptive search does not support search legacy mode: {}/{}",
					facetSearchContext.getFacetSearchConfig().getName(), facetSearchContext.getIndexedType().getIdentifier());
		}
		else
		{
			if (PriceRowModel._TYPECODE.equals(facetSearchContext.getIndexedType().getCode()))
			{
				facetSearchContext.getSearchQuery().addFilterRawQuery(
						String.format(COLLAPSE_FIELDS_STRING_QUERY_FORMAT, PRICE_PRIORITY));
			}

			super.beforeSearch(facetSearchContext);
		}
	}

	protected CommerceCommonI18NService getI18nService()
	{
		return i18nService;
	}
}
