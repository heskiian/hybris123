/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;


/**
 * Generic validator for {@link ShoppingCart}.
 *
 * @since 2003
 */
public class TmaShoppingCartValidator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String PAYMENT_METHOD_DUPLICATE_MESSAGE_ID = "paymentMethod.duplicate";
	private static final String PAYMENT_METHOD = "paymentMethod";

	/**
	 * Checks if payment method is correct.
	 *
	 * @param paymentMethodRefList
	 * 		payment method reference list
	 * @param errors
	 * 		the errors found
	 */
	protected void validatePaymentMethodList(final List<PaymentMethodRef> paymentMethodRefList, final Errors errors)
	{
		if (CollectionUtils.isEmpty(paymentMethodRefList))
		{
			return;
		}

		for (int i = 0; i < paymentMethodRefList.size(); i++)
		{
			if (paymentMethodRefList.get(i).getType() == null)
			{
				errors.rejectValue(PAYMENT_METHOD + "[" + i + "]" + ".type", FIELD_REQUIRED_MESSAGE_ID);
				return;
			}

			if (paymentMethodRefList.get(i).getType().equals(PaymentMethodRef.TypeEnum.VOUCHER) && StringUtils.isEmpty(
					paymentMethodRefList.get(i).getCode()))
			{
				errors.rejectValue(PAYMENT_METHOD + "[" + i + "]" + ".code", FIELD_REQUIRED_MESSAGE_ID);
				return;
			}
		}

		if (!paymentMethodRefList.stream().allMatch(new HashSet<>()::add))
		{
			errors.rejectValue(PAYMENT_METHOD, PAYMENT_METHOD_DUPLICATE_MESSAGE_ID);
		}
	}
}
