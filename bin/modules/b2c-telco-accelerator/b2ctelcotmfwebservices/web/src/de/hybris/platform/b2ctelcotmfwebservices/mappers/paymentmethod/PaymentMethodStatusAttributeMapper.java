/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethod;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodType;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for status attribute between {@link CCPaymentInfoData} and {@link PaymentMethodType}
 *
 * @since 1907
 */
public class PaymentMethodStatusAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentMethodType>
{
	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source, PaymentMethodType target, MappingContext context)
	{
		target.setStatus("active");
	}
}
