/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharValueUseModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementSpecCharValueUseModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementSpecCharValueUsePrepareInterceptor
		implements PrepareInterceptor<AgrAgreementSpecCharValueUseModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementSpecCharValueUsePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementSpecCharValueUseModel agreementSpecCharValueUseModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(agreementSpecCharValueUseModel.getId()))
		{
			agreementSpecCharValueUseModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
