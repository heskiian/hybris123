/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.resolver;

import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Locale;
import java.util.function.Function;


/**
 * @since 1810
 */
public class TmaTermCodeSource extends TmaAbstractTermStringInfoSource
{
	@Override
	protected Function<SubscriptionTermModel, String> getFunction(final Locale locale)
	{
		return SubscriptionTermModel::getId;
	}
}
