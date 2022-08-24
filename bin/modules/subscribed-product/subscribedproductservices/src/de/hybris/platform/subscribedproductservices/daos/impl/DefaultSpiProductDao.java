/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.daos.impl;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.subscribedproductservices.daos.SpiProductDao;
import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.enums.SpiProductStatusType;
import de.hybris.platform.subscribedproductservices.model.SpiBillingAccountModel;
import de.hybris.platform.subscribedproductservices.model.SpiPartyModel;
import de.hybris.platform.subscribedproductservices.model.SpiPartyRoleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation of {@link SpiProductDao}
 *
 * @since 2105
 */
public class DefaultSpiProductDao extends DefaultGenericDao<SpiProductModel> implements SpiProductDao
{
	private static final String PRODUCTS_QUERY = "SELECT {p.pk} FROM {"
			+ SpiProductModel._TYPECODE + " AS p LEFT JOIN "
			+ SpiProductStatusType._TYPECODE + " as s ON {p.status}={s.pk} LEFT JOIN "
			+ SpiBillingAccountModel._TYPECODE + " as b ON {p.billingAccount}={b.PK} LEFT JOIN "
			+ SpiPartyRoleModel._SPIPRODUCT2SPIPARTYROLERELATION + " AS rel ON {p:" + SpiProductModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ SpiPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + SpiPartyRoleModel.PK + "} LEFT JOIN "
			+ SpiPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {p.pk} is not null";


	private static final String PRODUCTS_COUNT_QUERY = "SELECT COUNT (DISTINCT {p.pk}) FROM {"
			+ SpiProductModel._TYPECODE + " AS p LEFT JOIN "
			+ SpiProductStatusType._TYPECODE + " as s ON {p.status}={s.pk} LEFT JOIN "
			+ SpiBillingAccountModel._TYPECODE + " as b ON {p.billingAccount}={b.PK} LEFT JOIN "
			+ SpiPartyRoleModel._SPIPRODUCT2SPIPARTYROLERELATION + " AS rel ON {p:" + SpiProductModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ SpiPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + SpiPartyRoleModel.PK + "} LEFT JOIN "
			+ SpiPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {p.pk} is not null";

	private static final String BILLING_ACCOUNT_FILTER = " and {b.id}=?billingId";
	private static final String PARTY_FILTER = " and {pa.id}=?partyId";
	private static final String STATUS_FILTER = " and {s.code}=?statusCode";

	public DefaultSpiProductDao()
	{
		super(SpiProductModel._TYPECODE);
	}

	@Override
	public SpiProductModel findUnique(final Map<String, ? extends Object> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<SpiProductModel> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + SpiProductModel._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}

	@Override
	public List<SpiProductModel> getProducts(final SpiProductContext spiProductContext, final Integer offset,
			final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(spiProductContext, PRODUCTS_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<SpiProductModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfProductsFor(final SpiProductContext spiProductContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(spiProductContext, PRODUCTS_COUNT_QUERY);
		searchQuery.setResultClassList(Collections.singletonList(Integer.class));
		return (Integer) getFlexibleSearchService().search(searchQuery).getResult().get(0);
	}

	private FlexibleSearchQuery buildSearchQuery(final SpiProductContext spiProductContext, final String baseSearchQuery)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(baseSearchQuery);
		if (StringUtils.isNotEmpty(spiProductContext.getBillingAccountId()))
		{
			sb.append(BILLING_ACCOUNT_FILTER);
		}
		if (StringUtils.isNotEmpty(spiProductContext.getRelatedPartyId()))
		{
			sb.append(PARTY_FILTER);
		}
		if (StringUtils.isNotEmpty(spiProductContext.getStatus()))
		{
			sb.append(STATUS_FILTER);
		}
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(sb);
		addSearchQueryParameters(searchQuery, spiProductContext);
		return searchQuery;
	}

	private void addSearchQueryParameters(final FlexibleSearchQuery searchQuery, final SpiProductContext spiProductContext)
	{
		if (StringUtils.isNotEmpty(spiProductContext.getBillingAccountId()))
		{
			searchQuery.addQueryParameter("billingId", spiProductContext.getBillingAccountId());
		}
		if (StringUtils.isNotEmpty(spiProductContext.getRelatedPartyId()))
		{
			searchQuery.addQueryParameter("partyId", spiProductContext.getRelatedPartyId());
		}
		if (StringUtils.isNotEmpty(spiProductContext.getStatus()))
		{
			searchQuery.addQueryParameter("statusCode", spiProductContext.getStatus());
		}
	}
}
