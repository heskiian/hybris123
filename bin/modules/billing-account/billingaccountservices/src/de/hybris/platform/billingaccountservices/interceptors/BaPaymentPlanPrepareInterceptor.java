/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaPaymentPlanModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaPaymentPlanModel} that are newly created.
 *
 * @since 2111
 */
public class BaPaymentPlanPrepareInterceptor implements PrepareInterceptor<BaPaymentPlanModel>
{
	private KeyGenerator keyGenerator;

	public BaPaymentPlanPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaPaymentPlanModel paymentPlanModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(paymentPlanModel.getId()))
		{
			paymentPlanModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
