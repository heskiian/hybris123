/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.paymentmethodref;

import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PaymentMethodRef;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PaymentInfoModel} and {@link PaymentMethodRef}
 *
 * @since 2108
 */
public class PaymentMethodRefAtReferredTypeAttributeMapper extends PrAttributeMapper<PaymentInfoModel, PaymentMethodRef>
{
	public PaymentMethodRefAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PaymentInfoModel source, final PaymentMethodRef target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		target.setAtreferredType(PartyroletmfwebservicesConstants.PAYMENT_METHOD_DEFAULT_REFERRED_TYPE);
	}
}
