/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.comparator;

import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.subscriptionservices.model.ChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Comparator for determining an order of prices. The priority is as follows:
 * - first recurring charge
 * - price value
 * - first one time charge
 *
 * @since 6.7
 * @deprecated since 2007
 */
@Deprecated(since = "2007")
public class SubscriptionPricePlanModelComparator extends AbstractComparator<SubscriptionPricePlanModel>
{
	@Override
	protected int compareInstances(final SubscriptionPricePlanModel instance1, final SubscriptionPricePlanModel instance2)
	{
		if (CollectionUtils.isNotEmpty(instance1.getRecurringChargeEntries()) || CollectionUtils
				.isNotEmpty(instance2.getRecurringChargeEntries()))
		{
			return compareCharges(instance1.getRecurringChargeEntries(), instance2.getRecurringChargeEntries());
		}

		return getStandAlonePriceFromSppData(instance1).compareTo(getStandAlonePriceFromSppData(instance2));
	}

	protected Double getStandAlonePriceFromSppData(final SubscriptionPricePlanModel priceData)
	{
		if (priceData.getPrice() != null)
		{
			return priceData.getPrice();
		}
		if (CollectionUtils.isNotEmpty(priceData.getOneTimeChargeEntries()))
		{
			return priceData.getOneTimeChargeEntries().iterator().next().getPrice();
		}

		throw new IllegalArgumentException("Price Plan with code " + priceData.getName()
				+ " should either have recurring charges, a value or one time charges !");
	}

	protected <T extends ChargeEntryModel> int compareCharges(final Collection<T> charges1, final Collection<T> charges2)
	{
		if (CollectionUtils.isEmpty(charges1))
		{
			return BEFORE;
		}

		return CollectionUtils.isEmpty(charges2) ? AFTER :
				charges1.iterator().next().getPrice().compareTo(charges2.iterator().next().getPrice());
	}
}
