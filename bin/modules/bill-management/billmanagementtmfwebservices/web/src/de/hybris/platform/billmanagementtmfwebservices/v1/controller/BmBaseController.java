/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.controller;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Error;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
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
public class BmBaseController
{
	private static final Logger LOG = Logger.getLogger(BmBaseController.class);
	static final String ERROR = "error";
	static final String BAD_REQUEST = "Error occurred due to bad request";
	static final String NOT_FOUND = "Resource not found";

	@Resource(name = "objectMapper")
	private ObjectMapper objectMapper;

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;

	@PostConstruct
	public void init()
	{
		objectMapper
				.setDateFormat(new SimpleDateFormat(Config.getParameter("billmanagementtmfwebservices.simpleDateFormat")));
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ ModelNotFoundException.class })
	public Error handleModelNotFoundException(final Exception ex)
	{
		LOG.error(ex.getMessage());
		final Error error = new Error();
		error.setCode(NOT_FOUND);
		error.setMessage(sanitize(ex.getMessage()));
		return error;
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
