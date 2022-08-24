/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services;

import de.hybris.platform.core.model.ItemModel;

import java.util.Map;


/**
 * Service responsible for handling {@link ItemModel}  related operations.
 *
 * @since 2111
 */
public interface SpiGenericService
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
	 * @return the item found
	 */
	ItemModel getItem(final String typeCode, final Map<String, String> params);

	/**
	 * Creates a new item model of the given class.
	 *
	 * @param itemClass
	 * 		the class of the item to be created.
	 * @return the created item.
	 */
	ItemModel createItem(final Class itemClass);

	/**
	 * Saves the given item model to the database.
	 *
	 * @param item
	 * 		the item model
	 */
	void saveItem(final ItemModel item);

}
