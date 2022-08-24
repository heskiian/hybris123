/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang3.StringUtils;


/**
 * Interceptor to generate unique id for the newly created {@link TmaAbstractOrderPriceModel}s.
 *
 * @since 2007
 */
public class TmaAbstractOrderPriceIdPrepareInterceptor implements PrepareInterceptor<TmaAbstractOrderPriceModel>
{
	private KeyGenerator keyGenerator;

	public TmaAbstractOrderPriceIdPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final TmaAbstractOrderPriceModel price, final InterceptorContext context)
	{
		if (context.isNew(price) && StringUtils.isEmpty(price.getId()))
		{
			price.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
