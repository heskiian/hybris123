/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;


/**
 * Implementation of {@link TmaAbstractOrderResourceStrategy} which updates the serviceProvider on the order entry
 *
 * @since 2003
 */
public class TmaServiceProviderResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private final ModelService modelService;

	public TmaServiceProviderResourceStrategy(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);
		return result;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException
	{
		if (StringUtils.isEmpty(commerceCartParameter.getServiceProvider()))
		{
			return;
		}
		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();
		abstractOrderEntryModel.setServiceProvider(commerceCartParameter.getServiceProvider());
		getModelService().save(abstractOrderEntryModel);
		getModelService().refresh(abstractOrderEntryModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);

	}

	protected ModelService getModelService()
	{
		return modelService;
	}

}
