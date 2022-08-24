/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Money;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for totalAmount attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanTotalAmountAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	private CommonI18NService commonI18NService;

	public PaymentPlanTotalAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final CommonI18NService commonI18NService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		Money totalAmount = new Money();
		if (source.getTotalAmount() != null)
		{
			totalAmount.setValue(source.getTotalAmount().floatValue());
		}
		if (!ObjectUtils.isEmpty(source.getCurrency()))
		{
			totalAmount.setUnit(source.getCurrency().getIsocode());
		}

		target.setTotalAmount(totalAmount);
	}

	@Override
	public void populateSourceAttributeFromTarget(final PaymentPlan target, final BaPaymentPlanModel source,
			final MappingContext context)
	{
		if (target.getTotalAmount() == null)
		{
			return;
		}

		if (target.getTotalAmount().getValue() != null)
		{
			source.setTotalAmount(Double.valueOf(target.getTotalAmount().getValue()));
		}
		if (target.getTotalAmount().getUnit() != null)
		{
			source.setCurrency(getCommonI18NService().getCurrency(target.getTotalAmount().getUnit()));
		}
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

}
