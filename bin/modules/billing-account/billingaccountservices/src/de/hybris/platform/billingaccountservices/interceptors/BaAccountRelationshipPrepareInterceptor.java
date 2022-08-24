/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.interceptors;

import de.hybris.platform.billingaccountservices.model.BaAccountRelationshipModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link BaAccountRelationshipModel} that are newly created.
 *
 * @since 2111
 */
public class BaAccountRelationshipPrepareInterceptor implements PrepareInterceptor<BaAccountRelationshipModel>
{
	private KeyGenerator keyGenerator;

	public BaAccountRelationshipPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final BaAccountRelationshipModel accountRelationshipModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(accountRelationshipModel.getId()))
		{
			accountRelationshipModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
