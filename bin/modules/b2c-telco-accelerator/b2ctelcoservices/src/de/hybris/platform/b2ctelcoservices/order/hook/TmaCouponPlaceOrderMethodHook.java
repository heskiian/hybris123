/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.hook;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.InvalidCartException;

import org.apache.commons.collections.CollectionUtils;


/**
 * Order validation hook to verify if coupons on cart used to place the order are valid.
 *
 * @since 2003
 */
public class TmaCouponPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	/**
	 * Coupon service
	 */
	private CouponService couponService;

	public TmaCouponPlaceOrderMethodHook(final CouponService couponService)
	{
		this.couponService = couponService;
	}

	@Override
	public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel)
	{
		// no implementation needed
	}

	@Override
	public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();

		if (cartModel == null || CollectionUtils.isEmpty(cartModel.getAppliedCouponCodes()))
		{
			return;
		}

		for (String couponId : cartModel.getAppliedCouponCodes())
		{
			if (!getCouponService().verifyCouponCode(couponId, cartModel).getSuccess())
			{
				throw new InvalidCartException(
						"Invalid coupon '" + couponId + "' for shopping cart '" + cartModel.getCode() + "'.");
			}
		}
	}

	@Override
	public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
	{
		// no implementation needed
	}

	protected CouponService getCouponService()
	{
		return couponService;
	}
}
