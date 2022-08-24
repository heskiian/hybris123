/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.daos.impl;

import de.hybris.platform.agreementservices.daos.AgrAgreementDao;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.enums.AgrAgreementStatus;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.model.AgrPartyModel;
import de.hybris.platform.agreementservices.model.AgrPartyRoleModel;
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
 * Default implementation of {@link AgrAgreementDao}.
 *
 * @since 2108
 */
public class DefaultAgrAgreementDao extends DefaultGenericDao<AgrAgreementModel> implements AgrAgreementDao
{

	private static final String AGREEMENTS_QUERY = "SELECT {agr.pk} FROM {"
			+ AgrAgreementModel._TYPECODE + " AS agr LEFT JOIN "
			+ AgrAgreementStatus._TYPECODE + " AS st ON {agr.status}={st.pk} LEFT JOIN "
			+ AgrPartyRoleModel._AGRAGREEMENT2AGRPARTYROLE + " AS rel ON {agr:" + AgrAgreementModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ AgrPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + AgrPartyRoleModel.PK + "} LEFT JOIN "
			+ AgrPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {agr.pk} is not null";

	private static final String AGREEMENTS_COUNT_QUERY = "SELECT COUNT (DISTINCT {agr.pk}) FROM {"
			+ AgrAgreementModel._TYPECODE + " AS agr LEFT JOIN "
			+ AgrAgreementStatus._TYPECODE + " AS st ON {agr.status}={st.pk} LEFT JOIN "
			+ AgrPartyRoleModel._AGRAGREEMENT2AGRPARTYROLE + " AS rel ON {agr:" + AgrAgreementModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ AgrPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + AgrPartyRoleModel.PK + "} LEFT JOIN "
			+ AgrPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {agr.pk} is not null";

	private static final String PARTY_FILTER = " and {pa.id}=?partyId";
	private static final String STATUS_FILTER = " and {st.code}=?statusCode";

	public DefaultAgrAgreementDao()
	{
		super(AgrAgreementModel._TYPECODE);
	}

	@Override
	public AgrAgreementModel findUnique(final Map<String, ?> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<AgrAgreementModel> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + AgrAgreementModel._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}

	@Override
	public List<AgrAgreementModel> getAgreements(final AgrAgreementContext agrAgreementContext,
			final Integer offset, final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(agrAgreementContext, AGREEMENTS_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<AgrAgreementModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfAgreementsFor(final AgrAgreementContext agrAgreementContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(agrAgreementContext, AGREEMENTS_COUNT_QUERY);
		searchQuery.setResultClassList(Collections.singletonList(Integer.class));
		return (Integer) getFlexibleSearchService().search(searchQuery).getResult().get(0);
	}

	private FlexibleSearchQuery buildSearchQuery(final AgrAgreementContext agrAgreementContext, final String baseSearchQuery)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseSearchQuery);
		if (StringUtils.isNotEmpty(agrAgreementContext.getRelatedPartyId()))
		{
			stringBuilder.append(PARTY_FILTER);
		}
		if (StringUtils.isNotEmpty(agrAgreementContext.getStatus()))
		{
			stringBuilder.append(STATUS_FILTER);
		}

		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(stringBuilder);
		addSearchQueryParameters(searchQuery, agrAgreementContext);
		return searchQuery;
	}

	private void addSearchQueryParameters(final FlexibleSearchQuery searchQuery, final AgrAgreementContext agrAgreementContext)
	{
		if (StringUtils.isNotEmpty(agrAgreementContext.getRelatedPartyId()))
		{
			searchQuery.addQueryParameter("partyId", agrAgreementContext.getRelatedPartyId());
		}
		if (StringUtils.isNotEmpty(agrAgreementContext.getStatus()))
		{
			searchQuery.addQueryParameter("statusCode", agrAgreementContext.getStatus());
		}
	}

}
