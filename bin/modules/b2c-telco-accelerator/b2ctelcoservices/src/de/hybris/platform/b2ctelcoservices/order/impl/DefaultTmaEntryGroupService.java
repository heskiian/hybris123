/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.impl.DefaultEntryGroupService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of {@link TmaEntryGroupService}
 *
 * @since 1911
 */
public class DefaultTmaEntryGroupService extends DefaultEntryGroupService implements TmaEntryGroupService
{
	@Override
	public void updateEntryGroup(@Nonnull AbstractOrderModel order, @Nonnull EntryGroup entryGroup)
	{
		final List<EntryGroup> orderGroups = order.getEntryGroups();
		final List<EntryGroup> updatedGroups = orderGroups.stream().filter(group -> !group.getGroupNumber().equals(entryGroup
				.getGroupNumber())).collect(Collectors.toList());
		updatedGroups.add(entryGroup);
		order.setEntryGroups(updatedGroups);
		forceOrderSaving(order);
	}

	@Override
	public List<AbstractOrderEntryModel> getEntriesFrom(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup)
	{
		if (abstractOrder == null || CollectionUtils.isEmpty(abstractOrder.getEntries()) || entryGroup == null)
		{
			return Collections.emptyList();
		}

		return abstractOrder.getEntries().stream()
				.filter(entry -> entry.getEntryGroupNumbers().contains(entryGroup.getGroupNumber())).collect(
						Collectors.toList());
	}
}
