/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.comparator;

import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Comparator for determining an order of prices comparing both {@link PriceRowModel}s and {@link SubscriptionPricePlanModel}.
 *
 * @since 6.7
 * @deprecated since 2007
 */
@Deprecated(since = "2007")
public class PriceRowModelComparator extends AbstractComparator<PriceRowModel>
{

	private SubscriptionPricePlanModelComparator subscriptionPricePlanComparator;

	@Override
	protected int compareInstances(final PriceRowModel instance1, final PriceRowModel instance2)
	{
		if (SubscriptionPricePlanModel._TYPECODE.equals(instance1.getItemtype()) && SubscriptionPricePlanModel._TYPECODE
				.equals(instance2.getItemtype()))
		{
			return getSubscriptionPricePlanComparator()
					.compare((SubscriptionPricePlanModel) instance1, (SubscriptionPricePlanModel) instance2);
		}

		if (SubscriptionPricePlanModel._TYPECODE.equals(instance1.getItemtype()))
		{
			return compareSubscriptionPricePlanToPriceRow((SubscriptionPricePlanModel) instance1, instance2);
		}

		if (SubscriptionPricePlanModel._TYPECODE.equals(instance2.getItemtype()))
		{
			return comparePriceRowToSubscriptionPricePlan(instance1, (SubscriptionPricePlanModel) instance2);
		}

		return instance1.getPrice().compareTo(instance2.getPrice());
	}

	protected int compareSubscriptionPricePlanToPriceRow(final SubscriptionPricePlanModel instance1, final PriceRowModel instance2)
	{
		if (instance1.getPrice() != null)
		{
			return instance1.getPrice().compareTo(instance2.getPrice());
		}

		if (CollectionUtils.isNotEmpty(instance1.getOneTimeChargeEntries()))
		{
			return instance1.getOneTimeChargeEntries().iterator().next().getPrice().compareTo(instance2.getPrice());
		}
		throw new IllegalArgumentException("Price Plan with code " + instance1.getName()
				+ " should either have recurring charges, a value or one time charges !");
	}

	protected int comparePriceRowToSubscriptionPricePlan(final PriceRowModel instance1, final SubscriptionPricePlanModel instance2)
	{
		if (instance2.getPrice() != null)
		{
			return instance1.getPrice().compareTo(instance2.getPrice());
		}

		if (CollectionUtils.isNotEmpty(instance2.getOneTimeChargeEntries()))
		{
			return instance1.getPrice().compareTo(instance2.getOneTimeChargeEntries().iterator().next().getPrice());
		}
		throw new IllegalArgumentException("Price Plan with code " + instance2.getName()
				+ " should either have recurring charges, a value or one time charges !");
	}

	protected SubscriptionPricePlanModelComparator getSubscriptionPricePlanComparator()
	{
		return subscriptionPricePlanComparator;
	}

	@Required
	public void setSubscriptionPricePlanComparator(final SubscriptionPricePlanModelComparator subscriptionPricePlanComparator)
	{
		this.subscriptionPricePlanComparator = subscriptionPricePlanComparator;
	}
}
