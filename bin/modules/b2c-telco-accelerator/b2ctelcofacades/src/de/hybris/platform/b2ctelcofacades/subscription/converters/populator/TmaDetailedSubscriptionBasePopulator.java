/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Populates {@link TmaDetailedSubscriptionBaseData} from a {@link TmaSubscriptionAccessModel} entity.
 *
 * @since 1907
 */
public class TmaDetailedSubscriptionBasePopulator
		implements Populator<TmaSubscriptionAccessModel, TmaDetailedSubscriptionBaseData>
{
	/**
	 * Separator
	 */
	private static final String SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR = "__";

	/**
	 * Converter from {@link TmaBillingAccountModel} to {@link TmaBillingAccountData}
	 */
	private Converter<TmaBillingAccountModel, TmaBillingAccountData> billingAccountConverter;

	@Override
	public void populate(final TmaSubscriptionAccessModel source, final TmaDetailedSubscriptionBaseData target)
	{
		validateParameterNotNull(source, "source cannot be null");
		validateParameterNotNull(target, "target cannot be null");

		if (StringUtils.isNotEmpty(source.getSubscriptionBase().getSubscriberIdentity()) && StringUtils
				.isNotEmpty(source.getSubscriptionBase().getBillingSystemId()))
		{
			target.setCode(
					source.getSubscriptionBase().getSubscriberIdentity() + SUBSCRIPTION_IDENTITY_BILLING_SYSTEM_SEPARATOR + source
							.getSubscriptionBase().getBillingSystemId());
		}

		target.setSubscriberIdentity(source.getSubscriptionBase().getSubscriberIdentity());
		target.setBillingSystemId(source.getSubscriptionBase().getBillingSystemId());
		if (source.getSubscriptionBase().getBillingAccount() != null)
		{
			target.setBillingAccountData(getBillingAccountConverter().convert(source.getSubscriptionBase().getBillingAccount()));
		}
	}

	protected Converter<TmaBillingAccountModel, TmaBillingAccountData> getBillingAccountConverter()
	{
		return billingAccountConverter;
	}

	@Required
	public void setBillingAccountConverter(
			Converter<TmaBillingAccountModel, TmaBillingAccountData> billingAccountConverter)
	{
		this.billingAccountConverter = billingAccountConverter;
	}
}
