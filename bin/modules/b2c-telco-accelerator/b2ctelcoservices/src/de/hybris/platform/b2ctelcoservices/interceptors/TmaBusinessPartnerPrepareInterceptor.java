/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaBusinessPartnerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;


/**
 * Interceptor to generate unique code for newly created Business Partner.
 *
 * @since 1911
 *
 */
public class TmaBusinessPartnerPrepareInterceptor implements PrepareInterceptor<TmaBusinessPartnerModel>
{

	private KeyGenerator tmaBusinessPartnerIdGenerator;

	public TmaBusinessPartnerPrepareInterceptor(final KeyGenerator tmaBusinessPartnerIdGenerator)
	{
		this.tmaBusinessPartnerIdGenerator = tmaBusinessPartnerIdGenerator;
	}

	@Override
	public void onPrepare(final TmaBusinessPartnerModel tmaBusinessPartner, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (interceptorContext.isNew(tmaBusinessPartner))
		{
			tmaBusinessPartner.setCommBusinessPartnerId(String.valueOf(getTmaBusinessPartnerIdGenerator().generate()));
		}
	}

	protected KeyGenerator getTmaBusinessPartnerIdGenerator()
	{
		return tmaBusinessPartnerIdGenerator;
	}
}
