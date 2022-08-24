/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;


import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;


/**
 * Interceptor to bypass {@link de.hybris.platform.subscriptionservices.interceptor.impl.SubscriptionPricePlanValidateInterceptor}
 * So that we are able to create a Simple Product Offering in one request	.
 *
 * @since 2003
 */
public class TmaSubscriptionPricePlanValidateInterceptor implements ValidateInterceptor<SubscriptionPricePlanModel>
{
	@Override public void onValidate(SubscriptionPricePlanModel pricePlanModel, InterceptorContext interceptorContext)
			throws InterceptorException
	{
			// do nothing
	}
}
