/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @since 1810
 */
public abstract class TmaAbstractTermStringInfoSource implements TmaTermInfoSource<String>
{
	private CommonI18NService i18nService;

	@Override
	public List<String> getItem(Collection<SubscriptionTermModel> terms, LanguageModel language, CurrencyModel currency)
	{
		Locale locale = language == null ? null : i18nService.getLocaleForLanguage(language);
		return terms == null || terms.isEmpty() ?
				Collections.singletonList("") :
				terms.stream().map(getFunction(locale)).collect(Collectors.toList());
	}

	protected abstract Function<SubscriptionTermModel, String> getFunction(Locale locale);

	public CommonI18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(CommonI18NService i18nService)
	{
		this.i18nService = i18nService;
	}
}
