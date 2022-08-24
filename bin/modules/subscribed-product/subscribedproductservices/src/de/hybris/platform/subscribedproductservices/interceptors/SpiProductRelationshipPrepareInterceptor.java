/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscribedproductservices.model.SpiProductRelationshipModel;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link SpiProductRelationshipModel} that are newly created.
 *
 * @since 2111
 */
public class SpiProductRelationshipPrepareInterceptor implements PrepareInterceptor<SpiProductRelationshipModel>
{
	private KeyGenerator keyGenerator;

	public SpiProductRelationshipPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final SpiProductRelationshipModel productRelationshipModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(productRelationshipModel.getId()))
		{
			productRelationshipModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
