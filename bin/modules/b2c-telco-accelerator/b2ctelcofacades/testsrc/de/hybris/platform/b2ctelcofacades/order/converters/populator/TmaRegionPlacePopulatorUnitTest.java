/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaPlaceData;
import de.hybris.platform.b2ctelcoservices.data.TmaRegionPlace;
import de.hybris.platform.b2ctelcoservices.enums.TmaPlaceRoleType;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.core.model.c2l.RegionModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaRegionPlacePopulator}
 *
 * @since 2003
 */
@UnitTest
public class TmaRegionPlacePopulatorUnitTest
{
	private static final String EXISTING_REGION_ID = "JP-20";
	private static final String NON_EXISTING_REGION_ID = "JP-25";

	@Mock
	private TmaRegionService tmaRegionService;

	private TmaRegionPlacePopulator tmaRegionPlacePopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		tmaRegionPlacePopulator = new TmaRegionPlacePopulator(tmaRegionService);
	}

	@Test
	public void testPopulate()
	{
		final RegionModel region = setRegionData();
		when(tmaRegionService.findRegionByIsocode(EXISTING_REGION_ID)).thenReturn(region);

		final TmaPlaceData source = mock(TmaPlaceData.class);
		given(source.getId()).willReturn(EXISTING_REGION_ID);
		given(source.getRole()).willReturn(TmaPlaceRoleType.PRODUCT_REGION);

		final TmaRegionPlace target = new TmaRegionPlace();
		tmaRegionPlacePopulator.populate(source, target);
		Assert.assertNotNull(target.getRegion());
		Assert.assertEquals(source.getId(), target.getRegion().getIsocode());
	}

	private RegionModel setRegionData()
	{
		final RegionModel region = mock(RegionModel.class);
		given(region.getIsocode()).willReturn(EXISTING_REGION_ID);
		return region;
	}
}
