/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.address;

import de.hybris.platform.commercefacades.user.data.AddressData;


/**
 * Address facade handling address related operations in TMU.
 *
 * @since 2007
 */
public interface TmaAddressFacade
{


	/**
	 * Retrieves given address from id for the provided user id.
	 *
	 * @param id
	 *           the id
	 * @param relatedPartyID
	 *           the related party ID
	 * @return the specific address for user if it belongs to user otherwise null
	 */
	AddressData getAddress(String id, String relatedPartyID);

	/**
	 * Updates a {@link AddressData} for the provided user.
	 *
	 * @param addressData
	 *           The address data
	 * @param addressId
	 *           The identifier of the address
	 * @param userId
	 *           The identifier of the address
	 * @return {@link AddressData} which was updated
	 */
	AddressData updateAddress(AddressData addressData, String addressId, String userId);

	/**
	 * Delete given address for the provided user id.
	 *
	 * @param userId
	 *           The identifier of the user
	 * @param addressId
	 *           The addressId
	 * @return True if record is deleted and False in case. no record deletion
	 *
	 */
	boolean removeAddress(final String userId, final String addressId);
}
