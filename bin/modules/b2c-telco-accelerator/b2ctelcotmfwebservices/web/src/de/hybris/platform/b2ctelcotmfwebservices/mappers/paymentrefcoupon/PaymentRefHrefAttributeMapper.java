/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentrefcoupon;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentRef;
import de.hybris.platform.commercefacades.coupon.data.CouponData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link CouponData} and {@link PaymentRef}
 *
 * @since 2003
 */
public class PaymentRefHrefAttributeMapper extends TmaAttributeMapper<CouponData, PaymentRef>
{
	@Override
	public void populateTargetAttributeFromSource(CouponData source, PaymentRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCouponId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.TMA_PAYMENT_METHOD_API_URL + source.getCouponId());
		}
	}
}
