/*
 *  Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaSubscribedProductAction;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * Resource strategy implementation. Validates and updates the subscribed product and action.
 *
 * @since 1911
 */
public class TmaSubscribedProductResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private static final String INVALID_CUSTOMER = "Invalid customer";
	private static final String INVALID_PRODUCT_AND_OFFERING = "Invalid offering for the selected subscribed product";

	private TmaCustomerInventoryService customerInventoryService;
	private ModelService modelService;

	public TmaSubscribedProductResourceStrategy(final TmaCustomerInventoryService customerInventoryService,
			final ModelService modelService)
	{
		this.customerInventoryService = customerInventoryService;
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);

		if (!(parameter.getProduct() instanceof TmaProductOfferingModel))
		{
			return result;
		}

		final TmaSubscribedProductAction action = parameter.getAction();
		if (action == null)
		{
			return result;
		}

		final TmaProductOfferingModel productOffering = (TmaProductOfferingModel) parameter.getProduct();

		if (TmaSubscribedProductAction.UPDATE.equals(action))
		{
			final CustomerModel customer = (CustomerModel) parameter.getUser();
			if (customer == null)
			{
				result.setMessage(INVALID_CUSTOMER);
				result.setValid(false);
				return result;
			}
			final TmaSubscribedProductModel subscribedProduct = parameter.getSubscribedProduct();
			if (!getCustomerInventoryService().canReplaceSubscribedProductWithOffering(subscribedProduct, productOffering))
			{
				result.setMessage(INVALID_PRODUCT_AND_OFFERING);
				result.setValid(false);
				return result;
			}
		}

		if (TmaSubscribedProductAction.KEEP.equals(action))
		{
			final TmaSubscribedProductModel subscribedProduct = parameter.getSubscribedProduct();
			if (subscribedProduct != null && productOffering != null && !productOffering.getCode()
					.equals(subscribedProduct.getProductCode()))
			{
				result.setMessage(INVALID_PRODUCT_AND_OFFERING);
				result.setValid(false);
				return result;
			}
		}
		return result;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		if (commerceCartParameter.getAction() == null)
		{
			return;
		}

		final TmaSubscribedProductModel subscribedProduct = commerceCartParameter.getSubscribedProduct();
		final String subscribedProductId = subscribedProduct != null ? subscribedProduct.getId() : null;
		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();

		final TmaCartSubscriptionInfoModel subscriptionInfoModel = abstractOrderEntryModel.getSubscriptionInfo();
		final TmaSubscribedProductAction subscribedProductAction = commerceCartParameter.getAction();

		final TmaCartSubscriptionInfoModel updatedSubscriptionInfo = getSubscriptionInfoWithIdAndAction(subscribedProductId,
				subscriptionInfoModel, subscribedProductAction);
		abstractOrderEntryModel.setSubscriptionInfo(updatedSubscriptionInfo);

		getModelService().save(updatedSubscriptionInfo);
		getModelService().save(abstractOrderEntryModel);

		commerceCartModification.setEntry(abstractOrderEntryModel);
		commerceCartModification.setSubscribedProductId(subscribedProductId);
		commerceCartModification.setSubscribedProductAction(subscribedProductAction);
	}

	protected TmaCartSubscriptionInfoModel getSubscriptionInfoWithIdAndAction(final String subscribedProductId,
			TmaCartSubscriptionInfoModel subscriptionInfoModel, final TmaSubscribedProductAction subscribedProductAction)
	{
		if (subscriptionInfoModel == null)
		{
			subscriptionInfoModel = getModelService().create(TmaCartSubscriptionInfoModel.class);
		}

		subscriptionInfoModel.setSubscribedProductId(subscribedProductId);
		subscriptionInfoModel.setSubscribedProductAction(subscribedProductAction);
		return subscriptionInfoModel;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}
}
