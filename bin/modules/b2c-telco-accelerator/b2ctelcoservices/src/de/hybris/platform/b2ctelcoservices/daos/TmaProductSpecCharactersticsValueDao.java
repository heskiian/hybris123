/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;

import java.util.List;


/**
 * TmaProductSpecCharactersticsValueDao is responsible for fetching the data from Database for given id.
 */

public interface TmaProductSpecCharactersticsValueDao
{

	/**
	 * TmaProductSpecCharactersticsValueDao is responsible for fetching the data from Database for given id
	 *
	 * @param pscvId
	 *           given id for fetching PSCV
	 * @return TmaProductSpecCharacteristicValueModel for give ID
	 */
	public List<TmaProductSpecCharacteristicValueModel> findPscvById(final String pscvId);

}
