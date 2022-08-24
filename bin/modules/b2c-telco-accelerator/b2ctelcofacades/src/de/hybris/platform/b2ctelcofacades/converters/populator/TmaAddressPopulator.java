/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import org.springframework.util.Assert;


/**
 * Populates {@link AddressData} based on {@link AddressModel}.
 *
 * @since 1907
 */
public class TmaAddressPopulator extends AddressPopulator
{

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setContactAddress(source.getContactAddress());
		target.setUnloadingAddress(source.getUnloadingAddress());
		target.setInstallationAddress(source.getInstallationAddress());
		target.setCode(source.getId());
		target.setBuilding(source.getBuilding());
		target.setApartment(source.getAppartment());
		super.populate(source, target);

		target.setId(source.getId());
	}

}
