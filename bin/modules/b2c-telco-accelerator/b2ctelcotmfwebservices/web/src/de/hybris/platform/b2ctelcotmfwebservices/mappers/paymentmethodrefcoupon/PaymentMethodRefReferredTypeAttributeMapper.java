/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.paymentmethodrefcoupon;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atrefferedType attribute between {@link CouponData} and {@link PaymentMethodRef}
 *
 * @since 2003
 */
public class PaymentMethodRefReferredTypeAttributeMapper extends TmaAttributeMapper<CouponData, PaymentMethodRef>
{
	@Override
	public void populateTargetAttributeFromSource(CouponData source, PaymentMethodRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCouponId()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.TMA_PAYMENT_DEFAULT_REFERRED));
		}
	}
}
