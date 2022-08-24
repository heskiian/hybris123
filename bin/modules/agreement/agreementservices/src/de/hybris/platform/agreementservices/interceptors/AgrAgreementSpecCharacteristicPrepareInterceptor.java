/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementSpecCharacteristicModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementSpecCharacteristicPrepareInterceptor
		implements PrepareInterceptor<AgrAgreementSpecCharacteristicModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementSpecCharacteristicPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementSpecCharacteristicModel agreementSpecCharacteristicModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(agreementSpecCharacteristicModel.getId()))
		{
			agreementSpecCharacteristicModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
