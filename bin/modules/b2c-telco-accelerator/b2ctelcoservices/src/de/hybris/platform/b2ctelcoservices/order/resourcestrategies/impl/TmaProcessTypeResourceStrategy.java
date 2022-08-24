/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;


/**
 * Process Type resource strategy responsible for updating process type for cart.
 *
 * @since 2007
 */
public class TmaProcessTypeResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private static final Logger LOG = Logger.getLogger(TmaProcessTypeResourceStrategy.class.getName());
	private final TmaCustomerInventoryService tmaCustomerInventoryService;
	private final ModelService modelService;
	private final EnumerationService enumerationService;

	public TmaProcessTypeResourceStrategy(final TmaCustomerInventoryService tmaCustomerInventoryService,
			final ModelService modelService,
			final EnumerationService enumerationService)
	{
		this.tmaCustomerInventoryService = tmaCustomerInventoryService;
		this.modelService = modelService;
		this.enumerationService = enumerationService;
	}
	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		TmaProcessType processType = null;
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);

		if (ObjectUtils.isEmpty(parameter.getProcessType()))
		{
			return result;
		}

		try
		{
			processType = getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE,
					parameter.getProcessType());
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error("Invalid ProcessType", e);
			result.setValid(false);
			result.setMessage(String.format("Invalid processType: %s", parameter.getProcessType()));
			return result;
		}
		final Set<TmaProcessType> eligibleProcessTypes = getTmaCustomerInventoryService().retrieveProcesses();
		if (CollectionUtils.isNotEmpty(eligibleProcessTypes) && !eligibleProcessTypes.contains(processType))
		{
			result.setValid(false);
			result.setMessage(
					String.format("Process type %s %s", parameter.getProcessType(), "is not eligible for the given user"));
			return result;
		}
		return result;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{

		if (ObjectUtils.isEmpty(commerceCartParameter.getProcessType()))
		{
			return;
		}

		final AbstractOrderEntryModel orderEntry = commerceCartModification.getEntry();
		final TmaProcessType oldProcessType = orderEntry.getProcessType();
		final TmaProcessType newProcessType = getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE,
				commerceCartParameter.getProcessType());
		if (!ObjectUtils.isEmpty(oldProcessType)
				&& StringUtils.equalsIgnoreCase(newProcessType.getCode(), oldProcessType.getCode()))
		{
			return;
		}

		orderEntry.setProcessType(newProcessType);
		getModelService().save(orderEntry);
		getModelService().refresh(orderEntry);
		commerceCartModification.setEntry(orderEntry);
	}

	protected TmaCustomerInventoryService getTmaCustomerInventoryService()
	{
		return tmaCustomerInventoryService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}


}
