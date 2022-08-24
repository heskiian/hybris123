/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcotmfwebservices.data.TmaErrorData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * ControllerAdvise to handle MethodArgumentNotValid exception for TMA WebServices
 *
 * @since 2003
 */
@ControllerAdvice
public class TmaRestControllerAdvice
{

	@Resource
	private MessageSource messageSource;
	@Resource
	private CommerceCommonI18NService commerceCommonI18NService;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public TmaErrorData handleInvalidRequest(final MethodArgumentNotValidException exception)
	{
		final TmaErrorData error = new TmaErrorData();
		error.setMessage(getMessage(exception));
		error.setStatus(HttpStatus.BAD_REQUEST.toString());
		return error;
	}

	protected String getMessage(final MethodArgumentNotValidException ex)
	{
		return messageSource.getMessage(getErrorCode(ex), null, commerceCommonI18NService.getCurrentLocale());
	}

	private String getErrorCode(final MethodArgumentNotValidException ex)
	{
		return ex.getBindingResult().getAllErrors().stream().findFirst().get().getCode();
	}

}