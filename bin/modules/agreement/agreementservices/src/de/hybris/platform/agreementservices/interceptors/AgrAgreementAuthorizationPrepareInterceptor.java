/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementAuthorizationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementAuthorizationModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementAuthorizationPrepareInterceptor implements PrepareInterceptor<AgrAgreementAuthorizationModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementAuthorizationPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementAuthorizationModel authorizationModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(authorizationModel.getId()))
		{
			authorizationModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
