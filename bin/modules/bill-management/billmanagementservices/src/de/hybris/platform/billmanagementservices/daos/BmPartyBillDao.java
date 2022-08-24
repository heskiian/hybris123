/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementservices.daos;

import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.List;
import java.util.Map;


/**
 * Data access object for {@link BmPartyBillModel}s.
 *
 * @since 2108
 */
public interface BmPartyBillDao extends GenericDao<BmPartyBillModel>
{
	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	BmPartyBillModel findUnique(Map<String, ? extends Object> params);

	/**
	 * Returns a list of {@link BmPartyBillModel}s for the given context.
	 *
	 * @param bmPartyBillContext
	 * 		the party bill context
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned party bills.
	 * @return the List of {@link BmPartyBillModel}s found for the given context.
	 */
	List<BmPartyBillModel> getPartyBills(final BmPartyBillContext bmPartyBillContext, final Integer offset, final Integer limit);

	/**
	 * Retrieves the total number of party bills found for a given partyBillContext.
	 *
	 * @param bmPartyBillContext
	 * 		the party bill context.
	 * @return the number of party bills found.
	 */
	Integer getNumberOfPartyBillsFor(final BmPartyBillContext bmPartyBillContext);
}
