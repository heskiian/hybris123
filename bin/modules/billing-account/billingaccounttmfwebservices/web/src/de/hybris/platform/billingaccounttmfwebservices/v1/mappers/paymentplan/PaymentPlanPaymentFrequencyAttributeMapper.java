/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for paymentFrequency attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanPaymentFrequencyAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	public PaymentPlanPaymentFrequencyAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getPaymentPeriod()) && !ObjectUtils.isEmpty(source.getPaymentPeriod().getUnits()))
		{
			target.setPaymentFrequency(source.getPaymentPeriod().getUnits());
		}
	}
}
