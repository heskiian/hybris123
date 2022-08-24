/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.shoppingcart;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.CartData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for payment method attribute between {@link CartData} and
 * {@link ShoppingCart}
 *
 * @since 1911
 */
public class ShoppingCartPaymentMethodAttributeMapper extends TmaAttributeMapper<CartData, ShoppingCart>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public ShoppingCartPaymentMethodAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(CartData source, ShoppingCart target, MappingContext context)
	{
		if (source.getPaymentInfo() == null && CollectionUtils.isEmpty(source.getAppliedVouchers()))
		{
			return;
		}

		final List<PaymentMethodRef> paymentMethodList = new ArrayList<>();

		if (source.getPaymentInfo() != null)
		{
			paymentMethodList.add(getMapperFacade().map(source.getPaymentInfo(), PaymentMethodRef.class, context));
		}

		if (CollectionUtils.isEmpty(source.getAppliedVouchers()))
		{
			target.setPaymentMethod(CollectionUtils.isNotEmpty(paymentMethodList) ? paymentMethodList : null);
			return;
		}

		for (String couponCode : source.getAppliedVouchers())
		{
			final CouponData coupon = new CouponData();
			coupon.setCouponId(couponCode);
			paymentMethodList.add(getMapperFacade().map(coupon, PaymentMethodRef.class, context));
		}

		target.setPaymentMethod(paymentMethodList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
