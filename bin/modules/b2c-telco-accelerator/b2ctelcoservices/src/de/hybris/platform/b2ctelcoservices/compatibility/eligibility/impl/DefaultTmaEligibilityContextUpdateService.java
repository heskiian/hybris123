/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.TmaEligibilityContextDao;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaEligibilityContextService}
 *
 * @since 1907
 */
public class DefaultTmaEligibilityContextUpdateService implements TmaEligibilityContextService
{
	private static final String SESSION_ATTR_SUBS_BASE = "subscriptionInfo";
	private static final String APPLY_ELIGIBILITY_FLAG = "applyEligibility";
	private static final String ANONYMOUS = "anonymous";
	private UserService userService;
	private TmaEligibilityContextDao tmaEligibilityContextDao;
	private TmaEligibilityPolicyEngine tmaEligibilityPolicyEngine;
	private ModelService modelService;
	private SessionService sessionService;

	@Override
	public void updateEligibilityContexts(final boolean forceRefresh)
	{
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();

		updateEligibilityContextsByCustomer(forceRefresh, currentUser);
	}

	@Override
	public void updateEligibilityContextsByCustomer(boolean forceRefresh, final CustomerModel customer)
	{
		if (customer.getUid().equalsIgnoreCase(ANONYMOUS))
		{
			forceRefresh = true;
		}

		if (!forceRefresh && CollectionUtils.isNotEmpty(extractEligibilityContextByCustomer(customer.getUid())))
		{
			return;
		}

		if (forceRefresh)
		{
			final Set<TmaEligibilityContextModel> oldEligibility = extractEligibilityContextByCustomer(customer.getUid());
			getModelService().removeAll(oldEligibility);
		}

		final Set<TmaEligibilityContext> eligibilityContexts = getTmaEligibilityPolicyEngine().createEligibilityContext(customer);

		if (eligibilityContexts.isEmpty())
		{
			return;
		}

		updateApplyEligibilityFlag(true);
		eligibilityContexts.forEach(eligibilityContext ->
		{
			final TmaEligibilityContextModel tmaEligibilityContextModel = getModelService().create(TmaEligibilityContextModel.class);

			tmaEligibilityContextModel.setBillingSystemId(eligibilityContext.getBillingSystemId());
			tmaEligibilityContextModel.setProcessesCodes(eligibilityContext.getProcessesCodes());
			tmaEligibilityContextModel.setRequiredPoCodes(eligibilityContext.getRequiredPoCodes());
			tmaEligibilityContextModel.setTermCodes(eligibilityContext.getTermCodes());
			tmaEligibilityContextModel.setCustomer(customer);
			tmaEligibilityContextModel.setSubscriberId(eligibilityContext.getSubscriptionBaseId());
			tmaEligibilityContextModel.setBaseSiteCodes(eligibilityContext.getBaseSiteCodes());

			getModelService().save(tmaEligibilityContextModel);
		});
	}

	@Override
	public boolean shouldApplyEligibility()
	{
		return getSessionService().getAttribute(APPLY_ELIGIBILITY_FLAG) != null
				? getSessionService().getAttribute(APPLY_ELIGIBILITY_FLAG)
				: Boolean.FALSE;
	}

	@Override
	public void updateApplyEligibilityFlag(final boolean applyEligibilityFlag)
	{
		getSessionService().setAttribute(APPLY_ELIGIBILITY_FLAG, applyEligibilityFlag);
	}

	@Override
	public Set<TmaEligibilityContextModel> extractEligibilityContext()
	{
		final CustomerModel currentUser = (CustomerModel) getUserService().getCurrentUser();

		return extractEligibilityContextByCustomer(currentUser.getUid());
	}

	@Override
	public Set<TmaEligibilityContextModel> extractEligibilityContextByCustomer(final String customerUid)
	{
		final TmaSubscriptionBaseData subsBase = getSessionService().getAttribute(SESSION_ATTR_SUBS_BASE);

		return getTmaEligibilityContextDao().getEligibilityContext(customerUid,
				subsBase != null ? subsBase.getSubscriberIdentity() : null);

	}

	public UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public TmaEligibilityContextDao getTmaEligibilityContextDao()
	{
		return tmaEligibilityContextDao;
	}

	@Required
	public void setTmaEligibilityContextDao(final TmaEligibilityContextDao tmaEligibilityContextDao)
	{
		this.tmaEligibilityContextDao = tmaEligibilityContextDao;
	}

	public TmaEligibilityPolicyEngine getTmaEligibilityPolicyEngine()
	{
		return tmaEligibilityPolicyEngine;
	}

	@Required
	public void setTmaEligibilityPolicyEngine(final TmaEligibilityPolicyEngine tmaEligibilityPolicyEngine)
	{
		this.tmaEligibilityPolicyEngine = tmaEligibilityPolicyEngine;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

}
