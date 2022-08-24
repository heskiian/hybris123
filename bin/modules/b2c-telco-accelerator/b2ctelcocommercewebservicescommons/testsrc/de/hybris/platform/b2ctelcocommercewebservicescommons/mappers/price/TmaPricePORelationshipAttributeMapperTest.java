/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PoRelationshipWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
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
 * JUnit Tests for the @{TmaPricePORelationshipAttributeMapper}
 *
 */
@UnitTest
public class TmaPricePORelationshipAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaPricePORelationshipAttributeMapper mapper = new TmaPricePORelationshipAttributeMapper();

	SubscriptionPricePlanData source;

	MappingContext context;

	ProductOfferingPriceWsDTO target;

	public static final String AFFECTED_PRODUCT = "AFFECTED_PRODUCT";

	public static final String REQUIRED_PRODUCT = "REQUIRED_PRODUCT";

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
		final ProductData affectedProductOffering = new ProductData();
		affectedProductOffering.setCode("PRODUCT_CODE");
		source.setAffectedProductOffering(affectedProductOffering);
		source.setProduct(affectedProductOffering);
		final PoRelationshipWsDTO poAffectedProduct = new PoRelationshipWsDTO();
		Mockito.when(mapperFacade.map(source.getAffectedProductOffering(), PoRelationshipWsDTO.class, context))
				.thenReturn(poAffectedProduct);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(AFFECTED_PRODUCT, poAffectedProduct.getPoAttributeType());
		Assert.assertEquals(source.getProduct().getCode(), target.getPoRelationship().get(0).getBpoId());
	}

	@Test
	public void testForRequiredProduct()
	{
		final List<ProductData> requiredProductOfferings = new ArrayList<>();
		final ProductData requiredProductOffering = new ProductData();
		requiredProductOffering.setCode("PRO_CODE");
		requiredProductOfferings.add(requiredProductOffering);
		source.setRequiredProductOfferings(requiredProductOfferings);
		final PoRelationshipWsDTO poAffectedProduct = new PoRelationshipWsDTO();
		for (final ProductData requiredProductData : source.getRequiredProductOfferings())
		{
			Mockito.when(mapperFacade.map(requiredProductData, PoRelationshipWsDTO.class, context)).thenReturn(poAffectedProduct);
		}
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(REQUIRED_PRODUCT, poAffectedProduct.getPoAttributeType());
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
