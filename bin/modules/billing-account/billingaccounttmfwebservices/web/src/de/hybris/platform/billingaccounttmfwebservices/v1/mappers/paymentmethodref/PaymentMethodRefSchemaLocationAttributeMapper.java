/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentmethodref;

import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link PaymentInfoModel} and {@link PaymentMethodRef}
 *
 * @since 2105
 * @deprecated since 2111
 */
@Deprecated(since = "2111")
public class PaymentMethodRefSchemaLocationAttributeMapper extends BaAttributeMapper<PaymentInfoModel, PaymentMethodRef>
{
	public PaymentMethodRefSchemaLocationAttributeMapper(String sourceAttributeName, String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(PaymentInfoModel source, PaymentMethodRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAtschemaLocation(BillingaccounttmfwebservicesConstants.BA_API_SCHEMA);
		}
	}
}
