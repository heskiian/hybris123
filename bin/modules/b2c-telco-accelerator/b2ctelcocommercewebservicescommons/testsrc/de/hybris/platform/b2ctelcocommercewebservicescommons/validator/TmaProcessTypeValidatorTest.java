/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


/**
 * test class for TmaProcessTypeValidator that validates if the given processType is valid and is eligible for requested
 * user
 * 
 * Test class for {@link TmaProcessTypeValidator}.
 *
 * @since 1907
 */
@UnitTest
public class TmaProcessTypeValidatorTest
{
	private static final String CODE_INVALID = "abcd";
	private TmaProcessTypeValidator tmaProcessTypeValidator;
	TmaProcessType processType;
	private Errors errors;
	Set<TmaProcessType> tmaProcessTypes;
	@Mock
	private TmaCustomerFacade customerFacade;
	@Mock
	private EnumerationService enumerationService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		tmaProcessTypeValidator = new TmaProcessTypeValidator(customerFacade,enumerationService);
		tmaProcessTypes = new HashSet<TmaProcessType>();
	}

	@Test
	public void testTmaProcessTypeIsValid()
	{
		processType = TmaProcessType.RETENTION;
		tmaProcessTypes.add(TmaProcessType.RETENTION);
		tmaProcessTypes.add(TmaProcessType.DEVICE_ONLY);
		given(enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, "retention")).willReturn(processType);
		given(customerFacade.retrieveEligibleProcessTypes()).willReturn(tmaProcessTypes);
		errors = new BeanPropertyBindingResult(processType, "processType");
		tmaProcessTypeValidator.validate("RETENTION", errors);
		assertEquals(0, errors.getErrorCount());
	}

	@Test
	public void testTmaProcessTypeInValidForCustomer()
	{
		processType = TmaProcessType.TARIFF_CHANGE;
		tmaProcessTypes.add(TmaProcessType.RETENTION);
		tmaProcessTypes.add(TmaProcessType.DEVICE_ONLY);
		given(enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, "TARIFF_CHANGE")).willReturn(processType);
		given(customerFacade.retrieveEligibleProcessTypes()).willReturn(tmaProcessTypes);
		errors = new BeanPropertyBindingResult(processType, "processType");
		tmaProcessTypeValidator.validate("TARIFF_CHANGE", errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testTmaProcessTypeInValid()
	{
		tmaProcessTypes.add(TmaProcessType.RETENTION);
		tmaProcessTypes.add(TmaProcessType.DEVICE_ONLY);
		given(enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, CODE_INVALID))
				.willThrow(UnknownIdentifierException.class);
		given(customerFacade.retrieveEligibleProcessTypes()).willReturn(tmaProcessTypes);
		errors = new BeanPropertyBindingResult(processType, "processType");
		tmaProcessTypeValidator.validate(CODE_INVALID, errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testTmaProcessTypeNull()
	{
		processType = TmaProcessType.RETENTION;
		tmaProcessTypes.add(TmaProcessType.RETENTION);
		tmaProcessTypes.add(TmaProcessType.DEVICE_ONLY);
		given(enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, "retention")).willReturn(null);
		given(customerFacade.retrieveEligibleProcessTypes()).willReturn(tmaProcessTypes);
		errors = new BeanPropertyBindingResult(processType, "processType");
		tmaProcessTypeValidator.validate("RETENTION", errors);
		assertEquals(0, errors.getErrorCount());
	}

	@Test
	public void testEligibleProcessTypesNull()
	{
		processType = TmaProcessType.RETENTION;
		given(enumerationService.getEnumerationValue(TmaProcessType._TYPECODE, "retention")).willReturn(processType);
		given(customerFacade.retrieveEligibleProcessTypes()).willReturn(tmaProcessTypes);
		errors = new BeanPropertyBindingResult(processType, "processType");
		tmaProcessTypeValidator.validate("RETENTION", errors);
		assertEquals(0, errors.getErrorCount());
	}
}
