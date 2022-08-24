/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoserviceabilityclient.service.impl;


import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.QUERY_SERVICE_QUALIFICATION;
import static de.hybris.platform.b2ctelcoserviceabilityclient.constants.B2ctelcoserviceabilityclientConstants.SEARCH_CRITERIA;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoserviceabilityclient.client.ServiceabilityHttpClient;
import de.hybris.platform.b2ctelcoserviceabilityclient.service.TmaServiceQualificationClientService;
import de.hybris.platform.b2ctelcoservices.model.TmaCustomerFacingServiceSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.Characteristic;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.QueryServiceQualification;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceQualificationItem;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceRefOrValue;
import de.hybris.platform.b2ctelcotmfresources.v4.dto.ServiceSpecificationRef;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Unit Test of {@link DefaultTmaServiceabilityCheckService}.
 *
 * @since 2102
 *
 */
@UnitTest
public class DefaultTmaServiceabilityCheckServiceUnitTest
{
	private static final String USAGE_UNIT = "mbps";

	private DefaultTmaServiceabilityCheckService defaultTmaServiceabilityCheckService;

	@Mock
	private ServiceabilityHttpClient serviceabilityHttpClient;

	@Mock
	private TmaServiceQualificationClientService tmaServiceabilityClientService;

	@Mock
	private Converter<AddressModel, GeographicAddress> tmaAddressConverter;

	private TmaProductOfferingModel serviceableProduct;
	private TmaProductOfferingModel nonServiceableProduct;
	private AddressModel installationAddress;
	private QueryServiceQualification availableServices;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.defaultTmaServiceabilityCheckService = new DefaultTmaServiceabilityCheckService(tmaServiceabilityClientService,
				tmaAddressConverter);

		serviceableProduct = createPO("serviceablePO");
		nonServiceableProduct = createPO("nonServiceablePO");
		installationAddress = createInstallationAddress();

		availableServices = TmaQueryServiceQualificationBuilder.newTmaQueryServiceQualificationBuilder()
				.withInstantSyncQualification(Boolean.TRUE)
				.withSearchCriteria(TmaServiceQualificationItemBuilder.newTmaServiceQualificationItemBuilder()
						.withService(TmaServiceBuilder.newTmaServiceContextBuilder()
								.withPlace(Arrays.asList(tmaAddressConverter.convert(installationAddress))).build())
						.withType(SEARCH_CRITERIA).build())
				.withType(QUERY_SERVICE_QUALIFICATION).build();

		availableServices.setServiceQualificationItem(new ArrayList<>(Arrays.asList(createServiceQualificationItem("500 mbps"))));

	}

	@Test
	public void testPOIsServiceableForNonTmaPO()
	{
		final ProductModel product = new ProductModel();
		product.setCode("nonTmaProduct");
		assertTrue(defaultTmaServiceabilityCheckService.checkServiceability(product, installationAddress));
	}

	@Test
	public void testServiceableWithServiceablePO()
	{
		assertTrue(defaultTmaServiceabilityCheckService.isProductOfferingServiceable(availableServices, serviceableProduct));
	}

	@Test
	public void testServiceableWithNonServiceablePO()
	{

		assertFalse(defaultTmaServiceabilityCheckService.isProductOfferingServiceable(availableServices, nonServiceableProduct));
	}

	private TmaProductOfferingModel createPO(final String code)
	{
		final TmaProductOfferingModel product = new TmaProductOfferingModel();
		product.setCode(code);

		final TmaProductSpecificationModel ps = new TmaProductSpecificationModel();
		ps.setCfsSpecs(new HashSet<>(Arrays.asList(createCFS("copper_internet_svc"))));
		ps.setId("copper_internet_svc");
		product.setProductSpecification(ps);

		product.setProductSpecCharacteristicValues(
				new HashSet<>(Arrays.asList(createPSCV("upload_speed", code.equalsIgnoreCase("serviceablePO") ? "500" : "100"),
						createPSCV("download_speed", code.equalsIgnoreCase("serviceablePO") ? "500" : "100"))));
		return product;
	}

	private TmaCustomerFacingServiceSpecModel createCFS(final String id)
	{
		final TmaCustomerFacingServiceSpecModel cfs = new TmaCustomerFacingServiceSpecModel();
		cfs.setId(id);
		return cfs;
	}

	private TmaProductSpecCharacteristicValueModel createPSCV(final String id, final String value)
	{
		final TmaProductSpecCharacteristicValueModel pscv = new TmaProductSpecCharacteristicValueModel();
		pscv.setServiceSpecCharacteristicValues(new HashSet<>(Arrays.asList(createSSCV(id, value))));
		pscv.setValue(value, Locale.ENGLISH);
		pscv.setUnitOfMeasure(createUsageUnit(USAGE_UNIT));
		pscv.setId(id);
		return pscv;
	}

	private TmaServiceSpecCharacteristicValueModel createSSCV(final String id, final String value)
	{
		final TmaServiceSpecCharacteristicValueModel sscv = new TmaServiceSpecCharacteristicValueModel();
		sscv.setValue(value, Locale.ENGLISH);
		sscv.setUnitOfMeasure(createUsageUnit(USAGE_UNIT));
		sscv.setId(id);
		setLocale(sscv);
		final TmaServiceSpecCharacteristicModel tmaServiceSpecChar = new TmaServiceSpecCharacteristicModel();
		tmaServiceSpecChar.setId(id);
		sscv.setServiceSpecCharacteristic(tmaServiceSpecChar);
		return sscv;
	}

	private UsageUnitModel createUsageUnit(final String usageUnit)
	{
		final UsageUnitModel unit = new UsageUnitModel();
		unit.setId(usageUnit);
		setLocale(unit);
		unit.setName(usageUnit);
		return unit;
	}

	private void setLocale(final ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	private AddressModel createInstallationAddress()
	{
		final AddressModel installationAddress = new AddressModel();
		installationAddress.setInstallationAddress(true);
		installationAddress.setStreetname("street");
		installationAddress.setStreetnumber("1");
		installationAddress.setTown("New York");
		installationAddress.setPostalcode("94121");
		final RegionModel region = new RegionModel();
		region.setIsocode("US-NY");
		installationAddress.setRegion(region);
		final CountryModel country = new CountryModel();
		country.setIsocode("US");
		installationAddress.setCountry(country);
		return installationAddress;
	}

	private ServiceQualificationItem createServiceQualificationItem(final String value)
	{
		final ServiceRefOrValue service = new ServiceRefOrValue();
		service.setServiceSpecification(createServiceSpecification("copper_internet_svc"));
		service.setServiceCharacteristic(new ArrayList<>(Arrays.asList(createServiceCharacteristic("upload_speed", value),
				createServiceCharacteristic("download_speed", value))));

		final ServiceQualificationItem item = new ServiceQualificationItem();
		item.setService(service);
		return item;
	}

	private Characteristic createServiceCharacteristic(final String name, final String value)
	{
		final Characteristic serviceCharacteristic = new Characteristic();
		serviceCharacteristic.setName(name);
		serviceCharacteristic.setValue(value);
		return serviceCharacteristic;
	}

	private ServiceSpecificationRef createServiceSpecification(final String name)
	{
		final ServiceSpecificationRef serviceSpecification = new ServiceSpecificationRef();
		serviceSpecification.setName(name);
		return serviceSpecification;
	}

}
