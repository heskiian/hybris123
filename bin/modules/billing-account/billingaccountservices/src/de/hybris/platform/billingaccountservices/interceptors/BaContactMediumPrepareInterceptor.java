/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaContactMediumModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaContactMediumModel} that are newly created.
 *
 * @since 2111
 */
public class BaContactMediumPrepareInterceptor implements PrepareInterceptor<BaContactMediumModel>
{
	private KeyGenerator keyGenerator;

	public BaContactMediumPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaContactMediumModel contactMediumModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(contactMediumModel.getId()))
		{
			contactMediumModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
