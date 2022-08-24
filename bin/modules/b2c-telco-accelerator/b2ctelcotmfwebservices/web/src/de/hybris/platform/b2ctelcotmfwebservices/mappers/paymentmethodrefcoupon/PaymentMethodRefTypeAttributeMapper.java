/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethodrefcoupon;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.commercefacades.coupon.data.CouponData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for type attribute between {@link CouponData} and {@link PaymentMethodRef}
 *
 * @since 2003
 */
public class PaymentMethodRefTypeAttributeMapper extends TmaAttributeMapper<CouponData, PaymentMethodRef>
{
	@Override
	public void populateTargetAttributeFromSource(CouponData source, PaymentMethodRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCouponId()))
		{
			target.setType(PaymentMethodRef.TypeEnum.VOUCHER);
		}
	}
}
