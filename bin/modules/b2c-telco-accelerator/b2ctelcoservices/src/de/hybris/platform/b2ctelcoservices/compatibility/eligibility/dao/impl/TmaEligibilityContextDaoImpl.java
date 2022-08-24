/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.TmaEligibilityContextDao;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashSet;
import java.util.Set;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of {@link TmaEligibilityContextDao}
 *
 * @since 1907
 */
public class TmaEligibilityContextDaoImpl extends DefaultGenericDao<TmaEligibilityContextModel>
		implements TmaEligibilityContextDao
{
	private static final String GET_ELIGIBILITY_CONTEXT = "SELECT {ec:" + TmaEligibilityContextModel.PK
			+ "} FROM {" + TmaEligibilityContextModel._TYPECODE + " AS ec JOIN " + CustomerModel._TYPECODE + " AS user ON {ec:"
			+ TmaEligibilityContextModel.CUSTOMER + "}={user:" + CustomerModel.PK + "}} "
			+ "WHERE {user:" + CustomerModel.UID + "}=?uid ";

	public TmaEligibilityContextDaoImpl()
	{
		super(TmaEligibilityContextModel._TYPECODE);
	}

	@Override
	public Set<TmaEligibilityContextModel> getEligibilityContext(String customerId, String subscriberIdentity)
	{
		validateParameterNotNullStandardMessage(CustomerModel.UID, customerId);
		final StringBuilder getEligibilityContextQuery = new StringBuilder(GET_ELIGIBILITY_CONTEXT);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(getEligibilityContextQuery);

		if (subscriberIdentity != null)
		{
			getEligibilityContextQuery.append(" AND {ec:"
					+ TmaEligibilityContextModel.SUBSCRIBERID + "}=? ");
			query.addQueryParameter(TmaEligibilityContextModel.SUBSCRIBERID, subscriberIdentity);
		}

		query.addQueryParameter(CustomerModel.UID, customerId);

		return new HashSet<>(getFlexibleSearchService().<TmaEligibilityContextModel>search(query).getResult());
	}

	@Override
	public Set<TmaEligibilityContextModel> getAllEligibilityContexts()
	{
		String queryString = "SELECT {" + TmaEligibilityContextModel.PK + "} FROM {" + TmaEligibilityContextModel._TYPECODE + "}";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return new HashSet<>(getFlexibleSearchService().<TmaEligibilityContextModel>search(query).getResult());
	}
}
