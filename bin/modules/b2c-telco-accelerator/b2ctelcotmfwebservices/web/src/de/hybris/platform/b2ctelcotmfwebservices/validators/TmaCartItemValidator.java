/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ActionType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CartItem;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Place;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validator for {@link CartItem}.
 *
 * @since 1907
 */
public class TmaCartItemValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String FIELD_MUST_BE_EMPTY_MESSAGE = "field.must.be.empty";
	private static final String INVALID_QUANTITY_VALUE = "quantity.invalid";
	private static final String INVALID_REQUEST = "request.invalid";
	private static final String INVALID_ID = "id.invalid";
	private static final String ID = "id";
	private static final String QUANTITY = "quantity";
	private static final String CHILD_ITEM = "cartItem";
	private static final String PROCESS_TYPE = "processType";
	private static final String PROCESS_TYPE_ID = "processType.id";
	private static final String PRODUCT_OFFERING = "productOffering";
	private static final String PRODUCT_OFFERING_ID = "productOffering.id";
	private static final String PRODUCT_PLACE = "product.place";
	private static final String PRODUCT_ID = "product.id";
	private static final String ACTION = "action";

	private TmaPlaceValidator placeValidator;

	public TmaCartItemValidator(final TmaPlaceValidator placeValidator)
	{
		this.placeValidator = placeValidator;
	}

	@Override
	public boolean supports(Class<?> aClass)
	{
		return CartItem.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		final CartItem cartItem = (CartItem) object;

		validateActions(cartItem, errors);
		validateAddParameters(cartItem, errors);
		validateModifyParameters(cartItem, errors);
		validateDeleteParameters(cartItem, errors);
	}

	/**
	 * Checks if cart item is correct for adding a cartItem.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateAddParameters(final CartItem cartItem, final Errors errors)
	{
		if (StringUtils.isNotEmpty(cartItem.getId()))
		{
			return;
		}

		final List<CartItem> cartItemList = cartItem.getCartItem();

		if (CollectionUtils.isEmpty(cartItemList))
		{
			validateAddSpo(cartItem, errors);
			return;
		}

		validateAddBpo(cartItem, errors);
	}

	/**
	 * Checks if cart item is correct for modifying a cartItem.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateModifyParameters(final CartItem cartItem, final Errors errors)
	{
		final Integer quantity = cartItem.getQuantity();
		final String id = cartItem.getId();
		if (StringUtils.isEmpty(id) || (quantity != null && quantity == 0))
		{
			return;
		}

		if (CollectionUtils.isEmpty(cartItem.getCartItem()))
		{
			validateId(cartItem.getId(), errors);
			validatePlaces(cartItem, errors);
			return;
		}

		validateAddPoToExistingBpo(cartItem, errors);
	}

	/**
	 * Checks if cart item is correct for deleting a cartItem.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateDeleteParameters(final CartItem cartItem, final Errors errors)
	{
		final Integer quantity = cartItem.getQuantity();
		if (quantity == null || quantity != 0)
		{
			return;
		}

		validateId(cartItem.getId(), errors);

		if (CollectionUtils.isNotEmpty(cartItem.getCartItem()))
		{
			errors.rejectValue(CHILD_ITEM, INVALID_REQUEST);
		}
	}

	/**
	 * Checks if SPO added to cart is correct.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateAddSpo(final CartItem cartItem, final Errors errors)
	{
		validateQuantity(cartItem.getQuantity(), QUANTITY, errors);
		if (cartItem.getProcessType() == null)
		{
			errors.rejectValue(PROCESS_TYPE, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		ValidationUtils.rejectIfEmpty(errors, PROCESS_TYPE_ID, FIELD_REQUIRED_MESSAGE_ID);

		validatePlaces(cartItem, errors);
	}

	/**
	 * Checks if BPO added to cart is correct.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateAddBpo(CartItem cartItem, Errors errors)
	{
		final ProductOfferingRef productOffering = cartItem.getProductOffering();

		if ((productOffering == null || StringUtils.isEmpty(productOffering.getId())))
		{
			errors.rejectValue(PRODUCT_OFFERING, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		validateChildCartItems(cartItem.getCartItem(), errors);
		validateQuantity(cartItem.getQuantity(), QUANTITY, errors);

		if (cartItem.getProcessType() == null)
		{
			errors.rejectValue(PROCESS_TYPE, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		ValidationUtils.rejectIfEmpty(errors, PROCESS_TYPE_ID, FIELD_REQUIRED_MESSAGE_ID);
	}

	protected void validateChildCartItems(final List<CartItem> cartItems, final Errors errors)
	{
		for (int i = 0; i < cartItems.size(); i++)
		{
			final String childItem = CHILD_ITEM + "[" + i + "].";
			final CartItem item = cartItems.get(i);

			if (item.getProcessType() != null)
			{
				errors.rejectValue(PROCESS_TYPE, FIELD_MUST_BE_EMPTY_MESSAGE);
				return;
			}

			validateActions(item, errors);
			validateQuantity(item.getQuantity(), childItem + QUANTITY, errors);
			validatePlaces(item, errors);

			if (CollectionUtils.isNotEmpty(cartItems.get(i).getCartItem()))
			{
				validateChildCartItems(cartItems.get(i).getCartItem(), errors);
			}
		}
	}

	/**
	 * Checks if the quantity of the cart item is correct.
	 *
	 * @param quantity
	 * 		the quantity of the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validateQuantity(final Integer quantity, final String fieldName, final Errors errors)
	{
		if (quantity == null)
		{
			errors.rejectValue(QUANTITY, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}
		validateQuantityPositiveValue(quantity, fieldName, errors);
	}

	/**
	 * Checks that all the places on product are valid.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	protected void validatePlaces(final CartItem cartItem, final Errors errors)
	{
		final Product product = cartItem.getProduct();
		if (product != null && CollectionUtils.isNotEmpty(product.getPlace()))
		{
			final List<Place> places = product.getPlace();
			int i = 0;
			for (Place place : places)
			{
				try
				{
					errors.pushNestedPath(PRODUCT_PLACE + "[" + i + "]");
					ValidationUtils.invokeValidator(getPlaceValidator(), place, errors);
				}
				finally
				{
					errors.popNestedPath();
					i++;
				}
			}
		}
	}

	/**
	 * Checks that the subscribed product and offering are correctly set for the action found on the cartItem.
	 *
	 * @param cartItem
	 * 		the input cartItem
	 * @param errors
	 * 		the errors found
	 */
	protected void validateActions(final CartItem cartItem, final Errors errors)
	{
		ActionType actionType = cartItem.getAction();
		if (actionType == null && cartItem.getProduct() != null && StringUtils.isNotEmpty(cartItem.getProduct().getId()))
		{
			errors.rejectValue(ACTION, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}
		if (StringUtils.isNotEmpty(cartItem.getId()) && actionType == null)
		{
			return;
		}
		String subscribedProductId = cartItem.getProduct() != null ? cartItem.getProduct().getId() : null;
		String productOfferingId = cartItem.getProductOffering() != null ? cartItem.getProductOffering().getId() : null;
		validateAction(errors, actionType, subscribedProductId, productOfferingId);
	}

	/**
	 * Checks if PO added to cart is correct.
	 *
	 * @param cartItem
	 * 		the cart item
	 * @param errors
	 * 		the errors found
	 */
	private void validateAddPoToExistingBpo(final CartItem cartItem, final Errors errors)
	{
		if (cartItem.getProcessType() != null)
		{
			errors.rejectValue(PROCESS_TYPE, FIELD_MUST_BE_EMPTY_MESSAGE);
			return;
		}

		if (cartItem.getQuantity() != null)
		{
			errors.rejectValue(QUANTITY, FIELD_MUST_BE_EMPTY_MESSAGE);
			return;
		}

		if (CollectionUtils.isEmpty(cartItem.getCartItem()))
		{
			return;
		}

		validateChildCartItems(cartItem.getCartItem(), errors);
	}

	/**
	 * Checks if the identifier of the cart item is correct.
	 *
	 * @param id
	 * 		the identifier of the cart item
	 * @param errors
	 * 		the errors found
	 */
	private void validateId(final String id, final Errors errors)
	{
		if (StringUtils.isEmpty(id))
		{
			errors.rejectValue(ID, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		try
		{
			if (Integer.valueOf(id) < 0)
			{
				errors.rejectValue(ID, INVALID_ID);
			}
		}
		catch (final NumberFormatException e)
		{
			errors.rejectValue(ID, INVALID_ID);
		}
	}

	private void validateAction(Errors errors, ActionType actionType, String subscribedProductId, String productOfferingId)
	{

		if (actionType == null)
		{
			if (StringUtils.isEmpty(productOfferingId))
			{
				errors.rejectValue(PRODUCT_OFFERING_ID, FIELD_REQUIRED_MESSAGE_ID);
			}
			return;
		}

		switch (actionType)
		{
			case ADD:
				if (StringUtils.isEmpty(productOfferingId))
				{
					errors.rejectValue(PRODUCT_OFFERING_ID, FIELD_REQUIRED_MESSAGE_ID);
				}
				break;
			case UPDATE:
				if (StringUtils.isEmpty(productOfferingId))
				{
					errors.rejectValue(PRODUCT_OFFERING_ID, FIELD_REQUIRED_MESSAGE_ID);
				}
				if (StringUtils.isEmpty(subscribedProductId))
				{
					errors.rejectValue(PRODUCT_ID, FIELD_REQUIRED_MESSAGE_ID);
				}
				break;
			default:
				if (StringUtils.isEmpty(subscribedProductId))
				{
					errors.rejectValue(PRODUCT_ID, FIELD_REQUIRED_MESSAGE_ID);
				}
				if (StringUtils
						.isNotEmpty(productOfferingId))
				{
					errors.rejectValue(PRODUCT_OFFERING_ID, FIELD_MUST_BE_EMPTY_MESSAGE);
				}
				break;
		}
	}

	private void validateQuantityPositiveValue(final Integer quantity, final String fieldName, final Errors errors)
	{
		if (quantity <= 0)
		{
			errors.rejectValue(fieldName, INVALID_QUANTITY_VALUE);
		}
	}

	protected TmaPlaceValidator getPlaceValidator()
	{
		return placeValidator;
	}
}
