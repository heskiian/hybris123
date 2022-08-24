/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang3.StringUtils;


/**
 * Interceptor to generate unique id for {@link TmaProductSpecCharValueUseModel}  that are newly created or cloned.
 *
 * @since 2105
 */
public class TmaProductSpecCharValueUsePrepareInterceptor implements PrepareInterceptor<TmaProductSpecCharValueUseModel>
{
	private static final Integer DEFAULT_MIN_CARDINALITY = 0;

	private KeyGenerator tmaPscvuIdGenerator;

	public TmaProductSpecCharValueUsePrepareInterceptor(final KeyGenerator tmaPscvuIdGenerator)
	{
		this.tmaPscvuIdGenerator = tmaPscvuIdGenerator;
	}

	@Override
	public void onPrepare(final TmaProductSpecCharValueUseModel productSpecCharValueUse, final InterceptorContext ctx)
	{
		if (ctx.isNew(productSpecCharValueUse) && StringUtils.isEmpty(productSpecCharValueUse.getId()))
		{
			productSpecCharValueUse.setId((String) getTmaPscvuIdGenerator().generate());
		}

		if (productSpecCharValueUse.getMinCardinality() == null)
		{
			productSpecCharValueUse.setMinCardinality(DEFAULT_MIN_CARDINALITY);
		}
	}

	protected KeyGenerator getTmaPscvuIdGenerator()
	{
		return tmaPscvuIdGenerator;
	}
}
