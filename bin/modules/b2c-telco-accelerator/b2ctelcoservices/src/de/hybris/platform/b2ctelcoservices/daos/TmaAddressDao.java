/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.daos.AddressDao;

import java.io.Serializable;


/**
 * Data Access Object for the {@link AddressModel} object.
 *
 * @since 1911
 */
public interface TmaAddressDao extends AddressDao
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
}
