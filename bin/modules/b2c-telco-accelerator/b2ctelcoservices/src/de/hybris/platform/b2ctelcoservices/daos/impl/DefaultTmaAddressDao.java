/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaAddressDao;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.daos.impl.DefaultAddressDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link TmaAddressDao}
 *
 * @since 1911
 */
public class DefaultTmaAddressDao extends DefaultAddressDao implements TmaAddressDao
{
	@Override
	public AddressModel findAddress(final String addressId)
	{
		final Map<String, String> parameters = new HashMap<>();
		parameters.put(AddressModel.ID, addressId);
		final List<AddressModel> addresses = find(parameters);
		if (addresses.isEmpty())
		{
			throw new ModelNotFoundException("No address with id: " + addressId);
		}
		if (addresses.size() > 1)
		{
			throw new AmbiguousIdentifierException("Found " + addresses.size() + " models for " +
					AddressModel.ID + " '" + addressId);
		}
		return addresses.get(0);
	}
}
