/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services;

import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.subscription.BillingTimeService;


/**
 * Service responsible for handling {@link BillingTimeModel} objects.
 *
 * @since 2007
 */
public interface TmaBillingTimeService extends BillingTimeService
{
	/**
	 * Retrieves the billing time configured as default.
	 *
	 * @return the default billing time
	 */
	BillingTimeModel getDefaultBillingTime();
}
