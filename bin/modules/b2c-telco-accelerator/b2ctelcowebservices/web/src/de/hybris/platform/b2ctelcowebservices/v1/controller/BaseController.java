/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;


/**
 * Abstract Base Controller defining common methods and fields to be used by other API controllers.
 *
 * @since 6.6
 */
public abstract class BaseController
{
	private static final Logger LOG = Logger.getLogger(BaseController.class);

	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	protected static final String FULL_FIELD_SET = FieldSetLevelHelper.FULL_LEVEL;

	private DataMapper dataMapper;

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(
	{ ModelNotFoundException.class })
	public ErrorListWsDTO handleModelNotFoundException(final Exception ex)
	{
		LOG.info("Handling Exception for this request - " + ex.getClass().getSimpleName() + " - " + sanitize(ex.getMessage()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug(ex);
		}

		return handleErrorInternal(UnknownIdentifierException.class.getSimpleName(), ex.getMessage());
	}

	protected void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}

	protected ErrorListWsDTO handleErrorInternal(final String type, final String message)
	{
		final ErrorListWsDTO errorListDto = new ErrorListWsDTO();
		final ErrorWsDTO error = new ErrorWsDTO();
		error.setType(type.replace("Exception", "Error"));
		error.setMessage(sanitize(message));
		errorListDto.setErrors(Lists.newArrayList(error));
		return errorListDto;
	}


	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	@Resource(name = "dataMapper")
	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}
}
