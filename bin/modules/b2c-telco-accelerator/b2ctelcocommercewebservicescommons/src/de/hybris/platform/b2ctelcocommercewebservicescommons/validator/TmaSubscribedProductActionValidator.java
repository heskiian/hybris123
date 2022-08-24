/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ActionTypeWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates the subscribed product and offering attributes if the {@link OrderEntryWsDTO#getAction()} is present.
 *
 * @since 1911
 */
public class TmaSubscribedProductActionValidator implements Validator
{
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final String PRODUCT_ID = "subscribedProduct.id";
	private static final String PRODUCT_OFFERING_CODE = "product.code";
	private static final String FIELD_MUST_BE_EMPTY_MESSAGE = "field.must.be.empty";

	private static final String ACTION = "action";

	@Override
	public boolean supports(Class<?> aClass)
	{
		return OrderEntryWsDTO.class.equals(aClass);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		final OrderEntryWsDTO cartEntry = (OrderEntryWsDTO) object;
		final ActionTypeWsDTO actionType = cartEntry.getAction();
		if (actionType == null && StringUtils
				.isNotEmpty(cartEntry.getSubscribedProduct() != null ? cartEntry.getSubscribedProduct().getId() : null))
		{
			errors.rejectValue(ACTION, FIELD_REQUIRED_MESSAGE_ID);
			return;
		}

		if (cartEntry.getEntryNumber() != null && actionType == null)
		{
			return;
		}
		validateRequiredInformationFor(cartEntry, actionType, errors);
	}

	/**
	 * Checks that the subscribed product and offering are correctly set for the action found on the cartItem.
	 *
	 * @param cartEntry
	 * 		the input cartItem
	 */
	protected void validateRequiredInformationFor(final OrderEntryWsDTO cartEntry, final ActionTypeWsDTO actionType,
			final Errors errors)
	{
		final String subscribedProductId =
				cartEntry.getSubscribedProduct() != null ? cartEntry.getSubscribedProduct().getId() : null;
		final String productOfferingId = cartEntry.getProduct() != null ? cartEntry.getProduct().getCode() : null;

		if (actionType == null)
		{
			validateAttributeIsPresent(PRODUCT_OFFERING_CODE, productOfferingId, errors);
			return;
		}

		switch (actionType)
		{
			case ADD:
				validateAttributeIsPresent(PRODUCT_OFFERING_CODE, productOfferingId, errors);
				break;
			case UPDATE:
				validateAttributeIsPresent(PRODUCT_OFFERING_CODE, productOfferingId, errors);
				validateAttributeIsPresent(PRODUCT_ID, subscribedProductId, errors);
				break;
			default:
				validateAttributeIsPresent(PRODUCT_ID, subscribedProductId, errors);
				validateAttributeIsNotPresent(PRODUCT_OFFERING_CODE, productOfferingId, errors);
				break;
		}
	}

	private void validateAttributeIsPresent(final String attributeName, final String attributeValue, final Errors errors)
	{
		if (StringUtils.isEmpty(attributeValue))
		{
			errors.rejectValue(attributeName, FIELD_REQUIRED_MESSAGE_ID);
		}
	}

	private void validateAttributeIsNotPresent(final String attributeName, final String attributeValue, final Errors errors)
	{
		if (StringUtils.isNotEmpty(attributeValue))
		{
			errors.rejectValue(attributeName, FIELD_MUST_BE_EMPTY_MESSAGE);
		}
	}
}
