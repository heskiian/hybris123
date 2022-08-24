/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaIdentificationDao;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaIdentificationService;


/**
 * Default implementation of the {@link TmaIdentificationService}.
 *
 * @since 1911
 */
public class DefaultTmaIdentificationService implements TmaIdentificationService
{
	private TmaIdentificationDao tmaIdentificationDao;

	public DefaultTmaIdentificationService(final TmaIdentificationDao tmaIdentificationDao)
	{
		this.tmaIdentificationDao = tmaIdentificationDao;
	}

	@Override
	public TmaIdentificationModel findIdentificationByTypeAndNumber(final String identificationType,
			final String identificationNumber)
	{
		return getTmaIdentificationDao().getTmaIdentificationModelForTypeAndNumber(identificationType, identificationNumber);
	}

	protected TmaIdentificationDao getTmaIdentificationDao()
	{
		return tmaIdentificationDao;
	}

}
