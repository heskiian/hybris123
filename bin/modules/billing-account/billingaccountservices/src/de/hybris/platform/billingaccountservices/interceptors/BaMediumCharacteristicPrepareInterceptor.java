/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaMediumCharacteristicModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaMediumCharacteristicModel} that are newly created.
 *
 * @since 2111
 */
public class BaMediumCharacteristicPrepareInterceptor implements PrepareInterceptor<BaMediumCharacteristicModel>
{
	private KeyGenerator keyGenerator;

	public BaMediumCharacteristicPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaMediumCharacteristicModel mediumCharacteristicModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(mediumCharacteristicModel.getId()))
		{
			mediumCharacteristicModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
