/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethodref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link CCPaymentInfoData} and
 * {@link PaymentMethodRef}
 *
 * @since 1911
 */
public class PaymentMethodRefIdAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentMethodRef>
{
	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source,
			PaymentMethodRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}