/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.configurableguidedselling;

import de.hybris.platform.core.order.EntryGroup;

import java.util.List;


/**
 * Entry Group Facade. Facade is responsible for getting all information for Entry Groups for Bundle Templates.
 *
 * @since 6.6
 */

public interface EntryGroupFacade
{
	/**
	 * Fetch Root Group for the Leaf bundle entry number.
	 *
	 * @param groupNumber
	 * 		the group number
	 * @return the parent entry group
	 */
	EntryGroup getRootEntryGroup(Integer groupNumber);

	/**
	 * Fetch Current Group for the Leaf bundle entry number.
	 *
	 * @param groupNumber
	 * 		the group number
	 * @return the current entry group
	 */
	EntryGroup getCurrentEntryGroup(Integer groupNumber);
}
