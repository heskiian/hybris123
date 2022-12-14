/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.interceptor.impl;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscriptionservices.model.UsageChargeModel;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;


public class UsageChargeIdPrepareInterceptor implements PrepareInterceptor
{

	private KeyGenerator usageChargeIDGenerator;

	@Override
	public void onPrepare(@Nonnull final Object model, @Nonnull final InterceptorContext ctx)
			throws InterceptorException
	{
		if (model instanceof UsageChargeModel)
		{
			final UsageChargeModel usageCharge = (UsageChargeModel) model;
			final String id = usageCharge.getId();
			if (StringUtils.isEmpty(id))
			{
				usageCharge.setId(usageChargeIDGenerator.generate().toString());
			}
		}
	}

	@Required
	public void setUsageChargeIDGenerator(final KeyGenerator usageChargeIDGenerator)
	{
		this.usageChargeIDGenerator = usageChargeIDGenerator;
	}

}
