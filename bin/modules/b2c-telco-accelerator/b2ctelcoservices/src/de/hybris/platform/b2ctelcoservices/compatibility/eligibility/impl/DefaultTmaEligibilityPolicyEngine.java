/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEvaluationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityPolicyService;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.comparator.TmaEligibilityContextComparator;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaPolicyContextBuilder;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionBaseService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaEligibilityPolicyEngine}.
 *
 * @since 1810
 */
public class DefaultTmaEligibilityPolicyEngine implements TmaEligibilityPolicyEngine
{
	private TmaEligibilityPolicyService tmaEligibilityService;
	private TmaSubscriptionBaseService tmaSubscriptionBaseService;
	private TmaCompatibilityPolicyEvaluationStrategy policyEvaluationStrategy;
	private TmaEligibleForActionResolver eligibleForActionResolver;
	private TmaPoService poService;
	private SessionService sessionService;
	private UserService userService;
	private final TmaEligibilityContextComparator tmaEligibilityContextComparator;
	protected static final Logger LOG = Logger.getLogger(DefaultTmaEligibilityPolicyEngine.class);

	public DefaultTmaEligibilityPolicyEngine(final TmaEligibilityContextComparator tmaEligibilityContextComparator)
	{
		this.tmaEligibilityContextComparator = tmaEligibilityContextComparator;
	}

	@Override
	public Set<TmaEligibilityContext> createEligibilityContext(final CustomerModel customer)
	{
		final List<TmaCompatibilityPolicyModel> eligibilityPolicies = getTmaEligibilityService().getAvailableEligibilityPolicies();
		Set<TmaEligibilityContext> eligibilityContexts = new TreeSet<>(getTmaEligibilityContextComparator());
		eligibilityContexts.addAll(getEligibilityContextWithoutCpiParams(eligibilityPolicies));
		if (getUserService().isAnonymousUser(customer))
		{
			return eligibilityContexts;
		}

		eligibilityContexts.addAll(getEligibilityContextBasedOnCpi(eligibilityPolicies,
				getTmaSubscriptionBaseService().getSubscriptionBases(customer.getUid())));
		return eligibilityContexts;
	}

	@Override
	public Set<TmaCompatibilityPolicyModel> getApplicableEligibilityPolicies(final CustomerModel customer)
	{
		final List<TmaCompatibilityPolicyModel> eligibilityPolicies = getTmaEligibilityService().getAvailableEligibilityPolicies();
		final Set<TmaCompatibilityPolicyModel> applicableEligiblityPolicies = new HashSet<>();
		final List<TmaSubscriptionBaseModel> tmaSubscriptionBaseModels = getTmaSubscriptionBaseService()
				.getSubscriptionBases(customer.getUid());

		if (CollectionUtils.isEmpty(tmaSubscriptionBaseModels))
		{
			final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder().build();
			applicableEligiblityPolicies
					.addAll(getPolicyEvaluationStrategy().getApplicablePolicies(eligibilityPolicies, Arrays.asList(context)));
			return applicableEligiblityPolicies;
		}

		tmaSubscriptionBaseModels.forEach(subscriptionBase -> {
			final Set<TmaCompatibilityPolicyModel> applicablePolicies = getApplicablePoliciesForSubscriptionBase(eligibilityPolicies,
					subscriptionBase);
			applicableEligiblityPolicies.addAll(applicablePolicies);
		});

		return applicableEligiblityPolicies;
	}

	/**
	 * Returns a set of applicable eligibility context without cpi parameters.
	 *
	 * @param eligibilityPolicies
	 *           List of @{link TmaCompatibilityPolicyModel}
	 * @return set of @{link TmaEligibilityContext}
	 *
	 */
	protected Set<TmaEligibilityContext> getEligibilityContextWithoutCpiParams(
			final List<TmaCompatibilityPolicyModel> eligibilityPolicies)
	{
		final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder().build();
		final Set<TmaCompatibilityPolicyModel> applicablePolicies = getPolicyEvaluationStrategy()
				.getApplicablePolicies(eligibilityPolicies, Arrays.asList(context));
		if (CollectionUtils.isEmpty(applicablePolicies))
		{
			return new HashSet<>();
		}

		return createNewEligibilityContextForUserWithoutSubscriptions(applicablePolicies);
	}

	/**
	 * Returns a set of applicable eligibility context for a given cpi parameters.
	 *
	 * @param eligibilityPolicies
	 *           List of @{link TmaCompatibilityPolicyModel}
	 * @param subscriptions
	 *           List of @{link TmaSubscriptionBaseModel}
	 * @return set of @{link TmaEligibilityContext}
	 *
	 */
	protected Set<TmaEligibilityContext> getEligibilityContextBasedOnCpi(
			final List<TmaCompatibilityPolicyModel> eligibilityPolicies, final List<TmaSubscriptionBaseModel> subscriptions)
	{
		final Set<TmaEligibilityContext> eligibilityContextList = new HashSet<>();
		subscriptions.forEach(subscriptionBase -> {

			final Set<TmaCompatibilityPolicyModel> applicablePolicies = getApplicablePoliciesForSubscriptionBase(eligibilityPolicies,
					subscriptionBase);
			eligibilityContextList.addAll(createNewEligibilityContext(subscriptionBase, applicablePolicies));
		});
		return eligibilityContextList;
	}

