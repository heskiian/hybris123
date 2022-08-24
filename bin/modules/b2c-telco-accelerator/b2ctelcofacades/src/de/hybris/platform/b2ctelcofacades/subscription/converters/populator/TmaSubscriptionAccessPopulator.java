/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaAccessTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link TmaSubscriptionAccessData} from a {@link TmaSubscriptionAccessModel} entity.
 *
 * @since 6.6
 */
public class TmaSubscriptionAccessPopulator implements Populator<TmaSubscriptionAccessModel, TmaSubscriptionAccessData>
{
	private Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> subscriptionBaseDataConverter;

	@Override
	public void populate(final TmaSubscriptionAccessModel subscriptionAccessModel,
			final TmaSubscriptionAccessData subscriptionAccessData)
	{
		subscriptionAccessData.setPrincipalUid(subscriptionAccessModel.getPrincipal().getUid());
		subscriptionAccessData.setBillingSystemId(subscriptionAccessModel.getSubscriptionBase().getBillingSystemId());
		subscriptionAccessData.setSubscriberIdentity(subscriptionAccessModel.getSubscriptionBase().getSubscriberIdentity());
		subscriptionAccessData.setAccessType(TmaAccessTypeData.valueOf(subscriptionAccessModel.getAccessType().getCode()));
		if (subscriptionAccessModel.getSubscriptionBase() != null)
		{
			subscriptionAccessData
					.setSubscriptionBase(getSubscriptionBaseDataConverter().convert(subscriptionAccessModel.getSubscriptionBase()));
		}
	}

	protected Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> getSubscriptionBaseDataConverter()
	{
		return subscriptionBaseDataConverter;
	}

	@Required
	public void setSubscriptionBaseDataConverter(
			Converter<TmaSubscriptionBaseModel, TmaSubscriptionBaseData> subscriptionBaseDataConverter)
	{
		this.subscriptionBaseDataConverter = subscriptionBaseDataConverter;
	}
}
