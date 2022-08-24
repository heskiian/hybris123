/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentrefcoupon;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentRef;
import de.hybris.platform.commercefacades.coupon.data.CouponData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for code attribute between {@link CouponData} and {@link PaymentRef}
 *
 * @since 2003
 */
public class PaymentRefCodeAttributeMapper extends TmaAttributeMapper<CouponData, PaymentRef>
{
	@Override
	public void populateTargetAttributeFromSource(CouponData source, PaymentRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCouponId()))
		{
			target.setCode(source.getCouponId());
		}
	}
}
