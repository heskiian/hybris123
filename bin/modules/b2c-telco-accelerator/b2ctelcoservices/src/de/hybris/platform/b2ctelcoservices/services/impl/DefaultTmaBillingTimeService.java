/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.subscription.impl.DefaultBillingTimeService;


/**
 * Default implementation for {@link TmaBillingTimeService}
 *
 * @since 2007
 */
public class DefaultTmaBillingTimeService extends DefaultBillingTimeService implements TmaBillingTimeService
{
	private static final String DEFAULT_BILLING_TIME_CODE_KEY = "defaultBillingTimeCode";

	private ConfigurationService configurationService;

	public DefaultTmaBillingTimeService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public BillingTimeModel getDefaultBillingTime()
	{
		final String payNowBillingTimeCode = getConfigurationService().getConfiguration().getString(DEFAULT_BILLING_TIME_CODE_KEY);
		return getBillingTimeForCode(payNowBillingTimeCode);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
