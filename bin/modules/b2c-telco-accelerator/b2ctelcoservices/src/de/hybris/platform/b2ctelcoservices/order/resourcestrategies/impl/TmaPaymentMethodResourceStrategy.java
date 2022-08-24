/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * Resource strategy implementation. Validates and updates payment method.
 *
 * @since 1911
 */
public class TmaPaymentMethodResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private static final Logger LOG = Logger.getLogger(TmaPaymentMethodResourceStrategy.class);

	private CustomerAccountService customerAccountService;
	private ModelService modelService;

	public TmaPaymentMethodResourceStrategy(CustomerAccountService customerAccountService, ModelService modelService)
	{
		this.customerAccountService = customerAccountService;
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter parameter)
	{
		TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);

		return result;
	}

	/**
	 * Adds a payment method to the cart if one is provided or removes the payment method from the cart if none is provided.
	 *
	 * @param commerceCartParameter
	 * 		contains attributes used for cart updates
	 * @param commerceCartModification
	 * 		contains the updates made on the cart
	 * @throws CommerceCartModificationException
	 * 		in case of any error occurs during payment method update
	 */
	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException
	{
		if (commerceCartParameter.getPaymentMethodId() == null)
		{
			return;
		}

		final UserModel userModel = commerceCartParameter.getUser();
		final CartModel cartModel = commerceCartParameter.getCart();

		if (!(userModel instanceof CustomerModel))
		{
			throw new CommerceCartModificationException("The user must be a customer!");
		}

		final CustomerModel customer = (CustomerModel) userModel;

		if (StringUtils.isEmpty(commerceCartParameter.getPaymentMethodId()))
		{
			LOG.debug(
					"Removing payment method '" + commerceCartParameter.getPaymentMethodId() + "' for cart '" + commerceCartParameter
							.getCart() + "'.");
			cartModel.setPaymentInfo(null);
			getModelService().save(cartModel);
			commerceCartModification.setPaymentMethod(null);
			return;
		}

		CreditCardPaymentInfoModel paymentInfoModelForUpdate = null;
		final List<CreditCardPaymentInfoModel> paymentInfoModelList = getCustomerAccountService()
				.getCreditCardPaymentInfos(customer, false);

		for (CreditCardPaymentInfoModel paymentInfoModel : paymentInfoModelList)
		{
			if (paymentInfoModel.getCode().equals(commerceCartParameter.getPaymentMethodId()))
			{
				paymentInfoModelForUpdate = paymentInfoModel;
			}
		}

		if (paymentInfoModelForUpdate == null)
		{
			throw new CommerceCartModificationException(
					"Can't update cart " + cartModel.getCode() + " with payment method " + commerceCartParameter.getPaymentMethodId()
							+ ".");
		}

		cartModel.setPaymentInfo(paymentInfoModelForUpdate);
		getModelService().save(cartModel);

		commerceCartModification.setPaymentMethod(paymentInfoModelForUpdate);
	}

	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
