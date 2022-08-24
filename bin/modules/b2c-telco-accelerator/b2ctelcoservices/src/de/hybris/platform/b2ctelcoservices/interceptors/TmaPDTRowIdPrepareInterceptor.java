/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
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
 * Interceptor to generate unique code for newly created PDTRows.
 *
 * @since 1907
 *
 */
public class TmaPDTRowIdPrepareInterceptor implements PrepareInterceptor<PDTRowModel>
{
	private KeyGenerator tmaPDTRowIdGenerator;

	@Override
	public void onPrepare(final PDTRowModel model, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(model))
		{
			final String id = model.getCode();
			if (StringUtils.isEmpty(id))
			{
				model.setCode((String) getTmaPDTRowIdGenerator().generate());
			}
		}
	}

	protected KeyGenerator getTmaPDTRowIdGenerator()
	{
		return tmaPDTRowIdGenerator;
	}

	@Required
	public void setTmaPDTRowIdGenerator(final KeyGenerator tmaPDTRowIdGenerator)
	{
		this.tmaPDTRowIdGenerator = tmaPDTRowIdGenerator;
	}
}
