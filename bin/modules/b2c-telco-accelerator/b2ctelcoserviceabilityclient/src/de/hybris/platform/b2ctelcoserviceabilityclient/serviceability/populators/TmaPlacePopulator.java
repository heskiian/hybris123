/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoserviceabilityclient.serviceability.populators;

import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.GEOGRAPHIC_ADDRESS;
import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.INSTALLATION_PLACE;

import de.hybris.platform.b2ctelcotmfresources.v4.dto.GeographicAddress;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;

import org.springframework.util.Assert;


/**
 * Populate the Place from the AddressModel
 *
 * @since 2102
 *
 */
public class TmaPlacePopulator implements Populator<AddressModel, GeographicAddress>
{

	@Override
	public void populate(final AddressModel source, final GeographicAddress target)
	{
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source != null)
		{
			target.setAttype(GEOGRAPHIC_ADDRESS);
			target.setRole(INSTALLATION_PLACE);
			target.setCity(source.getTown());
			target.setPostcode(source.getPostalcode());
			target.setStreetName(source.getStreetname());
			target.setStreetNr(source.getStreetnumber());
			target.setCountry(source.getCountry() == null ? null : source.getCountry().getIsocode());
		}
	}
}
