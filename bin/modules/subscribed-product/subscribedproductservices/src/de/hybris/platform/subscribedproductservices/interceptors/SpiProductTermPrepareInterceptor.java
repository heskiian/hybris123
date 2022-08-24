/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscribedproductservices.model.SpiProductTermModel;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link SpiProductTermModel} that are newly created.
 *
 * @since 2111
 */
public class SpiProductTermPrepareInterceptor implements PrepareInterceptor<SpiProductTermModel>
{
	private KeyGenerator keyGenerator;

	public SpiProductTermPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final SpiProductTermModel productTermModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(productTermModel.getId()))
		{
			productTermModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
