/*
 *  Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.daos.impl;

import de.hybris.platform.billingaccountservices.daos.BaBillingAccountDao;
import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.enums.BaPaymentStatus;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaPartyModel;
import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation of {@link BaBillingAccountDao}.
 *
 * @since 2105.
 */
public class DefaultBaBillingAccountDao extends DefaultGenericDao<BaBillingAccountModel> implements BaBillingAccountDao
{
	private static final String BILLING_ACCOUNTS_QUERY = "SELECT DISTINCT {ba.pk} FROM {"
			+ BaBillingAccountModel._TYPECODE + " AS ba LEFT JOIN "
			+ BaPaymentStatus._TYPECODE + " AS ps ON {ba.paymentStatus}={ps.pk} LEFT JOIN "
			+ BaPartyRoleModel._BAACCOUNT2BAPARTYROLERELATION + " AS rel ON {ba.pk}={rel.source} LEFT JOIN "
			+ BaPartyRoleModel._TYPECODE + " AS pr ON {rel.target}={pr.pk} LEFT JOIN "
			+ BaPartyModel._TYPECODE + " AS pa ON {pr.party}={pa.pk}} WHERE {ba.pk} is not null";

	private static final String BILLING_ACCOUNTS_COUNT_QUERY = "SELECT COUNT (DISTINCT {ba.pk}) FROM {"
			+ BaBillingAccountModel._TYPECODE + " AS ba LEFT JOIN "
			+ BaPaymentStatus._TYPECODE + " AS ps ON {ba.paymentStatus}={ps.pk} LEFT JOIN "
			+ BaPartyRoleModel._BAACCOUNT2BAPARTYROLERELATION + " AS rel ON {ba.pk}={rel.source} LEFT JOIN "
			+ BaPartyRoleModel._TYPECODE + " AS pr ON {rel.target}={pr.pk} LEFT JOIN "
			+ BaPartyModel._TYPECODE + " AS pa ON {pr.party}={pa.pk}} WHERE {ba.pk} is not null";

	private static final String PARTY_FILTER = " and {pa.id}=?partyId";
	private static final String PAYMENT_STATUS_FILTER = " and {ps.code}=?paymentStatusCode";

	public DefaultBaBillingAccountDao()
	{
		super(BaBillingAccountModel._TYPECODE);
	}

	@Override
	public List<BaAccountModel> getBillingAccounts(final BaBillingAccountContext billingAccountContext, final Integer offset,
			final Integer limit)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(billingAccountContext, BILLING_ACCOUNTS_QUERY);
		if (limit != null)
		{
			searchQuery.setCount(limit);
		}
		if (offset != null)
		{
			searchQuery.setStart(offset);
		}
		final SearchResult<BaAccountModel> result = getFlexibleSearchService().search(searchQuery);
		return result.getResult();
	}

	@Override
	public Integer getNumberOfBillingAccountsFor(final BaBillingAccountContext billingAccountContext)
	{
		final FlexibleSearchQuery searchQuery = buildSearchQuery(billingAccountContext, BILLING_ACCOUNTS_COUNT_QUERY);
		searchQuery.setResultClassList(Collections.singletonList(Integer.class));
		return (Integer) getFlexibleSearchService().search(searchQuery).getResult().get(0);
	}

	private FlexibleSearchQuery buildSearchQuery(final BaBillingAccountContext billingAccountContext, final String baseSearchQuery)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(baseSearchQuery);
		if (StringUtils.isNotEmpty(billingAccountContext.getRelatedPartyId()))
		{
			stringBuilder.append(PARTY_FILTER);
		}
		if (StringUtils.isNotEmpty(billingAccountContext.getPaymentStatus()))
		{
			stringBuilder.append(PAYMENT_STATUS_FILTER);
		}
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(stringBuilder);
		addSearchQueryParameters(searchQuery, billingAccountContext);
		return searchQuery;
	}

	private void addSearchQueryParameters(final FlexibleSearchQuery searchQuery,
			final BaBillingAccountContext billingAccountContext)
	{
		if (StringUtils.isNotEmpty(billingAccountContext.getRelatedPartyId()))
		{
			searchQuery.addQueryParameter("partyId", billingAccountContext.getRelatedPartyId());
		}
		if (StringUtils.isNotEmpty(billingAccountContext.getPaymentStatus()))
		{
			searchQuery.addQueryParameter("paymentStatusCode", billingAccountContext.getPaymentStatus());
		}
	}
}
