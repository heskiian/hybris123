/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.subscriptionservices.model.BillingFrequencyModel;
import de.hybris.platform.subscriptionservices.model.BillingPlanModel;
import de.hybris.platform.subscriptionservices.model.RecurringChargeEntryModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdChgRecurringType;

import java.math.BigDecimal;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link RecurringChargeEntryModel} to {@link I_UtilsProdChgRecurringType}
 *
 * @since 1911
 */

public class UpilRecurringChargesPopulator implements Populator<RecurringChargeEntryModel, I_UtilsProdChgRecurringType>
{
	@Override
	public void populate(final RecurringChargeEntryModel source, final I_UtilsProdChgRecurringType target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setUtilitiesProduct(source.getSubscriptionPricePlanRecurring().getCode());
		target.setUtilsPriceID(source.getId());
		target.setUtilsPriceName(source.getId());
		target.setUtilsPriceAmount(BigDecimal.valueOf(source.getPrice()));
		if (!ObjectUtils.isEmpty(source.getCurrency()))
		{
			target.setUtilsPriceCurrency(source.getCurrency().getIsocode());
		}

		if (!ObjectUtils.isEmpty(source.getSemantics()))
		{
			target.setUtilsSemanticsName1(source.getSemantics().getSemanticsName1());
			target.setUtilsSemanticsName2(source.getSemantics().getSemanticsName2());
		}

		final BillingFrequencyModel billingFrequency = getUtilsPriceFrequencyDetails(source.getSubscriptionPricePlanRecurring());
		if (!ObjectUtils.isEmpty(billingFrequency))
		{
			target.setUtilsPriceFrequencyUnit(billingFrequency.getFrequencyUnit());
			target.setUtilsPriceFrequencyValue(billingFrequency.getFrequencyValue());
		}

	}

	private BillingFrequencyModel getUtilsPriceFrequencyDetails(final SubscriptionPricePlanModel price)
	{
		if (!CollectionUtils.isEmpty(price.getSubscriptionTerms()) && price.getSubscriptionTerms().iterator().hasNext())
		{
			final BillingPlanModel billingPlan = price.getSubscriptionTerms().iterator().next().getBillingPlan();
			if (!ObjectUtils.isEmpty(billingPlan) && !ObjectUtils.isEmpty(billingPlan.getBillingFrequency()))
			{
				return billingPlan.getBillingFrequency();
			}
		}
		return null;
	}
}
