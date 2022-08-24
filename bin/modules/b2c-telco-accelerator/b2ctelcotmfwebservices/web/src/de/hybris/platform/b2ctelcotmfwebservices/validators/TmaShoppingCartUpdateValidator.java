/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
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
public class TmaShoppingCartUpdateValidator extends TmaShoppingCartValidator implements Validator
{
	private TmaCartItemValidator cartItemValidator;

	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";

	private static final String RELATED_PARTY = "relatedParty";
	private static final String FIRST_RELATED_PARTY_ID = "relatedParty[0].id";

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
		validatePaymentMethodList(shoppingCart.getPaymentMethod(), errors);

		final List<CartItem> cartItemList = shoppingCart.getCartItem();

		if (CollectionUtils.isEmpty(cartItemList))
		{
			return;
		}

		for (int i = 0; i < cartItemList.size(); i++)
		{
			final CartItem cartItem = cartItemList.get(i);
			try
			{
				errors.pushNestedPath("cartItem[" + i + "]");
				ValidationUtils.invokeValidator(getCartItemValidator(), cartItem, errors);
			}
			finally
			{
				errors.popNestedPath();
			}
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
