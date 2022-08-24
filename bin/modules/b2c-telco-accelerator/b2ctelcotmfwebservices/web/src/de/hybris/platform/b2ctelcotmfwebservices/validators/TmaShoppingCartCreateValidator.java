/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryModeRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ShoppingCart;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validator for {@link ShoppingCart}.
 *
 * @since 1907
 */
public class TmaShoppingCartCreateValidator extends TmaShoppingCartValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String PAYMENT_METHOD_MESSAGE_ID = "paymentMethod.invalid";
	private static final String PLACE_MESSAGE_ID = "place.invalid";
	private static final String INVALID_DELIVERY_MODE_MESSAGE_ID = "deliveryMode.invalid";

	private static final String RELATED_PARTY = "relatedParty";
	private static final String FIRST_RELATED_PARTY_ID = "relatedParty[0].id";
	private static final String CART_ITEM = "cartItem";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String DELIVERY_MODE = "deliveryMode";
	private static final String PLACE = "place";

	private TmaCartItemValidator cartItemValidator;

	@Override
	public boolean supports(Class<?> aClass)
	{
		return ShoppingCart.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		final ShoppingCart shoppingCart = (ShoppingCart) object;

		final List<RelatedPartyRef> relatedPartyRefList = shoppingCart.getRelatedParty();

		validateRelatedParty(relatedPartyRefList, errors);

		if (CollectionUtils.isEmpty(shoppingCart.getCartItem()))
		{
			validatePaymentMethod(shoppingCart.getPaymentMethod(), errors);
			validatePlace(shoppingCart.getPlace(), errors);
			validateDeliveryMode(shoppingCart.getDeliveryMode(), errors);
			return;
		}

		validatePaymentMethodList(shoppingCart.getPaymentMethod(), errors);

		final List<CartItem> cartItemList = shoppingCart.getCartItem();

		for (int i = 0; i < cartItemList.size(); i++)
		{
			final CartItem item = cartItemList.get(i);
			final String cartItem = CART_ITEM + "[" + i + "]";

			try
			{
				errors.pushNestedPath(cartItem);
				ValidationUtils.invokeValidator(getCartItemValidator(), item, errors);
			}
			finally
			{
				errors.popNestedPath();
			}
		}
	}

	/**
	 * Checks if payment method is correct.
	 *
	 * @param paymentMethodRef
	 * 		payment method reference
	 * @param errors
	 * 		the errors found
	 */
	protected void validatePaymentMethod(List<PaymentMethodRef> paymentMethodRef, Errors errors)
	{
		if (CollectionUtils.isEmpty(paymentMethodRef))
		{
			return;
		}

		errors.rejectValue(PAYMENT_METHOD, PAYMENT_METHOD_MESSAGE_ID);
	}

	/**
	 * Checks if place is correct.
	 *
	 * @param places
	 * 		place reference
	 * @param errors
	 * 		the errors found
	 */
	protected void validatePlace(List<Place> places, Errors errors)
	{
		if (CollectionUtils.isEmpty(places))
		{
			return;
		}

		errors.rejectValue(PLACE, PLACE_MESSAGE_ID);
	}

	/**
	 * Checks if delivery mode is correct.
	 *
	 * @param deliveryMode
	 * 		the delivery mode
	 * @param errors
	 * 		the errors found
	 */
	protected void validateDeliveryMode(DeliveryModeRef deliveryMode, Errors errors)
	{
		if (deliveryMode != null && StringUtils.isNotEmpty(deliveryMode.getId()))
		{
			errors.rejectValue(DELIVERY_MODE, INVALID_DELIVERY_MODE_MESSAGE_ID);
		}
	}

	/**
	 * Checks if related party is correct.
	 *
	 * @param relatedPartyRefList
	 * 		related party reference list
	 * @param errors
	 * 		the errors found
	 */
	protected void validateRelatedParty(final List<RelatedPartyRef> relatedPartyRefList, final Errors errors)
	{
		if (CollectionUtils.isEmpty(relatedPartyRefList))
		{
			errors.rejectValue(RELATED_PARTY, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		if (StringUtils.isEmpty(relatedPartyRefList.get(0).getId()))
		{
			errors.rejectValue(FIRST_RELATED_PARTY_ID, FIELD_REQUIRED_MESSAGE_ID);
		}
	}

	protected TmaCartItemValidator getCartItemValidator()
	{
		return cartItemValidator;
	}

	@Required
	public void setCartItemValidator(TmaCartItemValidator cartItemValidator)
	{
		this.cartItemValidator = cartItemValidator;
	}
}
