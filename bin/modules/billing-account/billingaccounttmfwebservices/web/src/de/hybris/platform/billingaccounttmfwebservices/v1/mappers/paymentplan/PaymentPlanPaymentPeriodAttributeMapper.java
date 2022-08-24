/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaDurationModel;
import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for paymentPeriod attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanPaymentPeriodAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	private BaGenericService baGenericService;

	public PaymentPlanPaymentPeriodAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		// no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final PaymentPlan target, final BaPaymentPlanModel source,
			final MappingContext context)
	{
		final BaDurationModel baDurationModel = (BaDurationModel) getBaGenericService().createItem(BaDurationModel.class);
		if (StringUtils.isNotEmpty(target.getPaymentFrequency()))
		{
			baDurationModel.setUnits(target.getPaymentFrequency());
		}
		if (!ObjectUtils.isEmpty(target.getNumberOfPayments()))
		{
			baDurationModel.setAmount(target.getNumberOfPayments().longValue());
		}
		source.setPaymentPeriod(baDurationModel);
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
