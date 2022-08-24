/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * @since 1810
 */
public class TmaTermFrequencyInfoSource implements TmaTermInfoSource<String>
{
	private CommonI18NService i18nService;

	@Override
	public List<String> getItem(Collection<SubscriptionTermModel> terms, LanguageModel language, CurrencyModel currency)
	{
		Locale locale = i18nService.getLocaleForIsoCode(language.getIsocode());
		return terms == null || terms.isEmpty() ?
				null :
				terms.stream().map(SubscriptionTermModel::getBillingPlan)
						.filter(billingPlan -> billingPlan != null && billingPlan.getBillingFrequency() != null)
						.map(billingPlan -> billingPlan.getBillingFrequency().getNameInCart(locale)).collect(Collectors.toList());
	}

	public CommonI18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(CommonI18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}
