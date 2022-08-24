/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


public class TmaAddressReversePopulator extends AddressReversePopulator
{

	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		validateParameterNotNull(addressData, "addressData cannot be null.");
		validateParameterNotNull(addressModel, "addressModel cannot be null.");

		addressModel.setUnloadingAddress(addressData.isUnloadingAddress());
		addressModel.setContactAddress(addressData.isContactAddress());
      addressModel.setInstallationAddress(addressData.isInstallationAddress());
		addressModel.setBuilding(addressData.getBuilding());
		addressModel.setAppartment(addressData.getApartment());
		super.populate(addressData, addressModel);
	}
}
