/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.model;

import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;


/**
 * Attribute handler for dynamic attribute {@link UsageChargeModel#getBillingTime()}
 *
 * @since 1907
 */
public class TmaBillingTimeUsageChargeAttribute extends TmaBillingTimeAttributeHandler<UsageChargeModel>
{
	@Override
	protected SubscriptionPricePlanModel getSubscriptionPricePlan(UsageChargeModel model)
	{
		return model.getSubscriptionPricePlanUsage();
	}
}
