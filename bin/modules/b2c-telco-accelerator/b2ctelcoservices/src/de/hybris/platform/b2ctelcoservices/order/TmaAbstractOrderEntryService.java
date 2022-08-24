/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.AbstractOrderEntryService;

import java.util.List;


/**
 * Service business logic around  {@link AbstractOrderEntryModel}.
 *
 * @since 2011
 */
public interface TmaAbstractOrderEntryService extends AbstractOrderEntryService
{
	/**
	 * Returns the quantity of the product offering found in the list of provided entries.
	 *
	 * @param entries
	 * 		The entries from which the quantity will be retrieved
	 * @param productOffering
	 * 		The product offering
	 * @return The quantity of the product offering
	 */
	Long getPoQuantity(final List<AbstractOrderEntryModel> entries, final TmaProductOfferingModel productOffering);

	/**
	 * Returns all the SPO child entries for the provided entry in a list.
	 *
	 * @param orderEntry
	 * 		The entry
	 * @return List of {@link AbstractOrderEntryModel}
	 */
	List<AbstractOrderEntryModel> getSpoChildEntries(final AbstractOrderEntryModel orderEntry);

	/**
	 * Returns the root entry for the provided entry.
	 *
	 * @param entry
	 * 		The entry for which the root master entry will be returned
	 * @return The root master entry
	 */
	AbstractOrderEntryModel getRootEntry(final AbstractOrderEntryModel entry);

	/**
	 * Returns the abstract order entry with the id provided and part of he abstract order given by its id.
	 *
	 * @param abstractOrder
	 * 		the abstract order model where the entry is searched for
	 * @param entryNumber
	 * 		the identifier of the abstract order entry to be searched for
	 * @return the corresponding abstract order entry
	 */
	AbstractOrderEntryModel getEntryBy(final AbstractOrderModel abstractOrder, final int entryNumber);

	/**
	 * Returns all the child entries for the provided entry in a list.
	 *
	 * @param orderEntry
	 * 		The entry
	 * @return List of {@link AbstractOrderEntryModel}
	 */
	List<AbstractOrderEntryModel> getAllChildEntries(final AbstractOrderEntryModel orderEntry);

	/**
	 * Returns all the parent entries containing Bundled Product Offerings for the provided entry in a list.
	 *
	 * @param orderEntry
	 * 		The entry
	 * @return List of {@link AbstractOrderEntryModel}
	 */
	List<AbstractOrderEntryModel> getBpoParentEntries(final AbstractOrderEntryModel orderEntry);
}
