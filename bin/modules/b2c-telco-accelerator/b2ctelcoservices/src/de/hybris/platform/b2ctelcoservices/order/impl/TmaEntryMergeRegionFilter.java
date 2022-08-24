/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Merge only entries that contain the region equals to target entry region.
 *
 * @since 2003
 */
public class TmaEntryMergeRegionFilter implements EntryMergeFilter
{

	@Override
	public Boolean apply(@Nonnull final AbstractOrderEntryModel candidate, @Nonnull final AbstractOrderEntryModel target)
	{
		final RegionModel newcartentryRegion = candidate.getRegion();
		final RegionModel oldcartentryRegion = target.getRegion();
		if (!ObjectUtils.isEmpty(newcartentryRegion) && !ObjectUtils.isEmpty(oldcartentryRegion))
		{
			return StringUtils.equalsIgnoreCase(newcartentryRegion.getIsocode(), oldcartentryRegion.getIsocode());
		}
		return ObjectUtils.isEmpty(newcartentryRegion) && ObjectUtils.isEmpty(oldcartentryRegion);
	}

}
