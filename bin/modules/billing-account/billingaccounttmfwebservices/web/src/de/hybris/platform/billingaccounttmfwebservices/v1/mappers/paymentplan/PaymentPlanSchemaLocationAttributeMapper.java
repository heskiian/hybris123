/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentplan;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentPlan;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link BaPaymentPlanModel} and
 * {@link PaymentPlan}
 *
 * @since 2105
 */
public class PaymentPlanSchemaLocationAttributeMapper extends BaAttributeMapper<BaPaymentPlanModel, PaymentPlan>
{
	public PaymentPlanSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentPlanModel source, final PaymentPlan target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillingaccounttmfwebservicesConstants.BA_API_SCHEMA);
		}
	}
}
