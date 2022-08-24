/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.resourcestrategies.impl;

import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.data.TmaAddressPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.order.validators.TmaPlaceValidator;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;


/**
 * Resource strategy implementation. Validates and updates installation address resource.
 *
 * @since 1911
 */
public class TmaInstallationAddressResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private TmaPlaceValidator placeValidator;
	private ModelService modelService;

	public TmaInstallationAddressResourceStrategy(TmaPlaceValidator placeValidator, ModelService modelService)
	{
		this.placeValidator = placeValidator;
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(CommerceCartParameter parameter)
	{
		return getPlaceValidator().validate(TmaPlaceRoleType.INSTALLATION_ADDRESS, parameter);
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification)
	{
		if (CollectionUtils.isEmpty(commerceCartParameter.getPlaces()))
		{
			return;
		}

		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();

		final List<TmaPlace> inputPlaces = commerceCartParameter.getPlaces();

		if (CollectionUtils.isEmpty(inputPlaces))
		{
			return;
		}

		final Optional<TmaPlace> installationPlace =
				inputPlaces.stream().filter(tmaPlace -> tmaPlace.getRole().equals(TmaPlaceRoleType.INSTALLATION_ADDRESS)).findFirst();

		if (!installationPlace.isPresent())
		{
			return;
		}

		final AddressModel address = ((TmaAddressPlace) installationPlace.get()).getAddress();

		if (!address.getInstallationAddress())
		{
			throw new IllegalArgumentException("The address '" + address.getId() + "' is not an installation address.");
		}

		address.setInstallationAddress(true);
		getModelService().save(address);
		abstractOrderEntryModel.setInstallationAddress(address);
		commerceCartModification.setEntry(abstractOrderEntryModel);
		commerceCartModification.setInstallationAddress(address);
		getModelService().save(abstractOrderEntryModel);
	}

	protected TmaPlaceValidator getPlaceValidator()
	{
		return placeValidator;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
