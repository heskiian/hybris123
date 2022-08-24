/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.math.BigDecimal;

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
 * JUnit Tests for the @{TmaCartCostDutyFreeValueAttributeMapper}
 */

@UnitTest
public class TmaCartCostDutyFreeValueAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaCartCostDutyFreeValueAttributeMapper mapper = new TmaCartCostDutyFreeValueAttributeMapper();

	MappingContext context;

	TmaAbstractOrderChargePriceData source;
	CartCostWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new TmaAbstractOrderChargePriceData();
		target = new CartCostWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final PriceData dutyFreeValue = new PriceData();
		dutyFreeValue.setValue(new BigDecimal("100.00"));
		dutyFreeValue.setCurrencyIso("USD");
		source.setDutyFreeAmount(dutyFreeValue);
		final MoneyWsDTO dutyFreeAmount = new MoneyWsDTO();
		dutyFreeAmount.setCurrencyIso("USD");
		dutyFreeAmount.setValue("100.00");
		Mockito.when(mapperFacade.map(dutyFreeValue, MoneyWsDTO.class, context)).thenReturn(dutyFreeAmount);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getDutyFreeAmount().getCurrencyIso(), target.getDutyFreeAmount().getCurrencyIso());
		Assert.assertEquals(source.getDutyFreeAmount().getValue().toString(), target.getDutyFreeAmount().getValue());
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


