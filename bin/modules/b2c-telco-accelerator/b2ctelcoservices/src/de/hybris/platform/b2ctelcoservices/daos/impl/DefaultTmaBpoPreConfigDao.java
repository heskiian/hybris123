/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.daos.TmaBpoPreConfigDao;
import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation of {@link TmaBpoPreConfigDao}
 */
public class DefaultTmaBpoPreConfigDao extends DefaultGenericSearchDao<TmaBpoPreConfigModel> implements TmaBpoPreConfigDao
{
	public DefaultTmaBpoPreConfigDao()
	{
		super(TmaBpoPreConfigModel._TYPECODE);
	}

	@Override
	public TmaBpoPreConfigModel findBpoPreconfigByCode(final String code, final CatalogVersionModel catalogVersion)
	{
		validateParameterNotNull(code, "code must not be null!");
		validateParameterNotNull(catalogVersion, "CatalogVersion must not be null!");
		final Map parameters = new HashMap();
		parameters.put(TmaBpoPreConfigModel.CODE, code);
		parameters.put(TmaBpoPreConfigModel.CATALOGVERSION, catalogVersion);
		return findUnique(parameters);
	}
}
