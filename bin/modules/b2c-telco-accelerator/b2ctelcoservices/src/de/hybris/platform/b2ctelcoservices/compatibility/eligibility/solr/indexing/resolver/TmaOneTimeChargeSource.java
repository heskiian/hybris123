/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.OneTimeChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @since 1810
 */
public class TmaOneTimeChargeSource implements TmaChargeValueSource
{
	@Override
	public Double getPrice(PriceRowModel price, String param)
	{
		if (price instanceof SubscriptionPricePlanModel)
		{
			List<OneTimeChargeEntryModel> otcsWithBillingEvent = ((SubscriptionPricePlanModel) price).getOneTimeChargeEntries()
					.stream()
					.filter(otc -> (otc.getBillingEvent() == null || otc.getBillingEvent().getCode().equals(param)))
					.collect(Collectors.toList());
			return otcsWithBillingEvent.isEmpty() ? null : otcsWithBillingEvent.get(0).getPrice();
		}
		else
		{
			return price.getPrice();
		}
	}
}
