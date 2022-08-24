/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import org.springframework.util.ObjectUtils;



/**
 * Interceptor to set price value on {@link PriceRowModel} as 0 when there is a {@link TmaProductOfferingPriceModel}.
 *
 * @since 2007
 */
public class TmaPriceValuePrepareInterceptor implements PrepareInterceptor<PriceRowModel>
{
	@Override
	public void onPrepare(PriceRowModel priceRowModel, InterceptorContext interceptorContext)
	{
		TmaProductOfferingPriceModel productOfferingPrice = priceRowModel.getProductOfferingPrice();
		if (ObjectUtils.isEmpty(productOfferingPrice))
		{
			return;
		}
		priceRowModel.setPrice((double) 0);
	}
}


