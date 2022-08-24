/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.address.TmaAddressService;
import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaCartValidationBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.serviceability.TmaServiceabilityService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;


/**
 * Implementation of {@link TmaChecklistActionResolver} to resolve {@link TmaChecklistActionType#INSTALLATION_ADDRESS}
 * actions
 *
 * @since 1911
 */
public class TmaInstallationAddressActionResolver extends DefaultTmaAbstractChecklistActionResolver
{
	private static final Logger LOG = Logger.getLogger(TmaInstallationAddressActionResolver.class);
	private static final String INVALID_MESSAGE = "invalid " + TmaChecklistActionType.INSTALLATION_ADDRESS
			+ " set on the cart entry!";
	private static final String SERVICEABILITY_ERROR_MESSAGE = "The product is not serviceable at given Installation Address ";
	private static final String SERVICEABILITY_CHECK_ENABLED = "default.serviceability.check.enabled";

	private final TmaAddressService addressService;
	private final ConfigurationService configurationService;
	private final TmaServiceabilityService serviceabilityService;
	private String errrorInServicability;

	public TmaInstallationAddressActionResolver(final ModelService modelService,
			final TmaCartValidationBuilder cartValidationBuilder, final TmaAddressService addressService,
			final ConfigurationService configurationService, final TmaServiceabilityService serviceabilityService)
	{
		super(modelService, cartValidationBuilder);
		this.addressService = addressService;
		this.configurationService = configurationService;
		this.serviceabilityService = serviceabilityService;
	}

	@Override
	protected boolean isActionFulfilled(final TmaPolicyActionModel action, final CartEntryModel cartEntry)
	{
		final AddressModel address = cartEntry.getInstallationAddress();
		final UserModel user = cartEntry.getOrder().getUser();
		if (address != null && user != null && getAddressService().doesAddressBelongToUser(address, user))
		{
			this.setErrrorInServicability(SERVICEABILITY_ERROR_MESSAGE);
			return checkServiceability(cartEntry);
		}
		this.setErrrorInServicability(null);
		return false;
	}

	private boolean checkServiceability(final CartEntryModel cartEntry)
	{
		if (getConfigurationService().getConfiguration().getBoolean(SERVICEABILITY_CHECK_ENABLED))
		{
			return getServiceabilityService().isProductOfferingServiceable(cartEntry.getProduct(),
					cartEntry.getInstallationAddress());
		}
		else
		{
			LOG.warn("Serviceability Check has not been performed as GIS system is not configured ");

		}
		return true;
	}

	@Override
	protected String getInvalidMessage()
	{
		if (this.getErrrorInServicability() != null)
		{
			return SERVICEABILITY_ERROR_MESSAGE;
		}

		return INVALID_MESSAGE;

	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected TmaAddressService getAddressService()
	{
		return addressService;
	}

	protected TmaServiceabilityService getServiceabilityService()
	{
		return serviceabilityService;
	}

	protected String getErrrorInServicability()
	{
		return errrorInServicability;
	}

	public void setErrrorInServicability(final String errrorInServicability)
	{
		this.errrorInServicability = errrorInServicability;
	}
}
