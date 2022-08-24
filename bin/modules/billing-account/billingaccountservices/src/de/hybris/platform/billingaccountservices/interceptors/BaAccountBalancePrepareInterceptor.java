/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaAccountBalanceModel} that are newly created.
 *
 * @since 2111
 */
public class BaAccountBalancePrepareInterceptor implements PrepareInterceptor<BaAccountBalanceModel>
{
	private KeyGenerator keyGenerator;

	public BaAccountBalancePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaAccountBalanceModel accountBalanceModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(accountBalanceModel.getId()))
		{
			accountBalanceModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
