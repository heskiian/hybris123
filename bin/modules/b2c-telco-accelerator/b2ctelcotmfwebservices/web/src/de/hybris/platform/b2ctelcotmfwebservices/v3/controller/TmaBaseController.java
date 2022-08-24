/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.controller;

import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ErrorRepresentation;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.security.core.server.csi.XSSEncoder;


/**
 * Abstract Base Controller defining common methods and fields to be used by other API controllers.
 *
 * @since 2007
 */
public abstract class TmaBaseController
{
	private static final Logger LOG = Logger.getLogger(TmaBaseController.class);
	static final String BAD_REQUEST = "Error occurred due to bad request";
	static final String NOT_FOUND = "Resource not found";
	static final String INTERNAL_SERVER_ERROR = "Internal server error";

	@Resource(name = "objectMapper")
	private ObjectMapper objectMapper;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@PostConstruct
	public void init()
	{
		objectMapper.setDateFormat(new SimpleDateFormat(Config.getParameter("b2ctelcotmfwebservices.simpleDateFormat")));
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
		error.setCode(TmaApiResponseMessage.ERROR);
		error.setMessage(sanitize(e.getMessage()));
		return new ResponseEntity(error, httpStatus);
	}

	@SuppressWarnings(
			{ "unchecked", "WeakerAccess" })
	protected ResponseEntity getUnsuccessfulResponseWithErrorRepresentation(final String errorMessage, final Exception e,
			final int errorCode, final String reason, final HttpStatus httpStatus)
	{
		final ErrorRepresentation errorRepresentation = new ErrorRepresentation();
		errorRepresentation.setCode(errorCode);
		errorRepresentation.setReason(reason);
		if (e != null)
		{
			LOG.error(errorMessage, e);
			errorRepresentation.setMessage(sanitize(e.getMessage()));
		}

		return new ResponseEntity(errorRepresentation, httpStatus);
	}

	@SuppressWarnings("WeakerAccess")
	protected Error handleInternalError(final String message)
	{
		final Error error = new Error();
		error.setCode(TmaApiResponseMessage.ERROR);
		error.setMessage(sanitize(message));
		return error;
	}

	@SuppressWarnings("WeakerAccess")
	protected String getRequestUrl(final HttpServletRequest request)
	{
		if (null != request && null != request.getRequestURL())
		{
			final String queryString = sanitizeQueryString(request.getQueryString());
			String requestURL = request.getRequestURL().toString();
			requestURL = StringUtils.isNotEmpty(queryString) ? requestURL + "?" + queryString : requestURL;
			return encodeUrl(requestURL);
		}
		return StringUtils.EMPTY;
	}

	protected static String encodeUrl(final String url)
	{
		try
		{
			return XSSEncoder.encodeURL(url);
		}
		catch (final UnsupportedEncodingException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return url;
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected String getQueryStringWithoutOffsetAndLimit(final HttpServletRequest request)
	{
		final String queryString = request.getQueryString();
		if (queryString == null)
		{
			return "";
		}

		final String[] queryStrings = queryString.split("&");
		final StringBuilder queryWithoutOffsetAndLimit = new StringBuilder();

		for (final String query : queryStrings)
		{
			if (!query.contains("offset") && !query.contains("limit"))
			{
				queryWithoutOffsetAndLimit.append(query);
				queryWithoutOffsetAndLimit.append("&");
			}
		}

		return queryWithoutOffsetAndLimit.toString();
	}

	@SuppressWarnings("WeakerAccess")
	protected ResponseEntity<Object> handleTmaApiErrorInternal(final Exception e)
	{
		final TmaErrorRepresentationWsDto error = new TmaErrorRepresentationWsDto();
		error.setCode(TmaApiResponseMessage.ERROR);
		error.setMessage(YSanitizer.sanitize(e.getMessage()));
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@SuppressWarnings("WeakerAccess")
	protected String sanitizeQueryString(final String queryString)
	{
		// clean input
		String output = StringUtils.defaultString(queryString).trim();
		// remove CRLF injection
		output = output.replaceAll("(\\r\\n|\\r|\\n|%0D|%0d|%0A|%0a)+", "");
		return output;
	}

	protected String filter(final String value)
	{
		if (value == null)
		{
			return null;
		}
		String sanitized = value;
		// Simple characters
		sanitized = sanitized.replace("<", "&lt;").replace(">", "&gt;");
		sanitized = sanitized.replace("(", "&#40;").replace(")", "&#41;");
		sanitized = sanitized.replace("'", "&#39;");
		// RegEx pattern
		sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
		sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		return sanitized;
	}

	String validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);

		if (!errors.hasErrors())
		{
			return null;
		}

		StringBuilder errorsMessage = new StringBuilder();

		for (ObjectError objectError : errors.getAllErrors())
		{
			errorsMessage.append(objectError.toString() + "\n");
		}

		return errorsMessage.toString();
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
