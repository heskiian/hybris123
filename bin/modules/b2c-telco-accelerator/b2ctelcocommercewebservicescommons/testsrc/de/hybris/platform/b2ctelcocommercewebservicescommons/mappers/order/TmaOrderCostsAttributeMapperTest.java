/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.order;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;

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
 * JUnit Tests for the @{TmaOrderCostsAttributeMapper}
 */
@UnitTest
public class TmaOrderCostsAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@InjectMocks
	private TmaOrderCostsAttributeMapper mapper;

	OrderData source;

	MappingContext context;

	OrderWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		mapper = new TmaOrderCostsAttributeMapper(mapperFacade);
		source = new OrderData();
		target = new OrderWsDTO();
		final TmaAbstractOrderPriceData price = new TmaAbstractOrderPriceData();
		price.setId("44444KK");

		source.setPrice(price);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final CartCostWsDTO cartCost = new CartCostWsDTO();
		final List<CartCostWsDTO> cartPrices = new ArrayList<>();
		cartPrices.add(cartCost);
		cartCost.setId("44444KK");
		Mockito.when(mapper.getMapperFacade().map(source.getPrice(), CartCostWsDTO.class, context)).thenReturn(cartCost);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getPrice().getId(), target.getOrderCosts().get(0).getId());
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
