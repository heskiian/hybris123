/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementservices.interceptors;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;


/**
 * Interceptor to generate unique id for {@link AgrAgreementItemModel} that are newly created.
 *
 * @since 2111
 */
public class AgrAgreementItemPrepareInterceptor implements PrepareInterceptor<AgrAgreementItemModel>
{
	private KeyGenerator keyGenerator;

	public AgrAgreementItemPrepareInterceptor(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	@Override
	public void onPrepare(final AgrAgreementItemModel agreementItemModel, final InterceptorContext ctx)
	{
		if (StringUtils.isEmpty(agreementItemModel.getId()))
		{
			agreementItemModel.setId((String) getKeyGenerator().generate());
		}
	}

	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}
}
