/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates if the given processType is valid and is eligible for requested user
 *
 * @since 1907
 */
public class TmaProcessTypeValidator implements Validator
{
	private static final String PROCESS_TYPE_NOT_VALID = "tmaProcessType.invalidValue";
	private static final String GIVEN_PROCESS_TYPE_INVALID_FOR_USER = "tmaProcessType.invalidForCustomer";
	private TmaCustomerFacade customerFacade;
	private EnumerationService enumerationService;

	public TmaProcessTypeValidator(final TmaCustomerFacade customerFacade, final EnumerationService enumerationService)
	{
		this.customerFacade = customerFacade;
		this.enumerationService = enumerationService;
	}


	@Override
	public boolean supports(final Class<?> arg0)
	{
		return String.class.equals(arg0);
	}

	/**
	 * Validates if the given processType is valid 
	 * Validates if given processType is eligible for requested user
	 * @param target
	 *           the processType
	 * @param errors
	 *           the list of errors
	 */
	@Override
	public void validate(final Object target, final Errors errors)
	{
		final String tmaprocessType = (String) target;
		TmaProcessType processType = null;
		try
		{
			processType = getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE, tmaprocessType);
		}
		catch (final UnknownIdentifierException ex)
		{
			errors.reject(PROCESS_TYPE_NOT_VALID, new String[]
			{ tmaprocessType }, "[{0}] is not a valid ProcessType");
		}
		final Set<TmaProcessType> eligibleProcessTypes = getCustomerFacade().retrieveEligibleProcessTypes();
		if (CollectionUtils.isNotEmpty(eligibleProcessTypes) && !ObjectUtils.isEmpty(processType)
				&& !eligibleProcessTypes.contains(processType))
		{
			errors.reject(GIVEN_PROCESS_TYPE_INVALID_FOR_USER, new String[]
			{ tmaprocessType }, "Given ProcessType [{0}] is invalid for user");
		}
	}

	protected TmaCustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	public void setCustomerFacade(final TmaCustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

}
