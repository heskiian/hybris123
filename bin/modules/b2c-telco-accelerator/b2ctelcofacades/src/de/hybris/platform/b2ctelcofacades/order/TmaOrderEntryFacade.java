/*
 * Copyright (c)  2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;


/**
 * Facade handling cart operations for {@link OrderEntryData}.
 *
 * @since 2102
 */
public interface TmaOrderEntryFacade
{
	/**
	 * Returns the entry matching to the given entry number.
	 *
	 * @param entries
	 * 		The list of entries
	 * @param entryNumber
	 * 		The entry number
	 * @return The entry matching to the provided entry number
	 */
	OrderEntryData getEntry(final List<OrderEntryData> entries, final long entryNumber);

	/**
	 * Returns the entry containing the the given product code.
	 *
	 * @param entry
	 * 		The rootEntry
	 * @param productCode
	 * 		The identifier of the product offering
	 * @return The entry containing the matching product offering
	 */
	OrderEntryData getEntry(final OrderEntryData entry, final String productCode);

	/**
	 * Returns the entry matching to the given product code and store.
	 *
	 * @param entries
	 * 		The list of entries
	 * @param productCode
	 * 		The identifier of the product
	 * @param pickupStore
	 * 		The identifier of the pick-up store
	 * @return The entry matching to the provided entry number
	 */
	OrderEntryData getEntry(final List<OrderEntryData> entries, final String productCode, final String pickupStore);

	/**
	 * Returns all the child entries corresponding to SPOs.
	 *
	 * @param rootEntry
	 * 		The root entry
	 * @return List of {@link OrderEntryData} corresponding to SPOs
	 */
	List<OrderEntryData> getAllSpoEntries(final OrderEntryData rootEntry);

	/**
	 * Returns all abstract order entries corresponding to SPOs.
	 *
	 * @param abstractOrderData
	 * 		The sbatract order data object
	 * @return List of {@link OrderEntryData} corresponding to SPOs
	 */
	List<OrderEntryData> getAllSpoEntries(final AbstractOrderData abstractOrderData);

	/**
	 * Returns all the child entries for the provided rootEntry.
	 *
	 * @param rootEntry
	 * 		The root entry
	 * @return List of {@link OrderEntryData} containing the child entries
	 */
	List<OrderEntryData> getAllChildEntries(final OrderEntryData rootEntry);

}
