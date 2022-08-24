/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.validator;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TmaIdentificationWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;


/**
 * Test class for {@link TmaIdentificationValidator}.
 *
 * @since 1911
 *
 */

@UnitTest
public class TmaIdentificationValidatorTest
{

	private static final String INVALID_CODE = "111$$11";
	private static final String VALID_CODE = "11111";
	private static final String INVALID_TYPE = "DL";
	private TmaIdentificationType identificationType;
	private TmaIdentificationValidator tmaIdentificationValidator;

	@Mock
	private EnumerationService enumerationService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		tmaIdentificationValidator = new TmaIdentificationValidator(enumerationService);
		identificationType = TmaIdentificationType.DRIVING_LICENSE;
	}

	@Test
	public void testIdentificationIsValid()
	{
		given(enumerationService.getEnumerationValue(TmaIdentificationType._TYPECODE,
				TmaIdentificationType.DRIVING_LICENSE.toString())).willReturn(identificationType);
		final Errors errors = new BeanPropertyBindingResult(identificationType, "identificationType");
		final List<TmaIdentificationWsDTO> identificationWsDTOs = new ArrayList<>();
		final TmaIdentificationWsDTO identificationWsDTO = new TmaIdentificationWsDTO();
		identificationWsDTO.setIdentificationType(identificationType.getCode());
		identificationWsDTO.setIdentificationNumber(VALID_CODE);
		identificationWsDTOs.add(identificationWsDTO);
		tmaIdentificationValidator.validate(identificationWsDTOs, errors);
		assertEquals(0, errors.getErrorCount());
	}

	@Test
	public void testIdentificationTypeIsInvalid()
	{
		final List<TmaIdentificationWsDTO> identificationWsDTOs = new ArrayList<>();
		final TmaIdentificationWsDTO identificationWsDTO = new TmaIdentificationWsDTO();
		identificationWsDTO.setIdentificationType(INVALID_TYPE);
		identificationWsDTO.setIdentificationNumber(VALID_CODE);
		identificationWsDTOs.add(identificationWsDTO);
		given(enumerationService.getEnumerationValue(TmaIdentificationType._TYPECODE, INVALID_TYPE))
				.willThrow(UnknownIdentifierException.class);
		final Errors errors = new BeanPropertyBindingResult(identificationType, "identificationType");
		tmaIdentificationValidator.validate(identificationWsDTOs, errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testIdentificationTypeIsEmpty()
	{
		final List<TmaIdentificationWsDTO> identificationWsDTOs = new ArrayList<>();
		final TmaIdentificationWsDTO identificationWsDTO = new TmaIdentificationWsDTO();
		identificationWsDTO.setIdentificationNumber(VALID_CODE);
		identificationWsDTOs.add(identificationWsDTO);
		final Errors errors = new BeanPropertyBindingResult(identificationType, "identificationType");
		tmaIdentificationValidator.validate(identificationWsDTOs, errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testIdentificationNumberIsInvalid()
	{
		given(enumerationService.getEnumerationValue(TmaIdentificationType._TYPECODE,
				TmaIdentificationType.DRIVING_LICENSE.toString())).willReturn(identificationType);
		final List<TmaIdentificationWsDTO> identificationWsDTOs = new ArrayList<>();
		final TmaIdentificationWsDTO identificationWsDTO = new TmaIdentificationWsDTO();
		identificationWsDTO.setIdentificationType(identificationType.getCode());
		identificationWsDTO.setIdentificationNumber(INVALID_CODE);
		identificationWsDTOs.add(identificationWsDTO);
		final Errors errors = new BeanPropertyBindingResult(identificationType, "identificationType");
		tmaIdentificationValidator.validate(identificationWsDTOs, errors);
		assertEquals(1, errors.getErrorCount());
	}

	@Test
	public void testDuplicateIdentifications()
	{
		given(enumerationService.getEnumerationValue(TmaIdentificationType._TYPECODE,
				TmaIdentificationType.DRIVING_LICENSE.toString())).willReturn(identificationType);
		final List<TmaIdentificationWsDTO> identificationWsDTOs = new ArrayList<>();
		final TmaIdentificationWsDTO identificationWsDTO = new TmaIdentificationWsDTO();
		identificationWsDTO.setIdentificationType(identificationType.getCode());
		identificationWsDTO.setIdentificationNumber(VALID_CODE);
		identificationWsDTOs.add(identificationWsDTO);
		final TmaIdentificationWsDTO duplicateIdentificationWsDTO = new TmaIdentificationWsDTO();
		duplicateIdentificationWsDTO.setIdentificationType(identificationType.getCode());
		duplicateIdentificationWsDTO.setIdentificationNumber(VALID_CODE);
		identificationWsDTOs.add(duplicateIdentificationWsDTO);
		final Errors errors = new BeanPropertyBindingResult(identificationType, "identificationType");
		tmaIdentificationValidator.validate(identificationWsDTOs, errors);
		assertEquals(1, errors.getErrorCount());
	}
}
