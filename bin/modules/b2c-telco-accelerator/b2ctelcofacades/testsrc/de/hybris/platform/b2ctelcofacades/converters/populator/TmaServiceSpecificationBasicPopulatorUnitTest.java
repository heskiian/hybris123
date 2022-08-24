/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecificationModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Unit Test of {@link TmaServiceSpecificationBasicPopulator}.
 *
 * @since 2102
 */
@UnitTest
public class TmaServiceSpecificationBasicPopulatorUnitTest
{
	private static final String SERVICE_SPEC_ID = "gas_svc";
	private static final String SERVICE_SPEC_NAME = "Gas Service";

	private TmaServiceSpecificationModel source;
	private TmaServiceSpecificationData target;

	private TmaServiceSpecificationBasicPopulator populator;

	@Mock
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new TmaServiceSpecificationBasicPopulator(externalIdentifierConverter);
		target = new TmaServiceSpecificationData();
	}

	@Test
	public void testPopulate()
	{
		givenSource(SERVICE_SPEC_ID);
		whenPopulateIsRun();
		thenTheTargetIsPopulated();
	}

	private void givenSource(final String id)
	{
		source = new TmaServiceSpecificationModel();
		setLocale();
		source.setId(id);
		source.setName(SERVICE_SPEC_NAME);
		source.setExternalIds(new HashSet<>(getExternalIdentifiers()));
	}

	private void whenPopulateIsRun()
	{
		when(externalIdentifierConverter.convertAll(source.getExternalIds()))
				.thenReturn(getExternalServiceSpecIds());
		populator.populate(source, target);
	}

	private List<TmaExternalIdentifierModel> getExternalIdentifiers()
	{
		final List<TmaExternalIdentifierModel> externalIdentifierDataList = new ArrayList<>();

		final TmaExternalIdentifierModel externalIdentifierModel = new TmaExternalIdentifierModel();
		externalIdentifierModel.setBillingSystemExtId("gas_service_spec");
		externalIdentifierModel.setBillingSystemId("amdocs");
		externalIdentifierModel.setResourceType("ServiceSpecification");
		externalIdentifierDataList.add(externalIdentifierModel);

		return externalIdentifierDataList;
	}

	private List<TmaExternalIdentifierData> getExternalServiceSpecIds()
	{
		final List<TmaExternalIdentifierData> externalIdentifierDataList = new ArrayList<>();

		final TmaExternalIdentifierData externalIdentifierData = new TmaExternalIdentifierData();
		externalIdentifierData.setId("gas_service_spec");
		externalIdentifierData.setOwner("amdocs");
		externalIdentifierData.setResourceType("ServiceSpecification");
		externalIdentifierDataList.add(externalIdentifierData);

		return externalIdentifierDataList;
	}

	private void thenTheTargetIsPopulated()
	{
		assertTrue("It hasn't the expected id", source.getId().equals(target.getId()));
		assertTrue("It hasn't the expected Name", source.getName().equals(target.getName()));

		final List<TmaExternalIdentifierData> externalIds = target.getExternalIds();
		assertEquals(1, externalIds.size(), 1);
		assertEquals("gas_service_spec", externalIds.get(0).getId());
		assertEquals("amdocs", externalIds.get(0).getOwner());
		assertEquals("ServiceSpecification", externalIds.get(0).getResourceType());
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}
