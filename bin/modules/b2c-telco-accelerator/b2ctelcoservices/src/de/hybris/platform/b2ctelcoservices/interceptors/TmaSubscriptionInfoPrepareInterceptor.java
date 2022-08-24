/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Prepare interceptor that generates a unique identifier for the {@link TmaCartSubscriptionInfoModel} entities.
 *
 * @since 1911
 */
public class TmaSubscriptionInfoPrepareInterceptor implements PrepareInterceptor<TmaCartSubscriptionInfoModel>
{
	private KeyGenerator keyGenerator;

	public TmaSubscriptionInfoPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final TmaCartSubscriptionInfoModel subscriptionInfoModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(subscriptionInfoModel.getId()))
		{
			subscriptionInfoModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
