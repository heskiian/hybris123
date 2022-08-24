/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.commerceservices.order.EntryMergeFilter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Merge only entries that have same process type
 *
 * @since 1911
 */
public class TmaEntryMergeProcessTypeFilter implements EntryMergeFilter
{
	@Override
	public Boolean apply(@Nonnull final AbstractOrderEntryModel candidate, @Nonnull final AbstractOrderEntryModel target)
	{

		final TmaProcessType newProcessType = candidate.getProcessType();
		final TmaProcessType oldProcessType = target.getProcessType();
		if (!ObjectUtils.isEmpty(newProcessType) && !ObjectUtils.isEmpty(oldProcessType))
		{
			return StringUtils.equals(newProcessType.getCode(), oldProcessType.getCode());
		}
		if (ObjectUtils.isEmpty(newProcessType) && ObjectUtils.isEmpty(oldProcessType))
		{
			return true;
		}
		return false;
	}
}
