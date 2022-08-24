/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;


import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


/**
 * This populator will adds userId in rawQueryfilter.
 *
 * @since 2007
 */
public class TmaUserSolrFilterPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String ALL_USER = "\"\"";
	private static final String USER = "user_string";
	private static final String APPLY_ELIGIBILITY_FLAG = "applyEligibility";
	private final UserService userService;
	private final SessionService sessionService;

	public TmaUserSolrFilterPopulator(final UserService userService, final SessionService sessionService)
	{
		this.userService = userService;
		this.sessionService = sessionService;
	}

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final Boolean eligibilityFlag = getSessionService().getAttribute(APPLY_ELIGIBILITY_FLAG);
		if (Boolean.TRUE.equals(eligibilityFlag))
		{
			final UserModel currentUser = getUserService().getCurrentUser();
			target.getSearchQuery().addFilterRawQuery(builderQuery(currentUser.getUid(), USER));
		}
	}

	private String builderQuery(final String values, final String paramName)
	{
		String query = null;
		if (!values.isEmpty())
		{
			query = paramName + ":(" + values + ") OR " + paramName + ":(" + ALL_USER + ")";
		}
		return query;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}
}