/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * Data Access Object used to search models.
 *
 * @since 6.6
 */
public interface GenericSearchDao<M extends ItemModel>
{
	/**
	 * Searches for a unique model based on given parameters.
	 * If none or multiple models are found, a specific exception will be thrown.
	 *
	 * @param params
	 * 		{@link Map} containing name-value pairs used for identifying the unique model
	 * @return model for given parameter
	 */
	M findUnique(Map<String, ? extends Object> params);
}
