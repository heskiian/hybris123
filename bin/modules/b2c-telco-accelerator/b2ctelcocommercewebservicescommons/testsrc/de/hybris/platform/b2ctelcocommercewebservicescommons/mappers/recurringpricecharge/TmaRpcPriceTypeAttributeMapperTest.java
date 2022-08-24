/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.recurringpricecharge;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RecurringPriceChargeWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaRpcPriceTypeAttributeMapper}
 *
 */
@UnitTest
public class TmaRpcPriceTypeAttributeMapperTest
{
	@InjectMocks
	private final TmaRpcPriceTypeAttributeMapper mapper = new TmaRpcPriceTypeAttributeMapper();

	RecurringChargeEntryData source;

	MappingContext context;

	RecurringPriceChargeWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new RecurringPriceChargeWsDTO();
		source = new RecurringChargeEntryData();
		final PriceData price = new PriceData();
		price.setValue(new BigDecimal(7));
		price.setCurrencyIso("USD");
		source.setPrice(price);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final MoneyWsDTO rpcPrice = new MoneyWsDTO();
		rpcPrice.setCurrencyIso(source.getPrice().getCurrencyIso());
		rpcPrice.setValue(source.getPrice().getValue().toString());
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(rpcPrice.getCurrencyIso(), target.getPrice().getCurrencyIso());
		Assert.assertEquals(rpcPrice.getValue(), target.getPrice().getValue());
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
