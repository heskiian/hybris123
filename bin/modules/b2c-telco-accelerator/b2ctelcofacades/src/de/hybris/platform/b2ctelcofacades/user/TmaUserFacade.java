/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.user;


import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.List;


/**
 * User facade handling user related operations in TMU.
 *
 * @since 1907
 */
public interface TmaUserFacade extends UserFacade
{
	/**
	 * Return a list of {@link AddressData} for user identified by its uid having the address book visibility provided.
	 *
	 * @param userId
	 * 		the identifier of the user
	 * @param visibleInAddressBook
	 * 		flag indicating the fact address should be visible in address book
	 * @return a list of addresses for user with uid provided if exists, otherwise an empty list is returned
	 */
	List<AddressData> getAddressesForUser(String userId, boolean visibleInAddressBook);

	/**
	 * Creates a {@link AddressData} for the provided user.
	 *
	 * @param addressData
	 * 		The address
	 * @param userId
	 * 		The identifier of the user
	 * @return {@link AddressData} which was created
	 */
	AddressData createAddressForUser(AddressData addressData, String userId);

}
