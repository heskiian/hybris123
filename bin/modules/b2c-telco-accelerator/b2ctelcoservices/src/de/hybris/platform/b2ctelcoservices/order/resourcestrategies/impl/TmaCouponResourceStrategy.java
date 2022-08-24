/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.services.CouponService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resource strategy implementation. Validates and updates coupons.
 *
 * @since 2003
 */
public class TmaCouponResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private CouponService couponService;

	public TmaCouponResourceStrategy(final CouponService couponService)
	{
		this.couponService = couponService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter parameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();

		if (CollectionUtils.isEmpty(parameter.getCouponIds()))
		{
			result.setValid(true);
			result.setCommerceCartParameter(parameter);
			return result;
		}

		for (String couponId : getCouponsToBeApplied(parameter.getCart().getAppliedCouponCodes(), parameter.getCouponIds()))
		{

			if (!getCouponService().verifyCouponCode(couponId, parameter.getCart()).getSuccess())
			{
				result.setValid(false);
				result.setMessage("Invalid coupon code '" + couponId + "' for cart '" + parameter.getCart().getCode() + "'.");
				return result;
			}
		}

		result.setValid(true);
		result.setCommerceCartParameter(parameter);
		return result;
	}

	@Override
	public void updateResource(CommerceCartParameter commerceCartParameter,
			CommerceCartModification commerceCartModification)
	{
		if (commerceCartParameter.getCouponIds() == null)
		{
			return;
		}

		final CartModel cart = commerceCartParameter.getCart();
		final Collection<String> couponsInCart = cart.getAppliedCouponCodes();

		removeCoupons(cart, couponsInCart, commerceCartParameter.getCouponIds());
		applyCoupons(cart, couponsInCart, commerceCartParameter.getCouponIds());

		if (CollectionUtils.isNotEmpty(couponsInCart))
		{
			commerceCartModification.setCouponIds(new ArrayList<>(cart.getAppliedCouponCodes()));
		}
	}

	/**
	 * Removes from the cart the coupons which are in the list of coupons of the cart, but are not in the list of coupons of the request.
	 *
	 * @param cart
	 * 		The shopping cart
	 * @param couponsInCart
	 * 		The list of coupons in the cart
	 * @param newCouponIds
	 * 		The list of new coupons we want to add to the cart
	 */
	protected void removeCoupons(final CartModel cart, final Collection<String> couponsInCart, final List<String> newCouponIds)
	{
		if (CollectionUtils.isEmpty(couponsInCart))
		{
			return;
		}

		final List<String> couponsToBeRemoved;
		if (CollectionUtils.isEmpty(newCouponIds))
		{
			couponsToBeRemoved = new ArrayList<>(couponsInCart);
		}
		else
		{
			couponsToBeRemoved = couponsInCart.stream()
					.filter((String couponInCart) -> !newCouponIds.contains(couponInCart)).collect(Collectors.toList());
		}

		couponsToBeRemoved.forEach((String couponCode) -> getCouponService().releaseCouponCode(couponCode, cart));
	}

	/**
	 * Adds to the cart the coupons which are in the list of coupons of the request, but are not in the list of coupons of the cart.
	 *
	 * @param cart
	 * 		The shopping cart
	 * @param couponsInCart
	 * 		The list of coupons in the cart
	 * @param newCouponIds
	 * 		The list of new coupons we want to add to the cart
	 */
	protected void applyCoupons(final CartModel cart, final Collection<String> couponsInCart, final List<String> newCouponIds)
	{
		if (CollectionUtils.isEmpty(newCouponIds))
		{
			return;
		}

		final List<String> couponsToBeApplied = getCouponsToBeApplied(couponsInCart, newCouponIds);
		couponsToBeApplied.forEach((String couponId) -> getCouponService().redeemCoupon(couponId, cart));
	}

	private List<String> getCouponsToBeApplied(final Collection<String> couponsInCart, final List<String> couponIdList)
	{
		List<String> couponsToBeApplied;
		if (CollectionUtils.isEmpty(couponsInCart))
		{
			couponsToBeApplied = couponIdList;
		}
		else
		{
			couponsToBeApplied = couponIdList.stream()
					.filter((String couponId) -> !couponsInCart.contains(couponId)).collect(Collectors.toList());
		}
		return couponsToBeApplied;
	}

	protected CouponService getCouponService()
	{
		return couponService;
	}
}
