/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;


/**
 * @since 1810
 */
public class TmaRecurrentChargeSource implements TmaChargeValueSource
{
	@Override
	public Double getPrice(PriceRowModel price, String param)
	{
		if (price instanceof SubscriptionPricePlanModel)
		{
			Collection<RecurringChargeEntryModel> rces = ((SubscriptionPricePlanModel) price).getRecurringChargeEntries();
			return !rces.isEmpty() ?
					((RecurringChargeEntryModel) CollectionUtils.get(rces, getIndex(rces, param))).getPrice() :
					null;
		}
		else
		{
			return null;
		}
	}

	protected int getIndex(Collection<RecurringChargeEntryModel> rces, String param)
	{
		return param.equals("last") ? rces.size() - 1 : 0;
	}
}
