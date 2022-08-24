/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link SpiProductPriceModel} that are newly created.
 *
 * @since 2111
 */
public class SpiProductPricePrepareInterceptor implements PrepareInterceptor<SpiProductPriceModel>
{
	private KeyGenerator keyGenerator;

	public SpiProductPricePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final SpiProductPriceModel productPriceModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(productPriceModel.getId()))
		{
			productPriceModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
