/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.daos.TmaProductSpecCharactersticsValueDao;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation for {@link TmaProductSpecCharactersticsValueDao} fetched
 * TmaProductSpecCharacteristicValueModel for given ID
 *
 * @since 18.05
 */

public class DefaultTmaProductSpecCharactersticsValueDao extends DefaultGenericSearchDao<TmaProductSpecCharacteristicValueModel>
		implements TmaProductSpecCharactersticsValueDao
{

	public DefaultTmaProductSpecCharactersticsValueDao()
	{
		super(TmaProductSpecCharacteristicValueModel._TYPECODE);
	}


	@Override
	public List<TmaProductSpecCharacteristicValueModel> findPscvById(final String pscvId)
	{
		validateParameterNotNull(pscvId, "PSCV ID must not be null!");
		final Map parameters = new HashMap();
		parameters.put(TmaProductSpecCharacteristicValueModel.ID, pscvId);
		return find(parameters);
	}

}
