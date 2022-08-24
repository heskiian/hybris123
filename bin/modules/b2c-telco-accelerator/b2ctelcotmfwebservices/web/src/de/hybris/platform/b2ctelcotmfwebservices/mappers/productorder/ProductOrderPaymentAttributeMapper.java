/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.OrderData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for payment method attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 1907
 */
public class ProductOrderPaymentAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	public ProductOrderPaymentAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target, final MappingContext context)
	{
		if (source.getPaymentInfo() == null && CollectionUtils.isEmpty(source.getAppliedVouchers()))
		{
			return;
		}

		final List<PaymentRef> paymentList = new ArrayList<>();

		if (source.getPaymentInfo() != null)
		{
			paymentList.add(getMapperFacade().map(source.getPaymentInfo(), PaymentRef.class, context));
		}

		if (CollectionUtils.isEmpty(source.getAppliedVouchers()))
		{
			target.setPayment(paymentList);
			return;
		}

		source.getAppliedVouchers().forEach(couponCode ->
		{
			final CouponData coupon = new CouponData();
			coupon.setCouponId(couponCode);
			paymentList.add(getMapperFacade().map(coupon, PaymentRef.class, context));
		});

		target.setPayment(paymentList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
