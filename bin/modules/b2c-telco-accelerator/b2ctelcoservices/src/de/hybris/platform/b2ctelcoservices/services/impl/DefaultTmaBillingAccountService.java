/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaBillingAccountDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingAccountService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.testframework.Transactional;

import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaBillingAccountService}.
 *
 * @since 6.6
 */
public class DefaultTmaBillingAccountService implements TmaBillingAccountService
{
	private TmaBillingAccountDao tmaBillingAccountDao;
	private ModelService modelService;
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	private PersistentKeyGenerator tmaBillingAccountIdGenerator;
	private String defaultBillingSystemId;

	@Override
	public TmaBillingAccountModel findBillingAccount(final String billingSystemId, final String billingAccountId)
	{
		return getTmaBillingAccountDao().findBillingAccount(billingSystemId, billingAccountId);
	}

	@Override
	public TmaBillingAccountModel createBillingAccount(final String billingSystemId, final String billingAccountId)
	{
		final TmaBillingAccountModel billingAccountModel = createBillingAccountModel(billingSystemId, billingAccountId);
		getModelService().save(billingAccountModel);
		return billingAccountModel;
	}

	@Override
	public TmaBillingAccountModel createBillingAccountWithParentAccount(final String billingSystemId,
			final String billingAccountId, final String parentBillingAccountId)
	{
		final TmaBillingAccountModel billingAccountModel = createBillingAccountModel(billingSystemId, billingAccountId);
		final TmaBillingAccountModel parentBillingAccountModel = findBillingAccount(billingSystemId, parentBillingAccountId);
		billingAccountModel.setParent(parentBillingAccountModel);
		getModelService().save(billingAccountModel);
		return billingAccountModel;
	}

	@Transactional
	@Override
	public void deleteBillingAccount(final String billingSystemId, final String billingAccountId)
	{
		final TmaBillingAccountModel billingAccountModel = getTmaBillingAccountDao().findBillingAccount(billingSystemId,
				billingAccountId);
		removeAssociatedSubscriptionBases(billingAccountModel.getSubscriptionBases());
		getModelService().remove(billingAccountModel);
	}

	@Override
	public TmaBillingAccountModel generateBillingAccount()
	{
		return createBillingAccount(getDefaultBillingSystemId(), getTmaBillingAccountIdGenerator().generate().toString());
	}

	private String getDefaultBillingSystemId()
	{
		return defaultBillingSystemId;
	}

	@Required
	public void setDefaultBillingSystemId(final String defaultBillingSystemId)
	{
		this.defaultBillingSystemId = defaultBillingSystemId;
	}

	protected PersistentKeyGenerator getTmaBillingAccountIdGenerator()
	{
		return tmaBillingAccountIdGenerator;
	}

	@Required
	public void setTmaBillingAccountIdGenerator(final PersistentKeyGenerator tmaBillingAccountIdGenerator)
	{
		this.tmaBillingAccountIdGenerator = tmaBillingAccountIdGenerator;
	}

	protected TmaBillingAccountDao getTmaBillingAccountDao()
	{
		return tmaBillingAccountDao;
	}

	@Required
	public void setTmaBillingAccountDao(final TmaBillingAccountDao tmaBillingAccountDao)
	{
		this.tmaBillingAccountDao = tmaBillingAccountDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
	}

	@Required
	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

	private void removeAssociatedSubscriptionBases(final Set<TmaSubscriptionBaseModel> subscriptionBases)
	{
		for (final TmaSubscriptionBaseModel subscriptionBaseModel : subscriptionBases)
		{
			getTmaSubscriptionBaseService().deleteSubscriptionBase(subscriptionBaseModel.getSubscriberIdentity(),
					subscriptionBaseModel.getBillingSystemId());
		}
	}

	private TmaBillingAccountModel createBillingAccountModel(final String billingSystemId, final String billingAccountId)
	{
		final TmaBillingAccountModel billingAccountModel = getModelService().create(TmaBillingAccountModel.class);
		billingAccountModel.setBillingAccountId(billingAccountId);
		billingAccountModel.setBillingSystemId(billingSystemId);
		return billingAccountModel;
	}
}
