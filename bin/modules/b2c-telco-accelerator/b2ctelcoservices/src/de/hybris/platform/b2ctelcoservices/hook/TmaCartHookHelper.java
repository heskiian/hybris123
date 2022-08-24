/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.hook;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;


/**
 * Helper methods for operating with {@link de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel} in cart.
 *
 * @since 1810
 */
public interface TmaCartHookHelper
{
	/**
	 * Sets all cart entries belonging to the same entry group/BPO as "not calculated" and then recalculates all of them.
	 *
	 * @param cart
	 * 		the cart to recalculate entries
	 * @param entryGroup
	 * 		the entry group for which the cart entries are recalculated
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	void invalidateBpoEntries(final CartModel cart, final EntryGroup entryGroup);

	/**
	 * Sets all cart entries belonging to the same BPO as "not calculated" and then recalculates all of them.
	 *
	 * @param rootEntry
	 * 		the root entry which will be recalculated along with all its children
	 */
	void recalculateEntries(final AbstractOrderEntryModel rootEntry);
}
