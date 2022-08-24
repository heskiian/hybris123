/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;

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
 * JUnit Tests for the @{TmaPricePOTermAttributeMapper}
 *
 */
@UnitTest
public class TmaPricePOTermAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaPricePOTermAttributeMapper mapper = new TmaPricePOTermAttributeMapper();

	SubscriptionPricePlanData source;

	MappingContext context;

	ProductOfferingPriceWsDTO target;

	

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionPricePlanData();
		target = new ProductOfferingPriceWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final SubscriptionTermData subscriptionTermData = new SubscriptionTermData();
		final List<SubscriptionTermData> subscriptionTerms = new ArrayList<>();
		subscriptionTermData.setId("ID");
		subscriptionTerms.add(subscriptionTermData);
		source.setSubscriptionTerms(subscriptionTerms);
		final ProductOfferingTermWsDTO productOfferingTermWsDto = new ProductOfferingTermWsDTO();
		productOfferingTermWsDto.setId("ID");
		Mockito.when(mapperFacade.map(subscriptionTermData, ProductOfferingTermWsDTO.class, context))
				.thenReturn(productOfferingTermWsDto);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(productOfferingTermWsDto.getId(), target.getProductOfferingTerm().get(0).getId());
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
