/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionBaseDao;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Default implementation of the {@link TmaSubscriptionBaseDao}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionBaseDao extends DefaultGenericDao<TmaSubscriptionBaseModel> implements TmaSubscriptionBaseDao
{
	private static final String GET_ALL_SUBSCRIPTION_BASES = "SELECT {" + TmaSubscriptionBaseModel.PK + "} FROM {"
			+ TmaSubscriptionBaseModel._TYPECODE + "}";

	public DefaultTmaSubscriptionBaseDao()
	{
		super(TmaSubscriptionBaseModel._TYPECODE);
	}

	@Override
	public TmaSubscriptionBaseModel findSubscriptionBase(final String subscriberIdentity, final String billingSystemId)
	{
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);

		final Map<String, String> queryParams = new HashMap<>();
		queryParams.put(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		queryParams.put(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);
		final Collection<TmaSubscriptionBaseModel> subscriptionBases = find(queryParams);
		if (CollectionUtils.isEmpty(subscriptionBases))
		{
			throw new ModelNotFoundException("Could not find " + TmaSubscriptionBaseModel._TYPECODE + " for " +
					TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY + "'" + subscriberIdentity + " and "
					+ TmaSubscriptionBaseModel.BILLINGSYSTEMID + " " + billingSystemId + "'. ");
		}

		return subscriptionBases.iterator().next();
	}

	@Override
	public TmaSubscriptionBaseModel findSubscriptionBaseByIdentity(final String subscriberIdentity)
	{
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);

		final Map<String, String> queryParams = new HashMap<>();
		queryParams.put(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		final Collection<TmaSubscriptionBaseModel> subscriptionBases = find(queryParams);
		if (CollectionUtils.isEmpty(subscriptionBases))
		{
			throw new ModelNotFoundException("Could not find " + TmaSubscriptionBaseModel._TYPECODE + " for " +
					TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY + "'" + subscriberIdentity + "'. ");
		}
		return subscriptionBases.iterator().next();
	}

	@Override
	public Set<TmaSubscriptionBaseModel> findAllSubscriptionBases()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_SUBSCRIPTION_BASES);
		return new HashSet<>(getFlexibleSearchService().<TmaSubscriptionBaseModel> search(query).getResult());
	}
}
