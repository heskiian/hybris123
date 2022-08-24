/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderEntryPscvModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.springframework.beans.factory.annotation.Required;


/**
 * Sets default values for the id attribute of new {@link TmaAbstractOrderEntryPscvModel} instances
 *
 * @since 1911
 */
public class TmaAbstractOrderEntryPscvPrepareInterceptor implements PrepareInterceptor
{
	private KeyGenerator keyGenerator;

	@Override
	public void onPrepare(final Object model, final InterceptorContext ctx)
	{
		if (model instanceof TmaAbstractOrderEntryPscvModel && ctx.isNew(model)
				&& ((TmaAbstractOrderEntryPscvModel) model).getId() == null)
		{
			((TmaAbstractOrderEntryPscvModel) model).setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	@Required
	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}
}
