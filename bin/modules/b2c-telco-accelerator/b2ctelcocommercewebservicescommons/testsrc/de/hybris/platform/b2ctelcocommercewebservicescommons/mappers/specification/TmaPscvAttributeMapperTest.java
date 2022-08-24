/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPscvAttributeMapper}
 *
 */
@UnitTest
public class TmaPscvAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaPscvAttributeMapper mapper = new TmaPscvAttributeMapper();

	@Mock
	MappingContext context;

	FeatureData fData;

	ProdSpecCharValueUseWsDTO target;

	FeatureValueData featureValueData;

	ProductSpecCharacteristicValueWsDTO productSpecCharacteristicValueWsDTO;

	private static final String CODE = "iPhone_8";
	private static final String CSTIC_COLOR = "Car Color";
	private static final String VALUE_RED = "Dark Red";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProdSpecCharValueUseWsDTO();
		productSpecCharacteristicValueWsDTO = new ProductSpecCharacteristicValueWsDTO();
	}

	private FeatureData mockFeatureAccessories()
	{
		fData = new FeatureData();
		fData.setCode("PowertoolsClassification/1.0/WEC_CDRAGON_CAR.wec_dc_accessory");
		fData.setName(CSTIC_COLOR);
		fData.setType(CODE);
		fData.setComparable(true);
		fData.setRange(false);
		fData.setFeatureValues(mockFeatureValueData(VALUE_RED));
		return fData;
	}

	private static List<FeatureValueData> mockFeatureValueData(final String... valueNames)
	{
		final ArrayList<FeatureValueData> list = new ArrayList<>();
		for (final String valueName : valueNames)
		{
			final FeatureValueData valueData = new FeatureValueData();
			valueData.setValue(valueName);
			list.add(valueData);
		}
		return list;
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mockFeatureAccessories();
		final List<ProductSpecCharacteristicValueWsDTO> prodSpecCharValueList = new ArrayList<>();
		for (final FeatureValueData featureValue : fData.getFeatureValues())
		{
			Mockito.when(mapperFacade.map(featureValue, ProductSpecCharacteristicValueWsDTO.class, context))
					.thenReturn(productSpecCharacteristicValueWsDTO);
		}
		prodSpecCharValueList.add(productSpecCharacteristicValueWsDTO);
		mapper.populateTargetAttributeFromSource(fData, target, context);
		Assert.assertEquals(prodSpecCharValueList, target.getProductSpecCharacteristicValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullSource()
	{
		mapper.populateTargetAttributeFromSource(null, target, context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullTarget()
	{
		mapper.populateTargetAttributeFromSource(fData, null, context);
	}
}
