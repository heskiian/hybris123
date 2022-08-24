/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.populator;

import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaRetrieveUserPriceGroupsStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This clause will filter the results based on the user price group/s.
 *
 * @since 1903
 */
public class TmaUserGroupSolrFilterPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String USERPRICEGROUP_FIELD = "userpricegroup_string";
	private static final String ALL_USERGROUP_PRICES = "\"\"";
	private TmaRetrieveUserPriceGroupsStrategy userPriceGroupsStrategy;
	private UserService userService;


	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, IndexedType, IndexedProperty, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		final UserModel userModel = getUserService().getCurrentUser();
		final Set<UserPriceGroup> userPriceGroups = getUserPriceGroupsStrategy().getAllUserPriceGroups(userModel);

		if (CollectionUtils.isNotEmpty(userPriceGroups))
		{
			target.getSearchQuery().addFilterRawQuery(createUserPriceGroupsCondition(userPriceGroups));
		}
	}

	/**
	 * This method builds a filterRawQuery string condition based on user's price groups.
	 *
	 * @return a string containing a filterRawQuery condition
	 */
	protected String createUserPriceGroupsCondition(final Set<UserPriceGroup> userPriceGroups)
	{
		final List<String> priceGroups = new ArrayList<>();
		priceGroups.add(ALL_USERGROUP_PRICES);

		for (UserPriceGroup priceGroup : userPriceGroups)
		{
			priceGroups.add(priceGroup.getCode());
		}

		final String conditionParams = String.join(" OR ", priceGroups);

		return String.format("%s:(%s)", USERPRICEGROUP_FIELD, conditionParams);
	}

	protected TmaRetrieveUserPriceGroupsStrategy getUserPriceGroupsStrategy()
	{
		return userPriceGroupsStrategy;
	}

	@Required
	public void setUserPriceGroupsStrategy(
			TmaRetrieveUserPriceGroupsStrategy userPriceGroupsStrategy)
	{
		this.userPriceGroupsStrategy = userPriceGroupsStrategy;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}
}
