/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementservices.services;

import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.List;


/**
 * Service responsible for handling {@link BmPartyBillModel}  related operations.
 *
 * @since 2108
 */
public interface BmPartyBillService
{
	/**
	 * Returns a {@link BmPartyBillModel} for the given party bill no.
	 *
	 * @param partyBillNo
	 * 		party bill no of {@link BmPartyBillModel}
	 * @return the {@link BmPartyBillModel} found.
	 * @throws {@link
	 *      ModelNotFoundException}
	 * 		if no party bill is found.
	 */
	BmPartyBillModel getPartyBill(final String partyBillNo);

	/**
	 * Returns a list of {@link BmPartyBillModel}s for the given context.
	 *
	 * @param bmPartyBillContext
	 * 		the party bill context, used for filtering the party bills.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned party bills.
	 * @return the List of {@link BmPartyBillModel}s found for the given context.
	 */
	List<BmPartyBillModel> getPartyBills(final BmPartyBillContext bmPartyBillContext, final Integer offset, final Integer limit);

	/**
	 * Retrieves the total number of party bills found for a given bmPartyBillContext.
	 *
	 * @param bmPartyBillContext
	 * 		the party bill context.
	 * @return the number of party bills found.
	 */
	Integer getNumberOfPartyBillsFor(final BmPartyBillContext bmPartyBillContext);
}
