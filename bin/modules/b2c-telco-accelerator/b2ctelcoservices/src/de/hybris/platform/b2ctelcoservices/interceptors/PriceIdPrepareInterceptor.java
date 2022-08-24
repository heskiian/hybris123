/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor to generate unique price Id for newly created price plan.
 *
 * @since 1903
 *
 */
public class PriceIdPrepareInterceptor implements PrepareInterceptor
{

	private KeyGenerator priceIDGenerator;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof PDTRowModel && ctx.isNew(model))
		{
			final PDTRowModel pdtModel = (PDTRowModel) model;
			final String id = pdtModel.getCode();
			if (StringUtils.isEmpty(id))
			{
				pdtModel.setCode((String) getPriceIDGenerator().generate());
			}

		}
	}

	@Required
	public void setPriceIDGenerator(final KeyGenerator priceIDGenerator)
	{
		this.priceIDGenerator = priceIDGenerator;
	}

	protected KeyGenerator getPriceIDGenerator()
	{
		return priceIDGenerator;
	}

}
