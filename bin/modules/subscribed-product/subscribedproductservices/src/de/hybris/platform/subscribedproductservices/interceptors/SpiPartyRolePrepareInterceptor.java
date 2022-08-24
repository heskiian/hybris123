/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.subscribedproductservices.model.SpiPartyRoleModel;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link SpiPartyRoleModel} that are newly created.
 *
 * @since 2111
 */
public class SpiPartyRolePrepareInterceptor implements PrepareInterceptor<SpiPartyRoleModel>
{
	private KeyGenerator keyGenerator;

	public SpiPartyRolePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final SpiPartyRoleModel partyRoleModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(partyRoleModel.getId()))
		{
			partyRoleModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
