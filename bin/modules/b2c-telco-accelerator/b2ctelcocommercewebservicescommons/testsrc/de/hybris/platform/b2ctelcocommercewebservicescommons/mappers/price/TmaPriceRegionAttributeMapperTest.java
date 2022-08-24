/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commercewebservicescommons.dto.user.RegionWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * Test class for the @{TmaPriceRegionAttributeMapper}
 *
 * @since 1907
 */
@UnitTest
public class TmaPriceRegionAttributeMapperTest
{
	private static final String REGION_CODE = "JP-30";
	TmaPriceRegionAttributeMapper mapper;
	SubscriptionPricePlanData source;
	ProductOfferingPriceWsDTO target;

	@Mock
	MappingContext context;
	@Mock
	MapperFacade mapperFacade;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		mapper = new TmaPriceRegionAttributeMapper();
		mapper.setMapperFacade(mapperFacade);
		source = new SubscriptionPricePlanData();
		target = new ProductOfferingPriceWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final RegionData regionData = new RegionData();
		regionData.setIsocode(REGION_CODE);
		final List<RegionData> regionDatas = new ArrayList<>();
		regionDatas.add(regionData);
		source.setRegions(regionDatas);
		final RegionWsDTO regionWsDto = new RegionWsDTO();
		regionWsDto.setIsocode(REGION_CODE);
		Mockito.when(mapperFacade.map(regionData, RegionWsDTO.class, context)).thenReturn(regionWsDto);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(regionData.getIsocode(), target.getRegion().get(0).getIsocode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullSource()
	{
		mapper.populateTargetAttributeFromSource(null, target, context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullTarget()
	{
		mapper.populateTargetAttributeFromSource(source, null, context);
	}
}
