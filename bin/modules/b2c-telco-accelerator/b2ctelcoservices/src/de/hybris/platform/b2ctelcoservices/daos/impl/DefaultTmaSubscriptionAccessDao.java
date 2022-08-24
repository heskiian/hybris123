/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionAccessDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;


/**
 * Default implementation of the {@link TmaSubscriptionAccessDao}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionAccessDao extends DefaultGenericDao<TmaSubscriptionAccessModel> implements
		TmaSubscriptionAccessDao
{
	private static final String GET_SUBSCRIPTION_ACCESSES_BY_SUBSCRIBER_IDENTITY = "SELECT {sa:" + TmaSubscriptionAccessModel.PK
			+ "} FROM {" + TmaSubscriptionAccessModel._TYPECODE + " AS sa JOIN " + TmaSubscriptionBaseModel._TYPECODE + " AS sb ON "
			+ "{sa:" + TmaSubscriptionAccessModel.SUBSCRIPTIONBASE + "}={sb:" + TmaSubscriptionBaseModel.PK + "}}" + " WHERE {sb:"
			+ TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY + "}=?" + TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY + " AND {sb:"
			+ TmaSubscriptionBaseModel.BILLINGSYSTEMID + "}=?" + TmaSubscriptionBaseModel.BILLINGSYSTEMID;

	private static final String GET_SUBSCRIPTION_ACCESSES_BY_TYPE = "SELECT {sa:" + TmaSubscriptionAccessModel.PK + "}"
			+ " FROM {" + TmaSubscriptionAccessModel._TYPECODE + " AS sa" + " JOIN " + TmaAccessType._TYPECODE + " AS at ON"
			+ " {sa:" + TmaSubscriptionAccessModel.ACCESSTYPE + "}={at:" + EnumerationValueModel.PK + "}" + " JOIN "
			+ PrincipalModel._TYPECODE + " AS p ON" + " {sa:" + TmaSubscriptionAccessModel.PRINCIPAL + "}={p:" + PrincipalModel.PK
			+ "}}" + " WHERE {p:" + PrincipalModel.UID + "}=?" + PrincipalModel.UID + " AND {at:" + EnumerationValueModel.CODE
			+ "}=?" + EnumerationValueModel.CODE;

	private static final String GET_SUBSCRIPTION_ACCESS_BY_PRINCIPAL_AND_SUBSCRIPTION_BASE = "SELECT {sa:"
			+ TmaSubscriptionAccessModel.PK + "} FROM { " + TmaSubscriptionAccessModel._TYPECODE + " AS sa JOIN "
			+ PrincipalModel._TYPECODE + " AS p ON {sa:" + TmaSubscriptionAccessModel.PRINCIPAL + "}={p:" + PrincipalModel.PK
			+ "} JOIN " + TmaSubscriptionBaseModel._TYPECODE + " AS sb ON {sa:" + TmaSubscriptionAccessModel.SUBSCRIPTIONBASE
			+ "}={sb:" + TmaSubscriptionBaseModel.PK + "}} WHERE {p:" + PrincipalModel.UID + "}=?" + PrincipalModel.UID
			+ " AND {sb:" + TmaSubscriptionBaseModel.BILLINGSYSTEMID + "}=?" + TmaSubscriptionBaseModel.BILLINGSYSTEMID
			+ " AND {sb:" + TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY + "}=?" + TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY;

	private static final String GET_SUBSCRIPTION_ACCESSES_BY_PRINCIPAL_UID = "SELECT {sa:" + TmaSubscriptionAccessModel.PK
			+ "} FROM { " + TmaSubscriptionAccessModel._TYPECODE + " AS sa JOIN " + PrincipalModel._TYPECODE + " AS p ON {sa:"
			+ TmaSubscriptionAccessModel.PRINCIPAL + "}={p:" + PrincipalModel.PK + "}} WHERE {p:" + PrincipalModel.UID + "}=?"
			+ PrincipalModel.UID;

	public DefaultTmaSubscriptionAccessDao()
	{
		super(TmaSubscriptionAccessModel._TYPECODE);
	}

	@Override
	public TmaSubscriptionAccessModel findSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity)
	{
		validateNotNullSubscriptionAccessMandatoryFields(principalUid, billingSystemId, subscriberIdentity);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_SUBSCRIPTION_ACCESS_BY_PRINCIPAL_AND_SUBSCRIPTION_BASE);
		query.addQueryParameter(PrincipalModel.UID, principalUid);
		query.addQueryParameter(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);
		query.addQueryParameter(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		return getFlexibleSearchService().searchUnique(query);
	}

	@Override
	public List<TmaSubscriptionAccessModel> findSubscriptionAccessesBySubscriberIdentity(final String subscriberIdentity,
			final String billingSystemId)
	{
		validateNotNullSubscriptionBaseMandatoryFields(billingSystemId, subscriberIdentity);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_SUBSCRIPTION_ACCESSES_BY_SUBSCRIBER_IDENTITY);
		query.addQueryParameter(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
		query.addQueryParameter(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);
		return getFlexibleSearchService().<TmaSubscriptionAccessModel> search(query).getResult();
	}

	@Override
	public List<TmaSubscriptionAccessModel> findSubscriptionAccessesByType(final String principalUid,
			final TmaAccessType accessType)
	{
		validateParameterNotNullStandardMessage(PrincipalModel.UID, principalUid);
		validateParameterNotNullStandardMessage(TmaSubscriptionAccessModel.ACCESSTYPE, accessType);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_SUBSCRIPTION_ACCESSES_BY_TYPE);
		query.addQueryParameter(PrincipalModel.UID, principalUid);
		query.addQueryParameter(EnumerationValueModel.CODE, accessType.getCode());
		return getFlexibleSearchService().<TmaSubscriptionAccessModel> search(query).getResult();
	}

	@Override
	public List<TmaSubscriptionAccessModel> findSubscriptionAccessByPrincipalUid(final String principalUid)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_SUBSCRIPTION_ACCESSES_BY_PRINCIPAL_UID);
		query.addQueryParameter(PrincipalModel.UID, principalUid);
		return getFlexibleSearchService().<TmaSubscriptionAccessModel> search(query).getResult();
	}

	private void validateNotNullSubscriptionBaseMandatoryFields(final String billingSystemId, final String subscriberIdentity)
	{
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.BILLINGSYSTEMID, billingSystemId);
		validateParameterNotNullStandardMessage(TmaSubscriptionBaseModel.SUBSCRIBERIDENTITY, subscriberIdentity);
	}

	private void validateNotNullSubscriptionAccessMandatoryFields(final String principalUid, final String billingSystemId,
			final String subscriberIdentity)
	{
		validateParameterNotNullStandardMessage(PrincipalModel.UID, principalUid);
		validateNotNullSubscriptionBaseMandatoryFields(billingSystemId, subscriberIdentity);
	}
}
