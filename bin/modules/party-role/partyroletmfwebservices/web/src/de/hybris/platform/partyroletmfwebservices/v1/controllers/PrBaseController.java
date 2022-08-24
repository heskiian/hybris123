/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.controllers;

import de.hybris.platform.partyroletmfwebservices.v1.dto.Error;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Abstract Base Controller defining common methods and fields to be used by other API controllers.
 *
 * @since 2108
 */
public class PrBaseController
{
	private static final Logger LOG = Logger.getLogger(PrBaseController.class);
	static final String ERROR = "error";
	static final String BAD_REQUEST = "Error occurred due to bad request";

	@Resource(name = "objectMapper")
	private ObjectMapper objectMapper;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@PostConstruct
	public void init()
	{
		objectMapper.setDateFormat(new SimpleDateFormat(Config.getParameter("partyroletmfwebservices.simpleDateFormat")));
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ ModelNotFoundException.class })
	public Error handleModelNotFoundException(final Exception ex)
	{
		LOG.info("Handling Exception for this request - " + ex.getClass().getSimpleName() + " - " + sanitize(ex.getMessage()));
		if (LOG.isDebugEnabled())
		{

			LOG.debug(ex);

		}
		return handleInternalError(ex.getMessage());
	}

	@SuppressWarnings({ "unchecked" })
	protected ResponseEntity getUnsuccessfulResponse(String errorMessage, Exception e, HttpStatus httpStatus)
	{
		LOG.error(errorMessage, e);
		final Error error = new Error();
		error.setCode(ERROR);
		error.setMessage(sanitize(e.getMessage()));
		return new ResponseEntity(error, httpStatus);
	}

	@SuppressWarnings("WeakerAccess")
	protected Error handleInternalError(final String message)
	{
		final Error error = new Error();
		error.setCode(ERROR);
		error.setMessage(sanitize(message));
		return error;
	}

	@SuppressWarnings("WeakerAccess")
	protected String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	protected ObjectMapper getObjectMapper()
	{
		return objectMapper;
	}
}
