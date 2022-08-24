/**
 *Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcocommercewebservicescommons.exception.TmaCartVoucherExpiredException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 *
 * Default tma cart validator. Checks if cart is calculated and if needed values are filled.
 *
 * @since 2007
 *
 */

public class TmaPlaceOrderCartValidator implements Validator
{
	private final VoucherFacade voucherFacade;

	public TmaPlaceOrderCartValidator(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CartData.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final CartData cart = (CartData) target;

		if (!cart.isCalculated())
		{
			errors.reject("cart.notCalculated");
		}

		if (cart.getDeliveryMode() == null)
		{
			errors.reject("cart.deliveryModeNotSet");
		}

		if (cart.getPaymentInfo() == null)
		{
			errors.reject("cart.paymentInfoNotSet");
		}
		validateCartCoupon(cart);
	}

	private void validateCartCoupon(final CartData cart)
	{
		final TmaCartVoucherExpiredException exception = new TmaCartVoucherExpiredException("cartVoucherExpired");
		final List<String> appliedCoupons = cart.getAppliedVouchers();
		appliedCoupons.stream().filter(couponCode -> !voucherFacade.checkVoucherCode(couponCode)).collect(Collectors.toList())
				.forEach(invalidCoupon -> exception.addInvalidVoucher(invalidCoupon));
		if (CollectionUtils.isNotEmpty(exception.getInvalidVouchers()))
		{
			throw exception;
		}
	}
}
