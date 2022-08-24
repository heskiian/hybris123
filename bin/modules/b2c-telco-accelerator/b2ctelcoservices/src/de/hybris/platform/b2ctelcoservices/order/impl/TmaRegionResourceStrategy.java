/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.impl;

import de.hybris.platform.b2ctelcoservices.data.TmaPlace;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.order.data.TmaCartValidationResult;
import de.hybris.platform.b2ctelcoservices.order.resourcestrategies.TmaAbstractOrderResourceStrategy;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Resource strategy implementation. Validates and updates region resource.
 *
 * @since 2003
 */
public class TmaRegionResourceStrategy implements TmaAbstractOrderResourceStrategy
{
	private final TmaRegionService tmaRegionService;
	private final ModelService modelService;

	public TmaRegionResourceStrategy(final TmaRegionService tmaRegionService, final ModelService modelService)
	{
		this.tmaRegionService = tmaRegionService;
		this.modelService = modelService;
	}

	@Override
	public TmaCartValidationResult validateResource(final CommerceCartParameter parameter)
	{
		final TmaCartValidationResult result = new TmaCartValidationResult();
		result.setValid(true);
		result.setCommerceCartParameter(parameter);
		final List<TmaPlace> inputPlaces = parameter.getPlaces();

		if (CollectionUtils.isEmpty(inputPlaces))
		{
			return result;
		}

		final List<TmaPlace> productRegions = inputPlaces.stream()
				.filter(tmaPlace -> tmaPlace.getRole().equals(TmaPlaceRoleType.PRODUCT_REGION)).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(productRegions))
		{
			return result;
		}
		if (productRegions.size() > 1)
		{
			result.setValid(false);
			result.setMessage("Found multiple regions/places with role: " + TmaPlaceRoleType.PRODUCT_REGION);
			return result;
		}
		if (productRegions.get(0) instanceof TmaRegionPlace)
		{
			final String regionIsoCode = ((TmaRegionPlace) productRegions.get(0)).getRegion().getIsocode();

			try
			{
				getTmaRegionService().findRegionByIsocode(regionIsoCode);
			}
			catch (ModelNotFoundException | AmbiguousIdentifierException e)
			{
				result.setValid(false);
				result.setMessage("Invalid region/place: " + regionIsoCode);
			}
		}
		return result;
	}

	@Override
	public void updateResource(final CommerceCartParameter commerceCartParameter,
			final CommerceCartModification commerceCartModification) throws CommerceCartModificationException
	{
		final List<TmaPlace> places = commerceCartParameter.getPlaces();
		if (CollectionUtils.isEmpty(places))
		{
			return;
		}

		final Optional<TmaPlace> newRegionPlace = places.stream()
				.filter(tmaPlace -> tmaPlace.getRole().equals(TmaPlaceRoleType.PRODUCT_REGION)).findFirst();

		if (!newRegionPlace.isPresent())
		{
			return;
		}
		final RegionModel newRegion = ((TmaRegionPlace) newRegionPlace.get()).getRegion();
		final AbstractOrderEntryModel abstractOrderEntryModel = commerceCartModification.getEntry();
		final RegionModel oldEntryRegion = abstractOrderEntryModel.getRegion();

		if (!ObjectUtils.isEmpty(oldEntryRegion)
				&& StringUtils.equalsIgnoreCase(newRegion.getIsocode(), oldEntryRegion.getIsocode()))
		{
			return;
		}

		abstractOrderEntryModel.setRegion(newRegion);
		getModelService().save(abstractOrderEntryModel);
		getModelService().refresh(abstractOrderEntryModel);
		commerceCartModification.setEntry(abstractOrderEntryModel);
	}

	protected TmaRegionService getTmaRegionService()
	{
		return tmaRegionService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}
}
