/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang.StringUtils;


/**
 * Implementation of {@link TmaAbstractOrderResourceStrategy} which updates the appointment on the order entry
 *
 * @since 1911
 */
public class TmaAppointmentResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private ModelService modelService;

	public TmaAppointmentResourceStrategy(ModelService modelService)
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
	{
		if (StringUtils.isEmpty(commerceCartParameter.getAppointmentId()))
		{
			return;
		}

		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();

		abstractOrderEntryModel.setAppointmentReference(commerceCartParameter.getAppointmentId());
		getModelService().save(abstractOrderEntryModel);

		commerceCartModification.setEntry(abstractOrderEntryModel);
		commerceCartModification.setAppointmentId(commerceCartParameter.getAppointmentId());
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
