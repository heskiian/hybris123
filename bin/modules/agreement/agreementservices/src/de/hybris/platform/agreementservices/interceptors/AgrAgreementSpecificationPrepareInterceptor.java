/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementSpecificationModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementSpecificationPrepareInterceptor implements PrepareInterceptor<AgrAgreementSpecificationModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementSpecificationPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementSpecificationModel agrAgreementSpecificationModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(agrAgreementSpecificationModel.getId()))
		{
			agrAgreementSpecificationModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
