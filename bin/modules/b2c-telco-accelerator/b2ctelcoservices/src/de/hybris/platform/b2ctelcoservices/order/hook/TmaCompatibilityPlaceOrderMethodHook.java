/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.hook;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart validation hook to verify if cart contains compatibility errors.
 *
 * @since 1911
 */
public class TmaCompatibilityPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	private static final String COMPATIBILITY = "COMPATIBILITY";

	private TmaCompatibilityPolicyEngine tmaCompatibilityPolicyEngine;

	public TmaCompatibilityPlaceOrderMethodHook(final TmaCompatibilityPolicyEngine tmaCompatibilityPolicyEngine)
	{
		this.tmaCompatibilityPolicyEngine = tmaCompatibilityPolicyEngine;
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
		triggerCompatibility(cartModel);
		if (hasValidationErrors(cartModel))
		{
			throw new InvalidCartException(
					"Cannot place order because cart '" + parameter.getCart().getCode() + "' has compatibility errors.");
		}
	}

	@Override
	public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result)
	{
		// no implementation needed
	}

	/**
	 * Triggers the evaluation of compatibility policies for all the products in cart.
	 *
	 * @param cartModel
	 * 		cart for which compatibility policies are evaluated
	 */
	protected void triggerCompatibility(final CartModel cartModel)
	{
		cartModel.getEntries().stream()
				.filter((AbstractOrderEntryModel entry) -> entry.getMasterEntry() == null && CollectionUtils
						.isNotEmpty(entry.getChildEntries()))
				.forEach((AbstractOrderEntryModel entry) -> getTmaCompatibilityPolicyEngine()
						.verifyCompatibilityPolicies(entry, cartModel.getUser()));

		getTmaCompatibilityPolicyEngine().verifyCompatibilityPoliciesForStandaloneProducts(cartModel);
	}

	/**
	 * Checks the presence of validation error messages.
	 *
	 * @param cartModel
	 * 		cart for which validation error messages are evaluated
	 * @return <code>true</code> if the cart contains at least one validation error message, <code>false</code> otherwise.
	 */
	protected boolean hasValidationErrors(final CartModel cartModel)
	{
		if (CollectionUtils.isNotEmpty(cartModel.getCartValidations()) && hasCompatibilityError(cartModel.getCartValidations()))
		{
			return true;
		}

		if (CollectionUtils.isEmpty(cartModel.getEntries()))
		{
			return false;
		}

		return cartModel.getEntries().stream().anyMatch((AbstractOrderEntryModel entry) ->
				entry instanceof CartEntryModel && CollectionUtils.isNotEmpty(((CartEntryModel) entry).getCartEntryValidations()));
	}

	/**
	 * Checks if the list of validation messages contain any compatibility errors.
	 *
	 * @param validationMessages
	 * 		The list of validation messages
	 * @return <code>true</code> if the list contain compatibility validation messages, otherwise <code>false</code>
	 */
	protected boolean hasCompatibilityError(final Set<TmaCartValidationModel> validationMessages)
	{
		return validationMessages.stream().anyMatch((TmaCartValidationModel message) -> COMPATIBILITY.equals(message.getCode()));
	}

	protected TmaCompatibilityPolicyEngine getTmaCompatibilityPolicyEngine()
	{
		return tmaCompatibilityPolicyEngine;
	}

}
