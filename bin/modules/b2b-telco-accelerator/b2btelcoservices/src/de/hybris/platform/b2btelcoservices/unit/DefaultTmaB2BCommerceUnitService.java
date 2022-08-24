/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2btelcoservices.unit;

import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.company.impl.DefaultB2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.ArrayList;
import java.util.Collection;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of {@link B2BCommerceUnitService}
 *
 * @since 2105
 */
public class DefaultTmaB2BCommerceUnitService extends DefaultB2BCommerceUnitService
{
	@Override
	public void removeAddressEntry(final String unitUid, final String addressId)
	{
		final B2BUnitModel unit = getUnitForUid(unitUid);
		validateParameterNotNullStandardMessage("B2BUnit", unit);
		final Collection<AddressModel> addresses = new ArrayList<>(unit.getAddresses());
		for (final AddressModel addressModel : addresses)
		{
			if (addressModel.getId().equals(addressId))
			{
				addresses.remove(addressModel);
				unit.setAddresses(addresses);
				getModelService().remove(addressModel);
				break;
			}
		}
	}

	@Override
	public AddressModel getAddressForCode(final B2BUnitModel unit, final String id)
	{
		for (final AddressModel addressModel : unit.getAddresses())
		{
			if (addressModel.getId().equals(id))
			{
				return addressModel;
			}
		}
		return null;
	}
}
