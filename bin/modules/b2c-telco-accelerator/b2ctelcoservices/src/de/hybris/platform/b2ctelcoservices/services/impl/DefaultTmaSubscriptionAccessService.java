/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionAccessDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaAccessType;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.PrincipalService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionAccessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscriptionAccessService}.
 *
 * @since 6.6
 */
public class DefaultTmaSubscriptionAccessService implements TmaSubscriptionAccessService
{
	private ModelService modelService;
	private TmaSubscriptionAccessDao tmaSubscriptionAccessDao;
	private PrincipalService principalService;

	@Override
	public TmaSubscriptionAccessModel createSubscriptionAccessModel(final String principalUid, final String billingSystemId,
			final TmaSubscriptionBaseModel subscriptionBase, final TmaAccessType accessType)
	{
		final TmaSubscriptionAccessModel model = getModelService().create(TmaSubscriptionAccessModel.class);
		model.setPrincipal(getPrincipalService().findPrincipalByUid(principalUid));
		model.setSubscriptionBase(subscriptionBase);
		model.setAccessType(accessType);
		getModelService().save(model);
		return model;
	}

	@Override
	public List<TmaSubscriptionAccessModel> getSubscriptionAccessesByPrincipalUid(String principalUid)
	{
		return getTmaSubscriptionAccessDao().findSubscriptionAccessByPrincipalUid(principalUid);
	}

	@Override
	public TmaSubscriptionAccessModel getSubscriptionAccessByPrincipalAndSubscriptionBase(final String principalUid,
			final String billingSystemId, final String subscriberIdentity)
	{
		return getTmaSubscriptionAccessDao().findSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId,
				subscriberIdentity);
	}

	@Override
	public List<TmaSubscriptionAccessModel> getSubscriptionAccessesBySubscriberIdentity(final String billingSystemId,
			final String subscriberIdentity)
	{
		return getTmaSubscriptionAccessDao().findSubscriptionAccessesBySubscriberIdentity(subscriberIdentity, billingSystemId);
	}

	@Override
	public List<TmaSubscriptionAccessModel> getSubscriptionAccessesByType(final String principalUid,
			final TmaAccessType accessType)
	{
		return getTmaSubscriptionAccessDao().findSubscriptionAccessesByType(principalUid, accessType);
	}

	@Override
	public TmaSubscriptionAccessModel updateSubscriptionAccess(final String principalUid, final String billingSystemId,
			final String subscriberIdentity, final TmaAccessType accessType)
	{
		final TmaSubscriptionAccessModel model = getSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId,
				subscriberIdentity);
		model.setAccessType(accessType);
		getModelService().save(model);
		return model;
	}

	@Override
	public void deleteSubscriptionAccess(final String principalUid, final String billingSystemId, final String subscriberIdentity)
	{
		getModelService().remove(
				getSubscriptionAccessByPrincipalAndSubscriptionBase(principalUid, billingSystemId, subscriberIdentity));
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

	protected TmaSubscriptionAccessDao getTmaSubscriptionAccessDao()
	{
		return tmaSubscriptionAccessDao;
	}

	@Required
	public void setTmaSubscriptionAccessDao(final TmaSubscriptionAccessDao tmaSubscriptionAccessDao)
	{
		this.tmaSubscriptionAccessDao = tmaSubscriptionAccessDao;
	}

	protected PrincipalService getPrincipalService()
	{
		return principalService;
	}

	@Required
	public void setPrincipalService(final PrincipalService principalService)
	{
		this.principalService = principalService;
	}

}
