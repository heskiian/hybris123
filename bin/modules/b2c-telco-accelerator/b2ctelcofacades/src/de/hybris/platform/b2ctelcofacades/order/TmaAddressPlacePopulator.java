/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.platform.b2ctelcofacades.data.TmaPlaceData;
import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.data.TmaAddressPlace;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator for {@link TmaAddressPlace}
 *
 * @since 1911
 */
public class TmaAddressPlacePopulator implements Populator<TmaPlaceData, TmaAddressPlace>
{
	private TmaAddressService addressService;

	public TmaAddressPlacePopulator(final TmaAddressService addressService)
	{
		this.addressService = addressService;
	}

	@Override
	public void populate(final TmaPlaceData placeData, final TmaAddressPlace addressPlace) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", placeData);
		validateParameterNotNullStandardMessage("target", addressPlace);

		final String addressId = placeData.getId();
		if (!getAddressService().doesAddressExist(addressId))
		{
			throw new IllegalArgumentException("Invalid place id: " + addressId);
		}

		addressPlace.setAddress(getAddressService().findAddress(addressId));
		addressPlace.setRole(placeData.getRole());
	}

	protected TmaAddressService getAddressService()
	{
		return addressService;
	}
}
