/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaBillStructureModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaBillStructureModel} that are newly created.
 *
 * @since 2111
 */
public class BaBillStructurePrepareInterceptor implements PrepareInterceptor<BaBillStructureModel>
{
	private KeyGenerator keyGenerator;

	public BaBillStructurePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaBillStructureModel billStructureModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(billStructureModel.getId()))
		{
			billStructureModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
