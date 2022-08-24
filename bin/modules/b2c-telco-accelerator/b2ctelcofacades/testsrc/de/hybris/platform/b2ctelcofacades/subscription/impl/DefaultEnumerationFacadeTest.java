/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.EnumerationValueData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class DefaultEnumerationFacadeTest
{
	@Mock
	private EnumerationService enumerationService;
	@Mock
	private Converter<HybrisEnumValue, EnumerationValueData> enumerationValueConverter;
	private DefaultEnumerationFacade enumerationFacade;
	private List<HybrisEnumValue> inputModels;
	private List<EnumerationValueData> outputData;
	private List<EnumerationValueData> expectedData;

	@Before
	public void setup()
	{
		initMocks(this);
		this.inputModels = new ArrayList<>();
		this.expectedData = new ArrayList<>();
		this.enumerationFacade = new DefaultEnumerationFacade(TmaServiceType._TYPECODE);
		enumerationFacade.setEnumerationService(enumerationService);
		enumerationFacade.setEnumerationValueConverter(enumerationValueConverter);
	}

	@Test
	public void testGetEnumValues()
	{
		givenEnumModels(TmaServiceType._TYPECODE, TmaServiceType.ADD_ON, TmaServiceType.TARIFF_PLAN);
		whenEnumValuesAreFetched();
		thenEnumDataIs(expectedData);
	}

	private void givenEnumModels(String enumTypeCode, HybrisEnumValue... enumValues)
	{
		inputModels = Arrays.asList(enumValues);
		for (HybrisEnumValue enumValue : enumValues)
		{
			EnumerationValueData data = new EnumerationValueData();
			data.setCode(enumValue.getCode());
			expectedData.add(data);
		}
		when(enumerationService.getEnumerationValues(enumTypeCode)).thenReturn(inputModels);
		when(enumerationValueConverter.convertAll(inputModels)).thenReturn(expectedData);
	}

	private void whenEnumValuesAreFetched()
	{
		this.outputData = enumerationFacade.getValues();
	}

	private void thenEnumDataIs(List<EnumerationValueData> expectedData)
	{
		assertEquals(expectedData, outputData);
	}
}
