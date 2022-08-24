/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.customer;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Service that extends {@link DefaultCustomerAccountService} with telco related functionality.
 *
 * @since 2007
 */
public class DefaultTmaCustomerAccountService extends DefaultCustomerAccountService
{
	@Override
	public AddressModel getAddressForCode(final CustomerModel customerModel, final String code)
	{
		validateParameterNotNull(customerModel, "Customer model cannot be null");

		for (final AddressModel addressModel : getAllAddressEntries(customerModel))
		{
			if (addressModel.getId().equals(code))
			{
				return addressModel;
			}
		}
		return null;
	}

}
