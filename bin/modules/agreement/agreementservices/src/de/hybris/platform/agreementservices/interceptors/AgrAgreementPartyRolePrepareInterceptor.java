/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrPartyRoleModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrPartyRoleModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementPartyRolePrepareInterceptor implements PrepareInterceptor<AgrPartyRoleModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementPartyRolePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrPartyRoleModel partyRoleModel, final InterceptorContext ctx)
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
