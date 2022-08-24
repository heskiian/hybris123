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
 * JUnit Tests for the @{TmaCartCostTaxPriceAttributeMapper}
 */

@UnitTest
public class TmaCartCostTaxPriceAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaCartCostTaxPriceAttributeMapper mapper = new TmaCartCostTaxPriceAttributeMapper();

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
		final PriceData taxIncludedAmount = new PriceData();
		taxIncludedAmount.setValue(new BigDecimal("100.00"));
		taxIncludedAmount.setCurrencyIso("USD");
		source.setTaxIncludedAmount(taxIncludedAmount);
		final MoneyWsDTO taxIncludedValue = new MoneyWsDTO();
		taxIncludedValue.setCurrencyIso("USD");
		taxIncludedValue.setValue("100.00");
		Mockito.when(mapperFacade.map(taxIncludedAmount, MoneyWsDTO.class, context)).thenReturn(taxIncludedValue);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getTaxIncludedAmount().getCurrencyIso(), target.getTaxIncludedAmount().getCurrencyIso());
		Assert.assertEquals(source.getTaxIncludedAmount().getValue().toString(), target.getTaxIncludedAmount().getValue());
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
