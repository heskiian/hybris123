/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaBillingAccountDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of the {@link TmaBillingAccountDao}.
 *
 * @since 6.6
 */
public class DefaultTmaBillingAccountDao extends DefaultGenericDao<TmaBillingAccountModel> implements TmaBillingAccountDao
{
	public DefaultTmaBillingAccountDao()
	{
		super(TmaBillingAccountModel._TYPECODE);
	}

	@Override
	public TmaBillingAccountModel findBillingAccount(final String billingSystemId, final String billingAccountId)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(TmaBillingAccountModel.BILLINGSYSTEMID, billingSystemId);
		ServicesUtil.validateParameterNotNullStandardMessage(TmaBillingAccountModel.BILLINGACCOUNTID, billingAccountId);
		final Map<String, String> parameters = new HashMap<>(2);
		parameters.put(TmaBillingAccountModel.BILLINGACCOUNTID, billingAccountId);
		parameters.put(TmaBillingAccountModel.BILLINGSYSTEMID, billingSystemId);
		final List<TmaBillingAccountModel> billingAccounts = find(parameters);
		if (billingAccounts.isEmpty())
		{
			throw new ModelNotFoundException("No billing account with id: " + billingAccountId);
		}
		if (billingAccounts.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + billingAccounts.size() + " models for " +
					TmaBillingAccountModel.BILLINGSYSTEMID + " '" + billingSystemId + "' and "
					+ TmaBillingAccountModel.BILLINGACCOUNTID +
					" '" + billingAccountId + "'.");
		}
		return billingAccounts.get(0);
	}
}
