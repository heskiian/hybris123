/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import javax.annotation.Nonnull;


/**
 * Merge only entries that do not have subscription info created
 *
 * @since 1911
 */
public class TmaEntryMergeSubscriptionInfoFilter implements EntryMergeFilter
{
	@Override
	public Boolean apply(@Nonnull AbstractOrderEntryModel candidate, @Nonnull AbstractOrderEntryModel target)
	{
		final TmaCartSubscriptionInfoModel targetSubscriptionInfo = target.getSubscriptionInfo();
		if (targetSubscriptionInfo != null)
		{
			if (targetSubscriptionInfo.getSubscribedProductAction() != null)
			{
				return false;
			}
			SubscriptionTermModel candidateSubscriptionTerm = null;
			if (candidate.getSubscriptionInfo() != null)
			{
				candidateSubscriptionTerm = candidate.getSubscriptionInfo().getSubscriptionTerm();
			}

			final SubscriptionTermModel targetSubscriptionTerm = targetSubscriptionInfo.getSubscriptionTerm();

			return targetSubscriptionTerm == null || targetSubscriptionTerm.equals(candidateSubscriptionTerm);
		}
		return true;
	}
}
