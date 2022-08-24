/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;

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
 * JUnit Tests for the @{TmaProdOfferPriceAttributeMapper}
 *
 */
@UnitTest
public class TmaProdOfferPriceAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaProdOfferPriceAttributeMapper mapper = new TmaProdOfferPriceAttributeMapper();

	ProductData source;

	MappingContext context;

	ProductWsDTO target;
	private static final String APPROVAL_STATUS = "approvalStatus";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductWsDTO();
		source = new ProductData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final SubscriptionPricePlanData subscriptionPricePlanData = new SubscriptionPricePlanData();
		subscriptionPricePlanData.setPriceType(PriceDataType.BUY);
		final List<SubscriptionPricePlanData> productOfferingPrices = new ArrayList<>();
		productOfferingPrices.add(subscriptionPricePlanData);
		source.setProductOfferingPrices(productOfferingPrices);
		source.setApprovalStatus(APPROVAL_STATUS);
		final ProductOfferingPriceWsDTO price = new ProductOfferingPriceWsDTO();
		for (final SubscriptionPricePlanData priceData : source.getProductOfferingPrices())
		{
			Mockito.when(mapperFacade.map(priceData, ProductOfferingPriceWsDTO.class, context)).thenReturn(price);
		}
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getApprovalStatus(), target.getProductOfferingPrice().get(0).getLifecycleStatus());
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
