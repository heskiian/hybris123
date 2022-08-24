/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Collection;
import java.util.List;


/**
 * @param <T>
 * @since 1810
 */
public interface TmaTermInfoSource<T>
{
	List<T> getItem(Collection<SubscriptionTermModel> terms, LanguageModel language, CurrencyModel currency);
}
