/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaSubscriptionTermDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaSubscriptionTermService}.
 *
 * @since 6.7
 */
public class DefaultTmaSubscriptionTermService implements TmaSubscriptionTermService
{
	private static final String DEFAULT_SUBSCRIPTION_TERM_ID_KEY = "defaultSubscriptionTermId";

	private TmaSubscriptionTermDao tmaSubscriptionTermDao;

	private ConfigurationService configurationService;

	@Override
	public List<SubscriptionTermModel> getAllSubscriptionTerms()
	{
		return getTmaSubscriptionTermDao().findAllSubscriptionTerms();
	}

	@Override
	public Set<SubscriptionTermModel> getApplicableSubscriptionTerms(final TmaProductOfferingModel spo,
			final TmaBundledProductOfferingModel bpo, final TmaProcessType processType)
	{
		final List<SubscriptionTermModel> allSubscriptionTerms = getAllSubscriptionTerms();
		final Set<SubscriptionTermModel> applicableSubscriptionTerms = getSubscriptionTermsFromSpo(spo, processType,
				allSubscriptionTerms);
		if (bpo != null && applicableSubscriptionTerms.size() < allSubscriptionTerms.size())
		{
			applicableSubscriptionTerms.addAll(getSubscriptionTermsFromBpo(bpo, spo, processType, allSubscriptionTerms));
		}
		return applicableSubscriptionTerms;
	}

	@Override
	public SubscriptionTermModel getSubscriptionTerm(final String id)
	{
		final List<SubscriptionTermModel> result = getTmaSubscriptionTermDao()
				.find(Collections.singletonMap(SubscriptionTermModel.ID, id));
		if (result == null || result.isEmpty())
		{
			return null;
		}
		return result.get(0);
	}

	@Override
	public SubscriptionTermModel getDefaultSubscriptionTerm()
	{
		final String defaultConfigurationServiceId = getConfigurationService().getConfiguration()
				.getString(DEFAULT_SUBSCRIPTION_TERM_ID_KEY);

		if (defaultConfigurationServiceId == null)
		{
			return null;
		}

		return getSubscriptionTerm(defaultConfigurationServiceId);
	}

	@Override
	public List<SubscriptionTermModel> getSubscriptionTermsFor(final PriceRowModel priceRow)
	{
		final Set<SubscriptionTermModel> subscriptionTerms = priceRow.getSubscriptionTerms();

		if (CollectionUtils.isEmpty(subscriptionTerms))
		{
			return getAllSubscriptionTerms();
		}
		return new ArrayList<>(subscriptionTerms);
	}

	private Set<SubscriptionTermModel> getSubscriptionTermsFromSpo(final TmaProductOfferingModel spo,
			final TmaProcessType processType, final List<SubscriptionTermModel> allSubscriptionTerms)
	{
		final Set<PriceRowModel> applicablePricePlans = getSubscriptionPricePlansForProcessType(spo, processType);
		return getSubscriptionTerms(applicablePricePlans, allSubscriptionTerms);
	}

	private Set<SubscriptionTermModel> getSubscriptionTermsFromBpo(final TmaProductOfferingModel bpo,
			final TmaProductOfferingModel affectedPo, final TmaProcessType processType,
			final List<SubscriptionTermModel> allSubscriptionTerms)
	{
		final Set<PriceRowModel> applicablePricePlans = getSubscriptionPricePlansForProcessType(bpo, processType)
				.stream()
				.filter(pp -> containsAffectedProduct(pp, affectedPo))
				.collect(Collectors.toSet());
		return getSubscriptionTerms(applicablePricePlans, allSubscriptionTerms);
	}

	private Set<PriceRowModel> getSubscriptionPricePlansForProcessType(final TmaProductOfferingModel spo,
			final TmaProcessType processType)
	{
		return spo.getEurope1Prices().stream()
				.filter(pp -> containsProcessType(pp, processType))
				.collect(Collectors.toSet());
	}

	private Set<SubscriptionTermModel> getSubscriptionTerms(final Set<PriceRowModel> applicablePricePlans,
			final List<SubscriptionTermModel> allSubscriptionTerms)
	{
		final Set<SubscriptionTermModel> subscriptionTerms = new HashSet<>();
		for (final PriceRowModel pricePlan : applicablePricePlans)
		{
			if (pricePlan.getSubscriptionTerms().isEmpty())
			{
				subscriptionTerms.addAll(allSubscriptionTerms);
				break;
			}
			else
			{
				subscriptionTerms.addAll(getApplicableSubscriptionTerms(pricePlan, allSubscriptionTerms));
			}
		}
		return subscriptionTerms;
	}

	private Set<SubscriptionTermModel> getApplicableSubscriptionTerms(final PriceRowModel pricePlanModel,
			final List<SubscriptionTermModel> subscriptionTerms)
	{
		return pricePlanModel.getSubscriptionTerms().stream()
				.filter(pricePlanTerm -> subscriptionTerms.contains(pricePlanTerm)).collect(Collectors.toSet());
	}

	private boolean containsProcessType(final PriceRowModel pp, final TmaProcessType processType)
	{
		return pp.getProcessTypes() == null || pp.getProcessTypes().isEmpty() || pp.getProcessTypes().contains(processType);
	}

	private boolean containsAffectedProduct(final PriceRowModel pp, final TmaProductOfferingModel affectedProduct)
	{
		return pp.getAffectedProductOffering() == null || pp.getAffectedProductOffering().getCode()
				.equals(affectedProduct.getCode());
	}

	protected TmaSubscriptionTermDao getTmaSubscriptionTermDao()
	{
		return tmaSubscriptionTermDao;
	}

	@Required
	public void setTmaSubscriptionTermDao(final TmaSubscriptionTermDao tmaSubscriptionTermDao)
	{
		this.tmaSubscriptionTermDao = tmaSubscriptionTermDao;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}
