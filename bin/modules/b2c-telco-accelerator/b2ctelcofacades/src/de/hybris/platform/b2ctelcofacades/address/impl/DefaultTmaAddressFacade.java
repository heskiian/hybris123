/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.address.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.address.TmaAddressFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Default implementation of Tma Address facade. It implements Tma specific operations around address.
 *
 * @since 2007
 */
public class DefaultTmaAddressFacade implements TmaAddressFacade
{

	private final UserService userService;
	private final Converter<AddressModel, AddressData> addressConverter;
	private final CustomerAccountService customerAccountService;
	private final Converter<AddressData, AddressModel> addressReverseConverter;

	/**
	 * Instantiates a new default tma address facade.
	 *
	 * @param userService
	 *           the user service
	 * @param addressConverter
	 *           the address converter
	 * @param customerAccountService
	 *           the customer account service
	 * @param addressReverseConverter
	 *           the address reverse converter
	 */
	public DefaultTmaAddressFacade(final UserService userService, final Converter<AddressModel, AddressData> addressConverter,
			final CustomerAccountService customerAccountService, final Converter<AddressData, AddressModel> addressReverseConverter)
	{
		this.userService = userService;
		this.addressConverter = addressConverter;
		this.customerAccountService = customerAccountService;
		this.addressReverseConverter = addressReverseConverter;
	}

	@Override
	public AddressData getAddress(final String id, final String relatedPartyID)
	{
		final UserModel userModel = getUserService().getUserForUID(relatedPartyID);
		for (final AddressModel addressModel : userModel.getAddresses())
		{
			if (id.equals(addressModel.getId()))
			{
				return getAddressConverter().convert(addressModel);
			}
		}
		return null;
	}

	@Override
	public AddressData updateAddress(final AddressData addressData, final String addressId, final String userId)
	{
		validateParameterNotNullStandardMessage("addressData", addressData);
		validateParameterNotNullStandardMessage("addressId", addressId);
		validateParameterNotNullStandardMessage("userId", userId);
		final UserModel userModel = getUserService().getUserForUID(userId);
		if (!(userModel instanceof CustomerModel))
		{
			return null;
		}
		final CustomerModel customerModel = (CustomerModel) userModel;
		final AddressModel addressModel = getCustomerAccountService().getAddressForCode(customerModel, addressId);
		getAddressReverseConverter().convert(addressData, addressModel);
		getCustomerAccountService().saveAddressEntry(customerModel, addressModel);
		if (addressData.isDefaultAddress())
		{
			getCustomerAccountService().setDefaultAddressEntry(customerModel, addressModel);
		}
		else if (addressModel.equals(customerModel.getDefaultShipmentAddress()))
		{
			getCustomerAccountService().clearDefaultAddressEntry(customerModel);
		}
		return addressModel == null ? null : getAddressConverter().convert(addressModel);

	}

	@Override
	public boolean removeAddress(final String userId, final String addressId)
	{
		validateParameterNotNull(userId, "User ID cannot be null");
		validateParameterNotNull(addressId, "Address ID cannot be null");

		final UserModel userModel = getUserService().getUserForUID(userId);

		if (userModel == null || !(userModel instanceof CustomerModel))
		{
			return false;
		}

		final CustomerModel customerModel = (CustomerModel) userModel;

		for (final AddressModel addressModel : getCustomerAccountService().getAddressBookEntries(customerModel))
		{
			if (addressId.equals(addressModel.getId()))
			{
				getCustomerAccountService().deleteAddressEntry(customerModel, addressModel);
				return true;
			}
		}
		return false;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	protected CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	protected Converter<AddressData, AddressModel> getAddressReverseConverter()
	{
		return addressReverseConverter;
	}

}

