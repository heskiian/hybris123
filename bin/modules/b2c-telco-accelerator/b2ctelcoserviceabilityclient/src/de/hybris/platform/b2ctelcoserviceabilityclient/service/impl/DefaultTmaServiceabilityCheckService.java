/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;

import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.QUERY_SERVICE_QUALIFICATION;
import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.SEARCH_CRITERIA;

import de.hybris.platform.b2ctelcoserviceabilityclient.service.TmaServiceQualificationClientService;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.serviceability.TmaServiceabilityCheckService;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.Characteristic;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 *
 * Default Implementation of {@link TmaServiceabilityCheckService}
 *
 * @since 2102
 */
public class DefaultTmaServiceabilityCheckService implements TmaServiceabilityCheckService
{
	private final TmaServiceQualificationClientService tmaServiceQualificationClientService;

	private final Converter<AddressModel, GeographicAddress> tmaPlaceConverter;


	/**
	 * This is a class constructor used to specify the required dependencies required by class for creation.
	 *
	 * @param tmaServiceQualificationClientService
	 *           the b2ctelcoServiceabilityClientService
	 * @param tmaPlaceConverter
	 *           the tmaAddressConverter
	 * @return the class constructor initialized with required dependencies
	 */
	public DefaultTmaServiceabilityCheckService(
			final TmaServiceQualificationClientService tmaServiceQualificationClientService,
			final Converter<AddressModel, GeographicAddress> tmaPlaceConverter)
	{
		this.tmaServiceQualificationClientService = tmaServiceQualificationClientService;
		this.tmaPlaceConverter = tmaPlaceConverter;
	}

	@Override
	public boolean checkServiceability(final ProductModel product, final AddressModel installationAddress)
	{
		if (!(product instanceof TmaProductOfferingModel))
		{
			return true;
		}
		final QueryServiceQualification availableServices = getAvailableServicesFromExternalSystem(installationAddress);
		return isProductOfferingServiceable(availableServices, (TmaProductOfferingModel) product);
	}

	private QueryServiceQualification getAvailableServicesFromExternalSystem(final AddressModel installationAddress)
	{
		final QueryServiceQualification queryServiceQualification = TmaQueryServiceQualificationBuilder
				.newTmaQueryServiceQualificationBuilder().withInstantSyncQualification(Boolean.TRUE)
				.withSearchCriteria(TmaServiceQualificationItemBuilder.newTmaServiceQualificationItemBuilder()
						.withService(TmaServiceBuilder.newTmaServiceContextBuilder()
								.withPlace(Arrays.asList(getTmaAddressConverter().convert(installationAddress))).build())
						.withType(SEARCH_CRITERIA).build())
				.withType(QUERY_SERVICE_QUALIFICATION).build();

		return tmaServiceQualificationClientService.createQueryServiceQualification(queryServiceQualification);
	}

	protected Boolean isProductOfferingServiceable(final QueryServiceQualification availableServices,
			final TmaProductOfferingModel product)
	{
		final List<String> availableServiceSpecifications = retrieveAvailableServiceSpecifications(availableServices);
		final List<String> requiredServiceSpecifications = retrieveRequiredServiceSpecifications(product);
		availableServiceSpecifications.retainAll(requiredServiceSpecifications);

		if (CollectionUtils.isEmpty(availableServiceSpecifications))
		{
			return false;
		}
		final Set<TmaServiceSpecCharacteristicValueModel> requiredSscvs = retrieveRequiredServiceSpecificationCharacterstics(
				product);
		final List<Characteristic> availableSscvs = retrieveAvailableServiceSpecificationCharacterstics(availableServices,
				requiredServiceSpecifications);
		final List<Characteristic> serviceableServices = new ArrayList<>();
		availableSscvs.forEach(availableSscv -> {
			requiredSscvs.stream()
					.filter(required -> availableSscv.getName().equalsIgnoreCase(required.getServiceSpecCharacteristic().getId())
							&& availableSscv.getValue().toString()
									.equalsIgnoreCase(required.getValue().concat(" " + required.getUnitOfMeasure().getName())))
					.forEach(required -> serviceableServices.add(availableSscv));
		});

		return (availableSscvs.size() == serviceableServices.size());
	}

	private List<String> retrieveAvailableServiceSpecifications(final QueryServiceQualification availableServices)
	{
		final List<String> serviceSpecificationsAvailableAtAddress = new ArrayList<>();
		if (CollectionUtils.isEmpty(availableServices.getServiceQualificationItem()))
		{
			return serviceSpecificationsAvailableAtAddress;
		}
		if (CollectionUtils.isNotEmpty(availableServices.getServiceQualificationItem()))
		{
			availableServices.getServiceQualificationItem().forEach(qualification -> {
				serviceSpecificationsAvailableAtAddress.add(qualification.getService().getServiceSpecification().getName());
			});
		}
		return serviceSpecificationsAvailableAtAddress;
	}

	private List<String> retrieveRequiredServiceSpecifications(final TmaProductOfferingModel product)
	{
		final List<String> cfsforProduct = new ArrayList<>();
		if ((product.getProductSpecification() != null)
				&& CollectionUtils.isNotEmpty(product.getProductSpecification().getCfsSpecs()))
		{
			product.getProductSpecification().getCfsSpecs().forEach(cfs -> {
				cfsforProduct.add(cfs.getId());
			});
		}
		return cfsforProduct;
	}

	private Set<TmaServiceSpecCharacteristicValueModel> retrieveRequiredServiceSpecificationCharacterstics(
			final TmaProductOfferingModel product)
	{
		final Set<TmaServiceSpecCharacteristicValueModel> requiredSscvs = new HashSet<>();
		product.getProductSpecCharacteristicValues().forEach(pscv -> {
			requiredSscvs.addAll(pscv.getServiceSpecCharacteristicValues());
		});
		return requiredSscvs;
	}

	private List<Characteristic> retrieveAvailableServiceSpecificationCharacterstics(
			final QueryServiceQualification availableServices, final List<String> servicesRequired)
	{
		final List<Characteristic> availableSscvs = new ArrayList<>();
		availableServices.getServiceQualificationItem().stream()
				.filter(qualification -> servicesRequired.contains(qualification.getService().getServiceSpecification().getName()))
				.forEach(qualification -> {
					if (qualification.getService().getServiceCharacteristic() != null)
					{
						availableSscvs.addAll(qualification.getService().getServiceCharacteristic());
					}
				});
		return availableSscvs;
	}

	protected TmaServiceQualificationClientService getB2ctelcoserviceabilityclientService()
	{
		return tmaServiceQualificationClientService;
	}

	protected Converter<AddressModel, GeographicAddress> getTmaAddressConverter()
	{
		return tmaPlaceConverter;
	}

}
