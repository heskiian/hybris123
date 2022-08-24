/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;

import java.util.List;


/**
 * Strategy responsible for handling validation messages
 *
 * @since 1911
 */
public interface TmaValidationMessagesStrategy
{

	/**
	 * Sets validation messages on cart.
	 *
	 * @param cartModel
	 * 		the cart to be updated
	 * @param messages
	 * 		list of messages
	 */
	void setValidationMessagesOn(CartModel cartModel, List<String> messages);

	/**
	 * Returns a boolean that states whether the validation messages should be updated or not.
	 *
	 * @param entryGroup
	 * 		the entry group to be checked
	 * @param errorMessages
	 * 		list of messages
	 * @return a boolean specifying if it should be updated or not
	 * @deprecated since 2102 - use {@link #shouldUpdateValidationMessages(CartEntryModel, List)} instead
	 */
	@Deprecated(since = "2102")
	boolean shouldUpdateValidationMessages(final EntryGroup entryGroup, final List<String> errorMessages);

	/**
	 * Returns a boolean that states whether the validation messages should be updated or not.
	 *
	 * @param cartEntry
	 * 		the cart entry to be checked
	 * @param errorMessages
	 * 		list of messages
	 * @return a boolean specifying if it should be updated or not
	 */
	boolean shouldUpdateValidationMessages(final CartEntryModel cartEntry, final List<String> errorMessages);

	/**
	 * Sets validation messages on entry group.
	 *
	 * @param entryGroup
	 * 		the entry group to be updated
	 * @param messages
	 * 		list of messages
	 * @deprecated since 2102 - use {@link #setValidationMessagesOn(CartEntryModel, List)} instead
	 */
	@Deprecated(since = "2102")
	void setValidationMessagesOn(EntryGroup entryGroup, List<String> messages);

	/**
	 * Sets validation messages on cart entry.
	 *
	 * @param cartEntry
	 * 		the cart entry to be updated
	 * @param messages
	 * 		list of messages
	 */
	void setValidationMessagesOn(CartEntryModel cartEntry, final List<String> messages);

	/**
	 * Removes validation messages from the cart.
	 *
	 * @param cartModel
	 * 		The cart
	 */
	void cleanupValidationMessagesOn(final CartModel cartModel);

	/**
	 * Removes the validation messages form the entry group.
	 *
	 * @param abstractOrder
	 * 		The cart or order
	 * @param entryGroup
	 * 		The entry group
	 * @deprecated since 2102 - use {@link #cleanupValidationMessagesOn(CartEntryModel)} (AbstractOrderEntryModel)} instead
	 */
	@Deprecated(since = "2102")
	void cleanupValidationMessagesOn(final AbstractOrderModel abstractOrder, final EntryGroup entryGroup);

	/**
	 * Removes the validation messages form the cart entry.
	 *
	 * @param cartEntry
	 * 		The cart entry
	 */
	void cleanupValidationMessagesOn(final CartEntryModel cartEntry);
}
