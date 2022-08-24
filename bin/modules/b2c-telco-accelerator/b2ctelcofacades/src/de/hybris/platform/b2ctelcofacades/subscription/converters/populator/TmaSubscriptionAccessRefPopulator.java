/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaAccessTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates basic attribute of subscription access in {@link TmaSubscriptionAccessData} for reference purpose with
 * source as
 * {@link TmaSubscriptionAccessModel} entity.
 *
 * @since 2102
 */
public class TmaSubscriptionAccessRefPopulator implements Populator<TmaSubscriptionAccessModel, TmaSubscriptionAccessData>
{
	@Override
	public void populate(final TmaSubscriptionAccessModel subscriptionAccessModel,
			final TmaSubscriptionAccessData subscriptionAccessData)
	{
		subscriptionAccessData.setPrincipalUid(subscriptionAccessModel.getPrincipal().getUid());
		subscriptionAccessData.setSubscriberIdentity(subscriptionAccessModel.getSubscriptionBase().getSubscriberIdentity());
		subscriptionAccessData.setAccessType(TmaAccessTypeData.valueOf(subscriptionAccessModel.getAccessType().getCode()));
	}
}
