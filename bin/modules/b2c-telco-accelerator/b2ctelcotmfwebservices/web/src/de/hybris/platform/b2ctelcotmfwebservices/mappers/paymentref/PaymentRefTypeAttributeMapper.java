/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentRef;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for type attribute between {@link CCPaymentInfoData} and {@link PaymentRef}
 *
 * @since 2003
 */
public class PaymentRefTypeAttributeMapper extends TmaAttributeMapper<CCPaymentInfoData, PaymentRef>
{
	@Override
	public void populateTargetAttributeFromSource(CCPaymentInfoData source, PaymentRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setType(PaymentRef.TypeEnum.CREDITCARD);
		}
	}
}
