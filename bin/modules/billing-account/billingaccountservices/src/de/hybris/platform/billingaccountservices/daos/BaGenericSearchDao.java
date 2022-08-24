/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.daos;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Map;


/**
 * Data Access Object used to search models.
 *
 * @since 2105
 */
public interface BaGenericSearchDao<M extends ItemModel> extends GenericDao<M>
{
	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 *      {@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	M findUnique(Map<String, ? extends Object> params);
}
