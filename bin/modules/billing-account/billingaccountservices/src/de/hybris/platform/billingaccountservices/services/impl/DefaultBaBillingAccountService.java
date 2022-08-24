/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billingaccountservices.services.impl;

import de.hybris.platform.billingaccountservices.daos.BaBillingAccountDao;
import de.hybris.platform.billingaccountservices.daos.BaGenericSearchDao;
import de.hybris.platform.billingaccountservices.data.BaBillingAccountContext;
import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.services.BaBillingAccountService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of {@link BaBillingAccountService}.
 *
 * @since 2105
 */
public class DefaultBaBillingAccountService implements BaBillingAccountService
{

	private BaGenericSearchDao billingAccountSearchDao;
	private BaBillingAccountDao billingAccountDao;
	private ModelService modelService;

	public DefaultBaBillingAccountService(final BaGenericSearchDao billingAccountSearchDao,
			final BaBillingAccountDao billingAccountDao, final ModelService modelService)
	{
		this.billingAccountSearchDao = billingAccountSearchDao;
		this.billingAccountDao = billingAccountDao;
		this.modelService = modelService;
	}

	@Override
	public BaAccountModel getBillingAccount(final String billingAccountId)
	{
		validateParameterNotNull(billingAccountId, "Billing Account id can not be null");
		final Map<String, String> parameters = new HashMap();
		parameters.put(BaAccountModel.ID, billingAccountId);
		return (BaAccountModel) getGenericSearchDao().findUnique(parameters);
	}

	@Override
	public List<BaAccountModel> getBillingAccounts(final BaBillingAccountContext billingAccountContext, final Integer offset,
			final Integer limit)
	{
		return getBillingAccountDao().getBillingAccounts(billingAccountContext, offset, limit);
	}

	@Override
	public Integer getNumberOfBillingAccountsFor(final BaBillingAccountContext billingAccountContext)
	{
		return getBillingAccountDao().getNumberOfBillingAccountsFor(billingAccountContext);
	}

	@Override
	public void saveBillingAccount(final BaAccountModel billingAccountModel)
	{
		getModelService().save(billingAccountModel);
		getModelService().refresh(billingAccountModel);
	}

	@Override
	public BaAccountModel createBillingAccount(final Class billingAccountClass)
	{
		return getModelService().create(billingAccountClass);
	}

	@Override
	public void removeBillingAccount(final BaAccountModel billingAccount)
	{
		getModelService().remove(billingAccount);
	}

	protected BaGenericSearchDao getGenericSearchDao()
	{
		return billingAccountSearchDao;
	}

	protected BaBillingAccountDao getBillingAccountDao()
	{
		return billingAccountDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
