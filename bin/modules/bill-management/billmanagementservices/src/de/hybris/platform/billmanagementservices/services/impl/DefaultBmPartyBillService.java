/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementservices.services.impl;

import de.hybris.platform.billmanagementservices.daos.BmPartyBillDao;
import de.hybris.platform.billmanagementservices.data.BmPartyBillContext;
import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementservices.services.BmPartyBillService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link BmPartyBillService}.
 *
 * @since 2108
 */
public class DefaultBmPartyBillService implements BmPartyBillService
{
	private BmPartyBillDao partyBillDao;

	public DefaultBmPartyBillService(BmPartyBillDao partyBillDao)
	{
		this.partyBillDao = partyBillDao;
	}

	@Override
	public BmPartyBillModel getPartyBill(final String partyBillNo)
	{
		validateParameterNotNull(partyBillNo, "Party Bill no cannot be null");
		final Map<String, String> parameters = new HashMap();
		parameters.put(BmPartyBillModel.BILLNO, partyBillNo);
		return getPartyBillDao().findUnique(parameters);
	}

	@Override
	public List<BmPartyBillModel> getPartyBills(final BmPartyBillContext bmPartyBillContext, final Integer offset,
			final Integer limit)
	{
		return getPartyBillDao().getPartyBills(bmPartyBillContext, offset, limit);
	}

	@Override
	public Integer getNumberOfPartyBillsFor(final BmPartyBillContext bmPartyBillContext)
	{
		return getPartyBillDao().getNumberOfPartyBillsFor(bmPartyBillContext);
	}

	protected BmPartyBillDao getPartyBillDao()
	{
		return partyBillDao;
	}
}
