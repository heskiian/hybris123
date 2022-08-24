/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecCharacteristicValueModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementSpecCharacteristicValueModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementSpecCharacteristicValuePrepareInterceptor
		implements PrepareInterceptor<AgrAgreementSpecCharacteristicValueModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementSpecCharacteristicValuePrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementSpecCharacteristicValueModel agreementSpecCharacteristicValueModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(agreementSpecCharacteristicValueModel.getId()))
		{
			agreementSpecCharacteristicValueModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
