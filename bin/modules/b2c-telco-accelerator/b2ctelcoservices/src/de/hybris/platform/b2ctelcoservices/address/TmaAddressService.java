/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.address;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.AddressService;


/**
 * Service handling operations specific to {@link AddressModel}
 *
 * @since 1911
 */
public interface TmaAddressService extends AddressService
{
	/**
	 * Gets an Address for a given id.
	 *
	 * @param addressId
	 * 		the id of the requested address.
	 * @return the Address if found.
	 * @throws ModelNotFoundException
	 * 		if no address is found
	 */
	AddressModel findAddress(final String addressId);

	/**
	 * Checks if the address with the given id exists.
	 *
	 * @param addressId
	 * 		the id of the address.
	 * @return true if the address exists; false otherwise.
	 */
	boolean doesAddressExist(final String addressId);

	/**
	 * Checks if an address belongs to a user.
	 *
	 * @param addressModel
	 * 		the address to be checked
	 * @param user
	 * 		the user.
	 * @return true if the address belongs to the user; otherwise false.
	 */
	boolean doesAddressBelongToUser(final AddressModel addressModel, final UserModel user);

	/**
	 * Checks if the address should be cloned
	 *
	 * @param address
	 * 		the address to be checked
	 * @param orderEntry
	 * 		the order entry for which the address should be checked
	 * @return true if the address should be cloned, otherwise false
	 */
	boolean doesAddressNeedCloning(final AddressModel address, final AbstractOrderEntryModel orderEntry);
}
