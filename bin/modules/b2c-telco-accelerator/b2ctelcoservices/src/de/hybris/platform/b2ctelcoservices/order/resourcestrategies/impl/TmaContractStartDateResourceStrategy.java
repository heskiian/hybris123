/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resource strategy implementation. Validates and updates contract start date.
 *
 * @since 2003
 */
public class TmaContractStartDateResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private ModelService modelService;
	private CartService cartService;
	private TmaSubscriptionTermService subscriptionTermService;

	public TmaContractStartDateResourceStrategy(final ModelService modelService, final CartService cartService,
			final TmaSubscriptionTermService subscriptionTermService)
	{
		this.modelService = modelService;
		this.cartService = cartService;
		this.subscriptionTermService = subscriptionTermService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter commerceCartParameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(commerceCartParameter);

		if (commerceCartParameter.getContractStartDate() == null)
		{
			return result;
		}

		return result;
	}

	@Override
	public void updateResource(CommerceCartParameter commerceCartParameter,
			CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		if (commerceCartParameter.getContractStartDate() == null)
		{
			return;
		}

		if (!isContractStartDateUpdateNeeded(commerceCartParameter, commerceCartModification))
		{
			return;
		}

		if (!isContractStartDateUpdatePossible(commerceCartParameter))
		{
			throw new CommerceCartModificationException(
					"The entry must be service plan and not have no contract as subscription term!");
		}

		updateContractStartDate(commerceCartParameter, commerceCartModification);
	}

	private void updateContractStartDate(CommerceCartParameter commerceCartParameter,
			CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = commerceCartParameter.getCart();
		final AbstractOrderEntryModel entryToUpdate = commerceCartModification.getEntry();

		if (entryToUpdate == null)
		{
			throw new CommerceCartModificationException("Unknown entry number");
		}

		final Date contractStartDate = commerceCartParameter.getContractStartDate();
		entryToUpdate.setContractStartDate(contractStartDate);

		getModelService().save(entryToUpdate);
		getModelService().refresh(cartModel);

		commerceCartModification.setEntry(entryToUpdate);
	}

	private boolean isContractStartDateUpdateNeeded(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		return commerceCartModification.getEntry().getContractStartDate() == null
				|| commerceCartModification.getEntry().getContractStartDate().compareTo(commerceCartParameter.getContractStartDate())
				!= 0;
	}

	private boolean isContractStartDateUpdatePossible(CommerceCartParameter commerceCartParameter)
	{

		final TmaProductOfferingModel tmaProductModel = (TmaProductOfferingModel) commerceCartParameter.getProduct();

		if (tmaProductModel == null || tmaProductModel.getEurope1Prices().isEmpty()
				|| tmaProductModel.getProductSpecification() == null
				|| tmaProductModel.getProductSpecification().getProductSpecificationTypes() == null)
		{
			return false;
		}

		return tmaProductModel.getEurope1Prices().stream().anyMatch(priceRow ->
				CollectionUtils.isNotEmpty(priceRow.getSubscriptionTerms()) && priceRow.getSubscriptionTerms().stream()
						.anyMatch(subsTerm -> {
							final SubscriptionTermModel defaultSubscriptionTerm = getSubscriptionTermService()
									.getDefaultSubscriptionTerm();
							if (defaultSubscriptionTerm != null)
							{
								return !subsTerm.getId().equals(defaultSubscriptionTerm.getId());
							}
							return true;
						}));
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	protected TmaSubscriptionTermService getSubscriptionTermService()
	{
		return subscriptionTermService;
	}
}
