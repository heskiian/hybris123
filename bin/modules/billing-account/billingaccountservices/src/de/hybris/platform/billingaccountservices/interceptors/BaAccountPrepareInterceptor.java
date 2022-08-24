/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.springframework.util.StringUtils;

/**
 * Interceptor to generate unique id for {@link BaAccountModel} that are newly created.
 *
 * @since 2111
 */
public class BaAccountPrepareInterceptor implements PrepareInterceptor<BaAccountModel>
{
	private KeyGenerator keyGenerator;

	public BaAccountPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaAccountModel baAccountModel, final InterceptorContext interceptorContext)

	{
		if (StringUtils.isEmpty(baAccountModel.getId()))
		{
			baAccountModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
