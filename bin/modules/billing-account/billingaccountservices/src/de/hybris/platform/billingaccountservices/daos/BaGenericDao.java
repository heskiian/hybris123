/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.daos;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * Data access object for {@link ItemModel}.
 *
 * @since 2111
 */
public interface BaGenericDao
{
	/**
	 * Retrieves an item model of a specific typeCode with the given id
	 *
	 * @param typeCode
	 * 		the typeCode of the model
	 * @param id
	 * 		the id
	 * @return the model found, null otherwise
	 */
	ItemModel getItem(final String typeCode, final String id);

	/**
	 * Retrieves an item model of a specific typeCode with the given parameters
	 *
	 * @param typeCode
	 * 		the typeCode of the model
	 * @param params
	 * 		the key-value pairs to filter the response
	 * @return the model found, null otherwise
	 */
	ItemModel getItem(final String typeCode, final Map<String, String> params);
}
