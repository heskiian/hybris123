/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.user.impl;

import de.hybris.platform.b2ctelcofacades.user.TmaUserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;

import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.commercefacades.user.impl.DefaultUserFacade;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of Tma user facade. It implements Tma specific operations around users.
 *
 * @since 1907
 */
public class DefaultTmaUserFacade extends DefaultUserFacade implements TmaUserFacade
{
	/**
	 * Converts from {@link UserModel} to {@link PrincipalData}
	 */
	private Converter<PrincipalModel, PrincipalData> principalConverter;
	/**
	 * {@inheritDoc}
	 * In case the visibleInAddressBook flag is false then the response also contains those addresses for which the
	 * visibleInAddressBook attribute value is null.
	 */
	@Override
	public List<AddressData> getAddressesForUser(String userId, boolean visibleInAddressBook)
	{
		validateParameterNotNull(userId, "User ID cannot be null");

		final UserModel userModel = getUserService().getUserForUID(userId);
		if (userModel == null)
		{
			return null;
		}

		final Collection<AddressModel> addressModels = userModel.getAddresses();
		if (CollectionUtils.isEmpty(addressModels))
		{
			return null;
		}

		final List<AddressData> result = new ArrayList<>();
		for (final AddressModel address : addressModels)
		{
			if (((address.getVisibleInAddressBook() == null) && !visibleInAddressBook) ||
					((address.getVisibleInAddressBook() != null) && address.getVisibleInAddressBook().equals(visibleInAddressBook)))
			{
				final AddressData addressData = getAddressConverter().convert(address);
				addressData.setUser(getPrincipalConverter().convert(userModel));
				result.add(addressData);
			}
		}

		return result;
	}

	@Override
	public AddressData createAddressForUser(AddressData addressData, String userId)
	{
		validateParameterNotNull(addressData, "AddressData cannot be null.");

		final UserModel userModel = getUserService().getUserForUID(userId);

		if (!(userModel instanceof CustomerModel))
		{
			return null;
		}

		final CustomerModel customerModel = (CustomerModel) userModel;

		final boolean makeThisAddressTheDefault = addressData.isDefaultAddress()
				|| (customerModel.getDefaultShipmentAddress() == null && addressData.isVisibleInAddressBook());


		final AddressModel addressModel = getModelService().create(AddressModel.class);

		if (addressModel == null)
		{
			return null;
		}

		getAddressReversePopulator().populate(addressData, addressModel);
		addressData.setUser(getPrincipalConverter().convert(userModel));

		getCustomerAccountService().saveAddressEntry(customerModel, addressModel);

		addressData.setId(addressModel.getId());

		if (makeThisAddressTheDefault)
		{
			getCustomerAccountService().setDefaultAddressEntry(customerModel, addressModel);
		}
		return addressData;
	}

	@Override
	public void addAddress(final AddressData addressData)
	{
		validateParameterNotNullStandardMessage("addressData", addressData);

		final CustomerModel currentCustomer = getCurrentUserForCheckout();

		final boolean makeThisAddressTheDefault = addressData.isDefaultAddress()
				|| (currentCustomer.getDefaultShipmentAddress() == null && addressData.isVisibleInAddressBook());

		// Create the new address model
		final AddressModel newAddress = getModelService().create(AddressModel.class);
		getAddressReversePopulator().populate(addressData, newAddress);

		// Store the address against the user
		getCustomerAccountService().saveAddressEntry(currentCustomer, newAddress);

		// Update the address ID in the newly created address
		addressData.setId(newAddress.getId());

		if (makeThisAddressTheDefault)
		{
			getCustomerAccountService().setDefaultAddressEntry(currentCustomer, newAddress);
		}
	}

	@Override
	public void removeAddress(final AddressData addressData)
	{
		validateParameterNotNullStandardMessage("addressData", addressData);
		final CustomerModel currentCustomer = getCurrentUserForCheckout();
		for (final AddressModel addressModel : getCustomerAccountService().getAddressBookEntries(currentCustomer))
		{
			if (addressData.getId().equals(addressModel.getId()))
			{
				getCustomerAccountService().deleteAddressEntry(currentCustomer, addressModel);
				break;
			}
		}
	}
	protected Converter<PrincipalModel, PrincipalData> getPrincipalConverter()
	{
		return principalConverter;
	}

	@Required
	public void setPrincipalConverter(
			Converter<PrincipalModel, PrincipalData> principalConverter)
	{
		this.principalConverter = principalConverter;
	}
}

