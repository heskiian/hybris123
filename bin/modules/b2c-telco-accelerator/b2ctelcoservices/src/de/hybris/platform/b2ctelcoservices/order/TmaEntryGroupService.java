/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * Service that exposes methods to deal with {@link EntryGroup} operations.
 *
 * @since 1911
 */
public interface TmaEntryGroupService extends EntryGroupService
{
	/**
	 * Updates an entry group
	 *
	 * @param order
	 * 		order to update
	 * @param entryGroup
	 * 		entry group to be updated
	 */
	void updateEntryGroup(@Nonnull final AbstractOrderModel order, @Nonnull EntryGroup entryGroup);

	/**
	 * Returns the entries from the cart part of the provided entry group.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @param entryGroup
	 * 		The entry group
	 * @return List of {@link AbstractOrderEntryModel}
	 */
	List<AbstractOrderEntryModel> getEntriesFrom(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup);
}
