/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementservices.daos.impl;

import de.hybris.platform.billmanagementservices.daos.BmPartyBillDao;
import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementservices.model.BmPartyModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation of {@link BmPartyBillDao}.
 *
 * @since 2108.
 */
public class DefaultBmPartyBillDao extends DefaultGenericDao<BmPartyBillModel> implements BmPartyBillDao
{
	private static final String PARTY_BILLS_QUERY = "SELECT DISTINCT {bill.pk} FROM {"
			+ BmPartyBillModel._TYPECODE + " AS bill LEFT JOIN "
			+ BmPartyModel._BMPARTYBILL2BMPARTYRELATION + " AS rel ON {bill.pk}={rel.source} LEFT JOIN "
			+ BmPartyModel._TYPECODE + " AS pa ON {rel.target}={pa.pk}} WHERE {bill.pk} is not null";

	private static final String PARTY_BILLS_COUNT_QUERY = "SELECT COUNT (DISTINCT {bill.pk}) FROM {"
			+ BmPartyBillModel._TYPECODE + " AS bill LEFT JOIN "
			+ BmPartyModel._BMPARTYBILL2BMPARTYRELATION + " AS rel ON {bill.pk}={rel.source} LEFT JOIN "
			+ BmPartyModel._TYPECODE + " AS pa ON {rel.target}={pa.pk}} WHERE {bill.pk} is not null";

	private static final String PARTY_FILTER = " and {pa.id}=?partyId";

	public DefaultBmPartyBillDao()
	{
		super(BmPartyBillModel._TYPECODE);
	}

	@Override
	public BmPartyBillModel findUnique(final Map<String, ? extends Object> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<BmPartyBillModel> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + BmPartyBillModel._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}

	@Override
	public List<BmPartyBillModel> getPartyBills(final BmPartyBillContext bmPartyBillContext, final Integer offset,
			final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(bmPartyBillContext, PARTY_BILLS_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<BmPartyBillModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfPartyBillsFor(final BmPartyBillContext bmPartyBillContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(bmPartyBillContext, PARTY_BILLS_COUNT_QUERY);
		searchQuery.setResultClassList(Collections.singletonList(Integer.class));
		return (Integer) getFlexibleSearchService().search(searchQuery).getResult().get(0);
	}

	private FlexibleSearchQuery buildSearchQuery(final BmPartyBillContext bmPartyBillContext, final String baseSearchQuery)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseSearchQuery);
		if (StringUtils.isNotEmpty(bmPartyBillContext.getRelatedPartyId()))
		{
			stringBuilder.append(PARTY_FILTER);
		}

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(stringBuilder);
		addSearchQueryParameters(searchQuery, bmPartyBillContext);
		return searchQuery;
	}

	private void addSearchQueryParameters(final FlexibleSearchQuery searchQuery, final BmPartyBillContext bmPartyBillContext)
	{
		if (StringUtils.isNotEmpty(bmPartyBillContext.getRelatedPartyId()))
		{
			searchQuery.addQueryParameter("partyId", bmPartyBillContext.getRelatedPartyId());
		}
	}
}
