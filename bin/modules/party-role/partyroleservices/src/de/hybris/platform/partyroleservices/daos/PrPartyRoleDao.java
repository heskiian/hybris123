/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroleservices.daos;

import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Map;


/**
 * Data access object for {@link PrPartyRoleModel}s.
 *
 * @since 2108
 */
public interface PrPartyRoleDao extends GenericDao<PrPartyRoleModel>
{

	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	PrPartyRoleModel findPartyRole(final Map<String, ? extends Object> params);
}
