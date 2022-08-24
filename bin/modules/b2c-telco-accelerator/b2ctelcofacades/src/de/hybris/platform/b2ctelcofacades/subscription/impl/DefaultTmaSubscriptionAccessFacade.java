/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaAccessTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionAccessFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscriptionAccessFacade}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionAccessFacade implements TmaSubscriptionAccessFacade
{
	private TmaSubscriptionAccessService tmaSubscriptionAccessService;
	private Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter;

	@Override
	public List<TmaSubscriptionAccessData> findSubscriptionAccessesByPrincipal(final String principalUid)
	{
		final List<TmaSubscriptionAccessModel> tmaSubscriptionAccessModels = getTmaSubscriptionAccessService()
				.getSubscriptionAccessesByPrincipalUid(principalUid);
		final List<TmaSubscriptionAccessData> tmaSubscriptionAccessDatas = new ArrayList<>();
		tmaSubscriptionAccessModels.forEach(tmaSubscriptionAccessModel -> tmaSubscriptionAccessDatas
				.add(getTmaSubscriptionAccessConverter().convert(tmaSubscriptionAccessModel)));
		return tmaSubscriptionAccessDatas;
	}

	@Override
	public TmaSubscriptionAccessModel getSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity)
	{
		return getTmaSubscriptionAccessService().getSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId,
				subscriberIdentity);
	}

	@Override
	public List<TmaSubscriptionAccessData> getSubscriptionAccessesBySubscriberIdentity(final String subscriberIdentity,
			final String billingSystemId)
	{
		final List<TmaSubscriptionAccessModel> subscriptionAccesses = getTmaSubscriptionAccessService()
				.getSubscriptionAccessesBySubscriberIdentity(billingSystemId, subscriberIdentity);
		return getTmaSubscriptionAccessConverter().convertAll(subscriptionAccesses);
	}

	protected Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> getTmaSubscriptionAccessConverter()
	{
		return tmaSubscriptionAccessConverter;
	}

	@Required
	public void setTmaSubscriptionAccessConverter(
			final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessConverter)
	{
		this.tmaSubscriptionAccessConverter = tmaSubscriptionAccessConverter;
	}

	protected TmaSubscriptionAccessService getTmaSubscriptionAccessService()
	{
		return tmaSubscriptionAccessService;
	}

	@Required
	public void setTmaSubscriptionAccessService(final TmaSubscriptionAccessService tmaSubscriptionAccessService)
	{
		this.tmaSubscriptionAccessService = tmaSubscriptionAccessService;
	}

	private TmaAccessType getAccessTypeFromAccessTypeData(final TmaAccessTypeData accessTypeData)
	{
		return TmaAccessType.valueOf(accessTypeData.name());
	}

}
