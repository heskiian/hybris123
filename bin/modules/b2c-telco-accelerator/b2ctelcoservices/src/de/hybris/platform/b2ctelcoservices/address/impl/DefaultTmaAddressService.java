/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.address.impl;

import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.daos.TmaAddressDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.impl.DefaultAddressService;

import java.util.Collection;
import java.util.Objects;

import org.apache.log4j.Logger;


/**
 * Default implementation of {@link TmaAddressService}
 *
 * @since 1911
 */
public class DefaultTmaAddressService extends DefaultAddressService implements TmaAddressService
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaAddressService.class);
	private transient TmaAddressDao tmaAddressDao;

	/**
	 * Default constructor for address service.
	 *
	 * @param tmaAddressDao
	 * 		the address dao used by the address service.
	 */
	public DefaultTmaAddressService(TmaAddressDao tmaAddressDao)
	{
		this.tmaAddressDao = tmaAddressDao;
	}

	@Override
	public AddressModel findAddress(final String addressId)
	{
		return getTmaAddressDao().findAddress(addressId);
	}

	@Override
	public boolean doesAddressExist(final String addressId)
	{
		try
		{
			return findAddress(addressId) != null;
		}
		catch (ModelNotFoundException | AmbiguousIdentifierException ex)
		{
			LOG.info(ex);
			return false;
		}
	}

	@Override
	public boolean doesAddressBelongToUser(final AddressModel addressModel, final UserModel user)
	{
		final Collection<AddressModel> addressesForOwner = getAddressesForOwner(user);
		return addressesForOwner.contains(addressModel);
	}

	@Override
	public boolean doesAddressNeedCloning(AddressModel address, AbstractOrderEntryModel orderEntry)
	{
		if (address == null)
		{
			return false;
		}
		return !Objects.equals(orderEntry, address.getOwner());
	}

	protected TmaAddressDao getTmaAddressDao()
	{
		return tmaAddressDao;
	}
}
