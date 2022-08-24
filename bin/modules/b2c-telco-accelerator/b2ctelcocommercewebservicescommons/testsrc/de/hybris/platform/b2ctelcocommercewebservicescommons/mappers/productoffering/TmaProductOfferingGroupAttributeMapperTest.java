/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OfferingGroupWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

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

import static org.mockito.Mockito.when;


/**
 * JUnit Tests for the @{TmaProductOfferingGroupAttributeMapper}
 */
@UnitTest
public class TmaProductOfferingGroupAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@Mock
	private TmaProductOfferFacade productOfferFacade;

	@InjectMocks
	private final TmaProductOfferingGroupAttributeMapper mapper = new TmaProductOfferingGroupAttributeMapper(productOfferFacade);

	ProductData source;

	MappingContext context;

	ProductWsDTO target;

	TmaOfferingGroupData tmaOfferingGroupData;

	private static final String TMA_ID = "ID1";

	private static final String PRODUCT_OFFERING_ID = "ID2";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductWsDTO();
		source = new ProductData();
		source.setItemType("TmaBundledProductOffering");
		final List<TmaOfferingGroupData> productOfferingGroups = new ArrayList<>();
		tmaOfferingGroupData = new TmaOfferingGroupData();
		tmaOfferingGroupData.setId(TMA_ID);
		productOfferingGroups.add(tmaOfferingGroupData);
		source.setOfferingGroups(productOfferingGroups);
		when(productOfferFacade.isBpo(source)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final List<OfferingGroupWsDTO> offeringGroups = new ArrayList<>();
		final OfferingGroupWsDTO productOfferingGroup = new OfferingGroupWsDTO();
		productOfferingGroup.setId(PRODUCT_OFFERING_ID);
		offeringGroups.add(productOfferingGroup);
		Mockito.when(mapperFacade.map(tmaOfferingGroupData, OfferingGroupWsDTO.class, context)).thenReturn(productOfferingGroup);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(offeringGroups, target.getOfferingGroup());
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
