/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Interceptor to validate that the identificationNumber of {@linkplain TmaIdentificationModel} does not contain any
 * special characters.
 *
 * @since 1911
 */
public class TmaIdentificationValidateInterceptor implements ValidateInterceptor<TmaIdentificationModel>
{
	private final static String REGEXP = "[A-Za-z0-9]*";

	@Override
	public void onValidate(final TmaIdentificationModel identificationModel, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		final Pattern pattern = Pattern.compile(REGEXP);
		final Matcher matcher = pattern.matcher(identificationModel.getIdentificationNumber());
		if (!matcher.matches())
		{
			throw new InterceptorException(
					MessageFormat.format("The identification number of {0} instance cannot contain special characters.",
							TmaIdentificationModel._TYPECODE,
							this));
		}
	}

}
