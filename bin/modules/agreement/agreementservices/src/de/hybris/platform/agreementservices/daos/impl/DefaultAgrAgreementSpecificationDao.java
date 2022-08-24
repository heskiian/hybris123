/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.daos.impl;

import de.hybris.platform.agreementservices.daos.AgrAgreementSpecificationDao;
import de.hybris.platform.agreementservices.data.AgrAgreementContext;
import de.hybris.platform.agreementservices.enums.AgrAgreementLifecycleStatus;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
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
 * Default implementation of {@link AgrAgreementSpecificationDao}.
 *
 * @since 2108
 */
public class DefaultAgrAgreementSpecificationDao extends DefaultGenericDao<AgrAgreementSpecificationModel>
		implements AgrAgreementSpecificationDao
{
	private static final String AGREEMENTS_SPECIFICATION_QUERY = "SELECT {agrspec.pk} FROM {"
			+ AgrAgreementSpecificationModel._TYPECODE + " AS agrspec LEFT JOIN "
			+ AgrAgreementLifecycleStatus._TYPECODE + " AS lst ON {agrspec.lifecycleStatus}={lst.pk} LEFT JOIN "
			+ AgrPartyRoleModel._AGRAGREEMENTSPECIFICATION2AGRPARTYROLE + " AS rel ON {agrspec:" + AgrAgreementSpecificationModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ AgrPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + AgrPartyRoleModel.PK + "} LEFT JOIN "
			+ AgrPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {agrspec.pk} is not null";

	private static final String AGREEMENTS_SPECIFICATION_COUNT_QUERY = "SELECT COUNT (DISTINCT {agrspec.pk}) FROM {"
			+ AgrAgreementSpecificationModel._TYPECODE + " AS agrspec LEFT JOIN "
			+ AgrAgreementLifecycleStatus._TYPECODE + " AS lst ON {agrspec.lifecycleStatus}={lst.pk} LEFT JOIN "
			+ AgrPartyRoleModel._AGRAGREEMENTSPECIFICATION2AGRPARTYROLE + " AS rel ON {agrspec:" + AgrAgreementSpecificationModel.PK
			+ " }={rel:source} LEFT JOIN "
			+ AgrPartyRoleModel._TYPECODE + " AS pr " + " ON {rel:target}={pr:" + AgrPartyRoleModel.PK + "} LEFT JOIN "
			+ AgrPartyModel._TYPECODE + " as pa ON {pr.party}={pa.pk}} WHERE {agrspec.pk} is not null";

	private static final String PARTY_FILTER = " and {pa.id}=?partyId";
	private static final String LIFECYCLE_STATUS_FILTER = " and {lst.code}=?lifecycleStatusCode";

	public DefaultAgrAgreementSpecificationDao()
	{
		super(AgrAgreementSpecificationModel._TYPECODE);
	}

	@Override
	public AgrAgreementSpecificationModel findUnique(final Map<String, ?> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<AgrAgreementSpecificationModel> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + AgrAgreementSpecificationModel._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}

	@Override
	public List<AgrAgreementSpecificationModel> getAgreementSpecifications(final AgrAgreementContext agrAgreementContext,
			final Integer offset, final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(agrAgreementContext, AGREEMENTS_SPECIFICATION_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<AgrAgreementSpecificationModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfAgreementSpecificationsFor(final AgrAgreementContext agrAgreementContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(agrAgreementContext, AGREEMENTS_SPECIFICATION_COUNT_QUERY);
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
		if (StringUtils.isNotEmpty(agrAgreementContext.getLifecycleStatus()))
		{
			stringBuilder.append(LIFECYCLE_STATUS_FILTER);
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
		if (StringUtils.isNotEmpty(agrAgreementContext.getLifecycleStatus()))
		{
			searchQuery.addQueryParameter("lifecycleStatusCode", agrAgreementContext.getLifecycleStatus());
		}
	}

}
