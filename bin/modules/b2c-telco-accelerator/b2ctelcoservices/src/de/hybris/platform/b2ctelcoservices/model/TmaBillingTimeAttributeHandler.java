/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.model;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Attribute handler for dynamic attribute billing time
 *
 * @param <T>
 * 		the model type
 * @since 1907
 */
public abstract class TmaBillingTimeAttributeHandler<T extends AbstractItemModel>
		extends AbstractDynamicAttributeHandler<BillingTimeModel, T>
{

	@Override
	public BillingTimeModel get(T model)
	{
		final SubscriptionPricePlanModel subscriptionPricePlan = getSubscriptionPricePlan(model);
		if (subscriptionPricePlan == null)
		{
			return null;
		}

		final Set<SubscriptionTermModel> subscriptionTerms = subscriptionPricePlan.getSubscriptionTerms();
		if (CollectionUtils.isNotEmpty(subscriptionTerms))
		{
			final SubscriptionTermModel subscriptionTerm = subscriptionTerms.iterator().next();
			if (subscriptionTerm.getBillingPlan() != null)
			{
				return subscriptionTerm.getBillingPlan().getBillingFrequency();
			}
		}
		return null;
	}

	protected abstract SubscriptionPricePlanModel getSubscriptionPricePlan(T model);

}
