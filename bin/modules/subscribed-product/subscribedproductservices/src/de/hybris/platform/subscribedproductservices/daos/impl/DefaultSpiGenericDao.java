/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.daos.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.subscribedproductservices.daos.SpiGenericDao;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link SpiGenericDao}
 *
 * @since 2111
 */
public class DefaultSpiGenericDao implements SpiGenericDao
{
	private FlexibleSearchService flexibleSearchService;

	public DefaultSpiGenericDao(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public ItemModel getItem(final String typeCode, final String id)
	{
		validateParameterNotNull(id, "Parameter id can not be null");
		validateParameterNotNull(typeCode, "Parameter typeCode can not be null");

		final Map<String, String> params = new HashMap<>();
		params.put("id", id);
		return getItem(typeCode, params);
	}

	@Override
	public ItemModel getItem(final String typeCode, final Map<String, String> params)
	{
		validateParameterNotNull(typeCode, "Parameter typeCode can not be null");

		final StringBuilder builder = this.createQueryString(typeCode);
		if (!params.isEmpty())
		{
			this.appendWhereClausesToBuilder(builder, params);
		}

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(builder.toString());

		if (!params.isEmpty())
		{
			searchQuery.addQueryParameters(params);
		}

		final SearchResult<ItemModel> result = getFlexibleSearchService().search(searchQuery);

		if (CollectionUtils.isEmpty(result.getResult()))
		{
			return null;
		}
		return result.getResult().get(0);
	}

	private StringBuilder createQueryString(final String typeCode)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("GET {").append(typeCode).append("}");
		return builder;
	}

	private void appendWhereClausesToBuilder(final StringBuilder builder, final Map<String, String> params)
	{
		if (!params.isEmpty())
		{
			builder.append(" WHERE ")
					.append(params.keySet().stream().map(k -> String.format("{%s}=?%s", k, k)).collect(Collectors.joining(" AND ")));
		}
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
