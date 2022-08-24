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
 * JUnit Tests for the @{TmaCartCostTaxRateAttributeMapper}
 */

@UnitTest
public class TmaCartCostTaxRateAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaCartCostTaxRateAttributeMapper mapper = new TmaCartCostTaxRateAttributeMapper();

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
		final PriceData taxRate = new PriceData();
		taxRate.setValue(new BigDecimal("100.00"));
		taxRate.setCurrencyIso("USD");
		source.setTaxRate(taxRate);
		final MoneyWsDTO taxRateAmount = new MoneyWsDTO();
		taxRateAmount.setCurrencyIso("USD");
		taxRateAmount.setValue("100.00");
		Mockito.when(mapperFacade.map(taxRate, MoneyWsDTO.class, context)).thenReturn(taxRateAmount);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getTaxRate().getCurrencyIso(), target.getTaxRate().getCurrencyIso());
		Assert.assertEquals(source.getTaxRate().getValue().toString(), target.getTaxRate().getValue());
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