	/**
	 * Returns a set of applicable eligibility policies for a given subscription base .
	 *
	 * @param eligibilityPolicies
	 *           List of {@link TmaCompatibilityPolicyModel}
	 * @param subscriptions
	 *           List of {@link TmaSubscriptionBaseModel}
	 * @return set of {@link TmaCompatibilityPolicyModel}
	 *
	 */
	protected Set<TmaCompatibilityPolicyModel> getApplicablePoliciesForSubscriptionBase(
			final List<TmaCompatibilityPolicyModel> eligibilityPolicies, final TmaSubscriptionBaseModel subscriptionBase)
	{
		final List<TmaPolicyContext> requestParamList = new ArrayList<>();
		final Set<TmaCompatibilityPolicyModel> applicablePolicies = new HashSet<>();
		if (subscriptionBase.getBillAgreement() == null || CollectionUtils.isEmpty(subscriptionBase.getSubscribedProducts()))
		{
			return applicablePolicies;
		}

		subscriptionBase.getSubscribedProducts().forEach(subscribedProduct -> {
			final List<TmaProductOfferingModel> productOfferingList = getProductOfferingForSubscribedProduct(subscribedProduct);
			if (!CollectionUtils.isEmpty(productOfferingList))
			{
				final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder()
						.withProductOfferings(productOfferingList).withSubscribed(true).withQuantity(1)
						.withSubscriptionEndDate(subscriptionBase.getBillAgreement().getTerm().getEnd())
						.withSubscribedProduct(subscribedProduct).build();
				requestParamList.add(context);
			}
		});
		applicablePolicies.addAll(getPolicyEvaluationStrategy().getApplicablePolicies(eligibilityPolicies, requestParamList));
		return applicablePolicies;
	}

	private List<TmaProductOfferingModel> getProductOfferingForSubscribedProduct(final TmaSubscribedProductModel subscribedProduct)
	{
		try
		{
			return getPoService().getProductsForCode(subscribedProduct.getProductCode());
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error("The subscribed product does not exists in the current catalog" + e.getMessage(), e);
		}
		return Collections.EMPTY_LIST;
	}

	private List<TmaEligibilityContext> createNewEligibilityContext(final TmaSubscriptionBaseModel subscriptionBase,
			final Set<TmaCompatibilityPolicyModel> applicablePolicies)
	{
		final List<TmaEligibilityContext> contextList = new ArrayList<>();
		if (CollectionUtils.isEmpty(applicablePolicies))
		{
			return contextList;
		}

		contextList.addAll(getEligibleForActionResolver().processEligibilityPolicyActions(applicablePolicies,
				subscriptionBase.getSubscriberIdentity(), subscriptionBase.getBillingSystemId()));
		return contextList;
	}

	private Set<TmaEligibilityContext> createNewEligibilityContextForUserWithoutSubscriptions(
			final Set<TmaCompatibilityPolicyModel> applicablePolicies)
	{
		final Set<TmaEligibilityContext> contextList = new HashSet<>();
		contextList.addAll(getEligibleForActionResolver().processEligibilityPolicyActions(applicablePolicies, StringUtils.EMPTY,
				StringUtils.EMPTY));
		return contextList;
	}

	@Required
	protected TmaEligibilityPolicyService getTmaEligibilityService()
	{
		return tmaEligibilityService;
	}

	public void setTmaEligibilityService(final TmaEligibilityPolicyService tmaEligibilityService)
	{
		this.tmaEligibilityService = tmaEligibilityService;
	}

	@Required
	protected TmaSubscriptionBaseService getTmaSubscriptionBaseService()
	{
		return tmaSubscriptionBaseService;
	}

	public void setTmaSubscriptionBaseService(final TmaSubscriptionBaseService tmaSubscriptionBaseService)
	{
		this.tmaSubscriptionBaseService = tmaSubscriptionBaseService;
	}

	@Required
	protected TmaCompatibilityPolicyEvaluationStrategy getPolicyEvaluationStrategy()
	{
		return policyEvaluationStrategy;
	}

	public void setPolicyEvaluationStrategy(final TmaCompatibilityPolicyEvaluationStrategy policyEvaluationStrategy)
	{
		this.policyEvaluationStrategy = policyEvaluationStrategy;
	}

	@Required
	protected TmaEligibleForActionResolver getEligibleForActionResolver()
	{
		return eligibleForActionResolver;
	}

	public void setEligibleForActionResolver(final TmaEligibleForActionResolver eligibleForActionResolver)
	{
		this.eligibleForActionResolver = eligibleForActionResolver;
	}

	@Required
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	@Required
	protected TmaPoService getPoService()
	{
		return poService;
	}

	public void setPoService(final TmaPoService poService)
	{
		this.poService = poService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected TmaEligibilityContextComparator getTmaEligibilityContextComparator()
	{
		return tmaEligibilityContextComparator;
	}
}
