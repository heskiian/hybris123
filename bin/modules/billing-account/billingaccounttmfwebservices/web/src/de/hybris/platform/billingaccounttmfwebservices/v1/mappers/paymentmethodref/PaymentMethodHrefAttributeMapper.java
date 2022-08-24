/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.paymentmethodref;

import de.hybris.platform.billingaccountservices.model.BaPaymentInfoModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BaPaymentInfoModel} and {@link PaymentMethodRef}
 *
 * @since 2111
 */
public class PaymentMethodHrefAttributeMapper extends BaAttributeMapper<BaPaymentInfoModel, PaymentMethodRef>
{
	public PaymentMethodHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPaymentInfoModel source, final PaymentMethodRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(BillingaccounttmfwebservicesConstants.PAYMENT_METHOD_REF_API_URL + source.getId());
		}
	}
}
