/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.subscriptionservices.model.BillingPlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Locale;


/**
 * Field Value Provider for indexing the Billing Frequency of the displayed Product Offering Price.
 *
 * @since 6.7
 */
public class TmaBillingTimeValueProvider extends TmaSubscriptionTermDependentValueProvider
{
	@Override
	protected Object getFieldValueForSubscriptionTerm(final SubscriptionTermModel subscriptionTerm)
	{
		return subscriptionTerm.getBillingPlan().getBillingFrequency();
	}

	@Override
	protected String getLocalizedString(final Object model, final Locale locale)
	{
		if (!(model instanceof SubscriptionTermModel))
		{
			return null;
		}

		final BillingPlanModel billingPlan = ((SubscriptionTermModel) model).getBillingPlan();
		return billingPlan != null && billingPlan.getBillingFrequency() != null ?
				billingPlan.getBillingFrequency().getNameInCart(locale) : null;
	}
}
