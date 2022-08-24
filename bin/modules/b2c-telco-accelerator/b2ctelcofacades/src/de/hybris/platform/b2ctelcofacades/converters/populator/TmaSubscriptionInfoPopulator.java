/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel} as source and
 * {@link  de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;} as target type.
 *
 * @since 1907
 */
public class TmaSubscriptionInfoPopulator
		implements Populator<TmaCartSubscriptionInfoModel, TmaCartSubscriptionInfoData>
{
	private Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter;

	@Override
	public void populate(final TmaCartSubscriptionInfoModel source,
			final TmaCartSubscriptionInfoData target)
	{
		target.setBillingSystemId(source.getBillingSystemId());
		target.setSubscribedProductId(source.getSubscribedProductId());
		target.setSubscriberIdentity(source.getSubscriberIdentity());
		target.setSubscribedProductAction(source.getSubscribedProductAction());
		addSubscriptionTerm(source, target);
	}

	protected void addSubscriptionTerm(TmaCartSubscriptionInfoModel source,
			TmaCartSubscriptionInfoData target)
	{
		if (source.getSubscriptionTerm() != null)
		{
			target.setSubscriptionTerm(
					getTmaSubscriptionTermConverter().convert(source.getSubscriptionTerm()));
		}
	}

	protected Converter<SubscriptionTermModel, SubscriptionTermData> getTmaSubscriptionTermConverter()
	{
		return tmaSubscriptionTermConverter;
	}

	@Required
	public void setTmaSubscriptionTermConverter(
			Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter)
	{
		this.tmaSubscriptionTermConverter = tmaSubscriptionTermConverter;
	}
}
