/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.controllers;

import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Error;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * Abstract Base Controller defining common methods and fields to be used by other API controllers.
 *
 * @since 2105
 */
public class BaBaseController
{
	private static final Logger LOG = Logger.getLogger(BaBaseController.class);
	private static final String ERROR = "error";
	private static final String SEMICOLON = ";";

	@Resource(name = "objectMapper")
	private ObjectMapper objectMapper;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@PostConstruct
	public void init()
	{
		objectMapper.setDateFormat(new SimpleDateFormat(Config.getParameter("billingaccounttmfwebservices.simpleDateFormat")));
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
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
	protected ResponseEntity getUnsuccessfulResponse(final String message, final HttpStatus httpStatus)
	{
		final Error error = new Error();
		error.setCode(ERROR);
		error.setMessage(sanitize(message));
		return new ResponseEntity(error, httpStatus);
	}

	protected String validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);

		if (!errors.hasErrors())
		{
			return null;
		}

		final StringBuilder errorsMessage = new StringBuilder();
		for (ObjectError objectError : errors.getAllErrors())
		{
			errorsMessage.append(buildErrorMessage(objectError));
			if (errors.getAllErrors().indexOf(objectError) < errors.getAllErrors().size() - 1)
			{
				errorsMessage.append(SEMICOLON);
			}
		}
		return errorsMessage.toString();
	}

	private String buildErrorMessage(final ObjectError objectError)
	{
		final StringBuilder errorMessage = new StringBuilder();
		if (objectError instanceof FieldError)
		{
			errorMessage.append(((FieldError) objectError).getField() + ":");
		}
		errorMessage.append(getLocalizedMessage(objectError.getCode(), objectError.getArguments()));
		return errorMessage.toString();
	}

	private String getLocalizedMessage(final String message, final Object[] args)
	{
		return getMessageSource().getMessage(message, args, getI18nService().getCurrentLocale());
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
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

	/**
	 * Checks if the user has given role.
	 *
	 * @param role
	 * 		The role is provided as string
	 * @return False if logged in user doesn't have given role, otherwise true.
	 */
	protected boolean hasRole(final String role, final Authentication authentication)
	{
		for (final GrantedAuthority ga : ((OAuth2Authentication) authentication).getOAuth2Request().getAuthorities())
		{
			if (ga.getAuthority().equals(role))
			{
				return true;
			}
		}
		return false;
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
