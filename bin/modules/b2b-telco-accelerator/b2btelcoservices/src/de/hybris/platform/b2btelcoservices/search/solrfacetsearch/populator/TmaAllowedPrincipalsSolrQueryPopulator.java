/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * This clause will filter the results based on the allowed principals.
 *
 * @since 2105
 */
public class TmaAllowedPrincipalsSolrQueryPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String EMPTY_OR_NULL_PRINCIPAL = "\"\" TO *";
	private static final String ALL_ROWS = "*:*";
	private static final String ALLOWED_PRINCIPAL = "allowedPrincipals_string_mv";
	private static final String CONDITION_OR = " OR ";
	private static final String CONDITION_AND = "AND";
	private static final String APPLY_ALLOWED_PRINCIPAL_FLAG = "applyAllowedPrincipalFlag";

	private final UserService userService;
	private final SessionService sessionService;

	public TmaAllowedPrincipalsSolrQueryPopulator(final UserService userService, final SessionService sessionService)
	{
		this.userService = userService;
		this.sessionService = sessionService;
	}

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final boolean applyAllowedPrincipalFilter = getAllowedPrincipalFlag();
		final UserModel currentUserModel = getUserService().getCurrentUser();

		if (getUserService().isAnonymousUser(currentUserModel) && Boolean.TRUE.equals(applyAllowedPrincipalFilter))
		{
			final String customRawQuery = "-" + ALLOWED_PRINCIPAL + ":[" + EMPTY_OR_NULL_PRINCIPAL + "]";
			final SearchQuery targetSearchQuery = target.getSearchQuery();
			targetSearchQuery.addFilterRawQuery(customRawQuery);
			return;
		}

		if (!getUserService().isAnonymousUser(currentUserModel))
		{
			final SearchQuery targetSearchQuery = target.getSearchQuery();
			targetSearchQuery.addFilterRawQuery(buildQuery(currentUserModel));
		}
	}

	private String buildQuery(final UserModel currentUserModel)
	{
		final String nullOrEmptyAllowedPrincipal = "-" + ALLOWED_PRINCIPAL + ":[" + EMPTY_OR_NULL_PRINCIPAL + "]";
		final String allRowsWithoutAllowedPrincipal = String
				.format("(%s %s %s)", nullOrEmptyAllowedPrincipal, CONDITION_AND, ALL_ROWS);

		final Set<String> allPrincipalUids = new HashSet<>();
		allPrincipalUids.add(currentUserModel.getUid());
		final Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(currentUserModel);
		userGroups.stream().filter(Objects::nonNull).forEach(userGroup -> allPrincipalUids.add(userGroup.getUid()));
		final String customRawQueryParam = String.join(CONDITION_OR, allPrincipalUids);

		return String.format("%s %s %s:(%s)", allRowsWithoutAllowedPrincipal, CONDITION_OR, ALLOWED_PRINCIPAL, customRawQueryParam);
	}

	private boolean getAllowedPrincipalFlag()
	{
		return getSessionService().getAttribute(APPLY_ALLOWED_PRINCIPAL_FLAG) != null
				? getSessionService().getAttribute(APPLY_ALLOWED_PRINCIPAL_FLAG)
				: Boolean.TRUE;
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
