/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.order.TmaCommerceCartResourceService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation for {@link TmaCommerceCartResourceService} interface.
 *
 * @since 1911
 */
public class DefaultTmaCommerceCartResourceService implements TmaCommerceCartResourceService
{
	private ModelService modelService;
	private List<TmaAbstractOrderResourceStrategy> tmaAbstractOrderResourceStrategies;

	public DefaultTmaCommerceCartResourceService(ModelService modelService,
			List<TmaAbstractOrderResourceStrategy> tmaAbstractOrderResourceStrategies)
	{
		this.modelService = modelService;
		this.tmaAbstractOrderResourceStrategies = tmaAbstractOrderResourceStrategies;
	}

	@Override
	public List<TmaCartValidationResult> validateResources(final CommerceCartParameter parameter)
	{
		List<TmaCartValidationResult> resultList = new ArrayList<>();
		for (final TmaAbstractOrderResourceStrategy resourceStrategy : getTmaAbstractOrderResourceStrategies())
		{
			resultList.add(resourceStrategy.validateResource(parameter));
		}
		return resultList;
	}

	@Override
	public void updateResources(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
			throws CommerceCartModificationException
	{
		for (final TmaAbstractOrderResourceStrategy resourceStrategy : getTmaAbstractOrderResourceStrategies())
		{
			resourceStrategy.updateResource(commerceCartParameter, commerceCartModification);
		}
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected List<TmaAbstractOrderResourceStrategy> getTmaAbstractOrderResourceStrategies()
	{
		return tmaAbstractOrderResourceStrategies;
	}
}
