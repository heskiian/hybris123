/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang3.StringUtils;


/**
 * Interceptor to generate unique id for the newly created {@link TmaProductOfferingPriceModel}s.
 *
 * @since 2007
 */
public class TmaProductOfferingPriceIdPrepareInterceptor implements PrepareInterceptor<TmaProductOfferingPriceModel>
{
	private KeyGenerator tmaProductOfferingPriceIdGenerator;

	public TmaProductOfferingPriceIdPrepareInterceptor(final KeyGenerator tmaProductOfferingPriceIdGenerator)
	{
		this.tmaProductOfferingPriceIdGenerator = tmaProductOfferingPriceIdGenerator;
	}

	@Override
	public void onPrepare(TmaProductOfferingPriceModel model, InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(model) && StringUtils.isEmpty(model.getId()))
		{
			model.setId((String) getTmaProductOfferingPriceIdGenerator().generate());
		}
	}

	protected KeyGenerator getTmaProductOfferingPriceIdGenerator()
	{
		return tmaProductOfferingPriceIdGenerator;
	}
}
