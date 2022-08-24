/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.model;

import de.hybris.platform.subscriptionservices.model.*;


/**
 * Attribute handler for dynamic attribute {@link ChargeEntryModel#getBillingTime()}
 *
 * @since 1907
 */
public class TmaBillingTimeChargeEntryAttribute extends TmaBillingTimeAttributeHandler<ChargeEntryModel>
{

	@Override
	protected SubscriptionPricePlanModel getSubscriptionPricePlan(final ChargeEntryModel model)
	{
		if (model instanceof OneTimeChargeEntryModel)
		{
			return ((OneTimeChargeEntryModel) model).getSubscriptionPricePlanOneTime();
		}

		if (model instanceof RecurringChargeEntryModel)
		{
			return ((RecurringChargeEntryModel) model).getSubscriptionPricePlanRecurring();
		}

		if (model instanceof UsageChargeEntryModel)
		{
			final UsageChargeModel usageChargeModel = ((UsageChargeEntryModel) model).getUsageCharge();
			if (usageChargeModel != null)
			{
				return usageChargeModel.getSubscriptionPricePlanUsage();
			}
		}

		return null;
	}

}
