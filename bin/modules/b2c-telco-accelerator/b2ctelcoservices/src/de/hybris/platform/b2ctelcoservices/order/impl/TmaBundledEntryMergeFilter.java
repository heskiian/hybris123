/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import javax.annotation.Nonnull;


/**
 * Merge only entries which contain Simple Product Offerings and have the same master entry.
 *
 * @since 2102
 */
public class TmaBundledEntryMergeFilter implements EntryMergeFilter
{
	@Override
	public Boolean apply(@Nonnull AbstractOrderEntryModel candidate, @Nonnull AbstractOrderEntryModel target)
	{
		if (candidate.getProduct() instanceof TmaBundledProductOfferingModel ||
				target.getProduct() instanceof TmaBundledProductOfferingModel)
		{
			return false;
		}

		if (candidate.getMasterEntry() == null && target.getMasterEntry() == null)
		{
			return true;
		}

		if ((candidate.getMasterEntry() != null && target.getMasterEntry() == null) || (candidate.getMasterEntry() == null
				&& target.getMasterEntry() != null))
		{
			return false;
		}

		return candidate.getMasterEntry().equals(target.getMasterEntry());
	}
}
