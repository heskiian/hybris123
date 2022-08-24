/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptionservices.daos.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.usageconsumptionservices.daos.UcUsageVolumeProductDao;
import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcNetworkProductModel;
import de.hybris.platform.usageconsumptionservices.model.UcPartyModel;
import de.hybris.platform.usageconsumptionservices.model.UcPartyRoleModel;
import de.hybris.platform.usageconsumptionservices.model.UcProductModel;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation of {@link UcUsageVolumeProductDao}.
 *
 * @since 2108
 */
public class DefaultUcUsageVolumeProductDao extends DefaultGenericDao<UcUsageVolumeProductModel>
		implements UcUsageVolumeProductDao
{
	private static final String USAGE_VOLUME_PRODUCT_QUERY = "SELECT DISTINCT {uvpo.pk} FROM {"
			+ UcUsageVolumeProductModel._TYPECODE + " AS uvpo LEFT JOIN "
			+ UcUsageVolumeProductModel._UCNETWORKPRODUCT2UCUSAGEVOLUMEPRODUCTRELATION + " AS prodRel ON {uvpo:"
			+ UcUsageVolumeProductModel.PK + " }={prodRel:target} LEFT JOIN "
			+ UcNetworkProductModel._TYPECODE + " AS npo ON {prodRel:source}={npo:" + UcNetworkProductModel.PK + "} LEFT JOIN "
			+ UcPartyRoleModel._UCPRODUCT2UCPARTYROLERELATION + " AS rel ON {npo:" + UcProductModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ UcPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + UcPartyRoleModel.PK + "} LEFT JOIN "
			+ UcPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {uvpo.pk} is not null";

	private static final String USAGE_VOLUME_PRODUCT_COUNT_QUERY = "SELECT COUNT (DISTINCT {uvpo.pk}) FROM {"
			+ UcUsageVolumeProductModel._TYPECODE + " AS uvpo LEFT JOIN "
			+ UcUsageVolumeProductModel._UCNETWORKPRODUCT2UCUSAGEVOLUMEPRODUCTRELATION + " AS prodRel ON {uvpo:"
			+ UcUsageVolumeProductModel.PK + " }={prodRel:target} LEFT JOIN "
			+ UcNetworkProductModel._TYPECODE + " AS npo ON {prodRel:source}={npo:" + UcNetworkProductModel.PK + "} LEFT JOIN "
			+ UcPartyRoleModel._UCPRODUCT2UCPARTYROLERELATION + " AS rel ON {npo:" + UcProductModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ UcPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + UcPartyRoleModel.PK + "} LEFT JOIN "
			+ UcPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {uvpo.pk} is not null";

	private static final String PARTY_FILTER = " and {pa.id}=?partyId";
	private static final String PRODUCT_ID_FILTER = " and {npo.id}=?productId";
	private static final String PUBLIC_IDENTIFIER_FILTER = " and {npo.publicIdentifier}=?publicIdentifier";

	public DefaultUcUsageVolumeProductDao()
	{
		super(UcUsageVolumeProductModel._TYPECODE);
	}

	@Override
	public List<UcUsageVolumeProductModel> getUsageVolumeProducts(final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final Integer offset, final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(ucUsageVolumeProductContext, USAGE_VOLUME_PRODUCT_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<UcUsageVolumeProductModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfUsageVolumeProductFor(final UcUsageVolumeProductContext ucUsageVolumeProductContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(ucUsageVolumeProductContext, USAGE_VOLUME_PRODUCT_COUNT_QUERY);
		searchQuery.setResultClassList(Collections.singletonList(Integer.class));
		return (Integer) getFlexibleSearchService().search(searchQuery).getResult().get(0);
	}

	private FlexibleSearchQuery buildSearchQuery(final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final String baseSearchQuery)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseSearchQuery);
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getRelatedPartyId()))
		{
			stringBuilder.append(PARTY_FILTER);
		}
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getProductId()))
		{
			stringBuilder.append(PRODUCT_ID_FILTER);
		}

		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getPublicIdentifier()))
		{
			stringBuilder.append(PUBLIC_IDENTIFIER_FILTER);
		}

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(stringBuilder);
		addSearchQueryParameters(searchQuery, ucUsageVolumeProductContext);
		return searchQuery;
	}

	private void addSearchQueryParameters(final FlexibleSearchQuery searchQuery,
			final UcUsageVolumeProductContext ucUsageVolumeProductContext)
	{
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getRelatedPartyId()))
		{
			searchQuery.addQueryParameter("partyId", ucUsageVolumeProductContext.getRelatedPartyId());
		}
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getProductId()))
		{
			searchQuery.addQueryParameter("productId", ucUsageVolumeProductContext.getProductId());
		}
		if (StringUtils.isNotEmpty(ucUsageVolumeProductContext.getPublicIdentifier()))
		{
			searchQuery.addQueryParameter("publicIdentifier", ucUsageVolumeProductContext.getPublicIdentifier());
		}
	}

}
