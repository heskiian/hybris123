/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for paymentMethod attribute between {@link BaPaymentPlanModel} and {@link PaymentPlan}
 *
 * @since 2105
 * @deprecated since 2111
 */
@Deprecated(since = "2111")
public class PaymentPlanPaymentMethodAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	private MapperFacade mapperFacade;

	public PaymentPlanPaymentMethodAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			MappingContext context)
	{
		if (!ObjectUtils.isEmpty(source.getPaymentMethod()))
		{
			target.setPaymentMethod(getMapperFacade().map(source.getPaymentMethod(), PaymentMethodRef.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
