/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscribedproductservices.model.SpiCharacteristicModel;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link SpiCharacteristicModel} that are newly created.
 *
 * @since 2111
 */
public class SpiCharacteristicPrepareInterceptor implements PrepareInterceptor<SpiCharacteristicModel>
{
	private KeyGenerator keyGenerator;

	public SpiCharacteristicPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final SpiCharacteristicModel characteristicModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(characteristicModel.getId()))
		{
			characteristicModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
