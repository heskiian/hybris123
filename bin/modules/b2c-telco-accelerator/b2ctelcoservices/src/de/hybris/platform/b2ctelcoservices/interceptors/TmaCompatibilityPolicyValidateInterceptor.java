/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.util.Date;


/**
 * Interceptor to validate the onlineFrom and onlineTo dates configured for a {@link TmaCompatibilityPolicyModel}.
 *
 * @since 1810
 */
public class TmaCompatibilityPolicyValidateInterceptor implements ValidateInterceptor<TmaCompatibilityPolicyModel>
{
	@Override
	public void onValidate(TmaCompatibilityPolicyModel tmaCompatibilityPolicyModel,
			InterceptorContext interceptorContext) throws InterceptorException
	{
		final Date onlineFrom = tmaCompatibilityPolicyModel.getOnlineFrom();
		final Date onlineTo = tmaCompatibilityPolicyModel.getOnlineTo();

		if (onlineFrom == null || onlineTo == null)
		{
			return;
		}

		if (onlineFrom.after(onlineTo))
		{
			throw new InterceptorException("OnlineFrom cannot be after OnlineTo date.", this);
		}

	}
}
