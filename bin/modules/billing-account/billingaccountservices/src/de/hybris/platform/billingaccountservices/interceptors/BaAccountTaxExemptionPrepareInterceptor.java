/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaAccountTaxExemptionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaAccountTaxExemptionModel} that are newly created.
 *
 * @since 2111
 */
public class BaAccountTaxExemptionPrepareInterceptor implements PrepareInterceptor<BaAccountTaxExemptionModel>
{
	private KeyGenerator keyGenerator;

	public BaAccountTaxExemptionPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaAccountTaxExemptionModel accountTaxExemptionModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(accountTaxExemptionModel.getId()))
		{
			accountTaxExemptionModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
