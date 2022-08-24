/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * JUnit Tests for the @{TmaCartEntriesPriceAttributeMapper}
 */
@UnitTest
public class TmaCartEntriesPriceAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@Mock
	Map<String, Class<CartCostWsDTO>> priceTypeDtoMap;
	
	@InjectMocks
	private final TmaCartEntriesPriceAttributeMapper mapper = new TmaCartEntriesPriceAttributeMapper();

	MappingContext context;

	OrderEntryData source;

	OrderEntryWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new OrderEntryData();
		target = new OrderEntryWsDTO();

		source.setPrice(priceData());
	}

	protected TmaAbstractOrderUsageChargePriceData priceData()
	{
		final TmaAbstractOrderUsageChargePriceData usagePriceData = new TmaAbstractOrderUsageChargePriceData();

		final BigDecimal value = new BigDecimal("100.00");
		final PriceData dutyFreeValue = new PriceData();
		final BillingTimeData billingTime = new BillingTimeData();
		final PriceData taxIncludedAmount = new PriceData();
		final PriceData taxRate = new PriceData();
		billingTime.setDescription("Pay Now");
		billingTime.setName("Pay On Checkout");
		billingTime.setCode("paynow");
		dutyFreeValue.setValue(value);
		dutyFreeValue.setCurrencyIso("USD");
		taxIncludedAmount.setValue(value);
		taxIncludedAmount.setCurrencyIso("USD");
		taxRate.setValue(value);
		taxRate.setCurrencyIso("USD");
		usagePriceData.setId("44444CLW");
		usagePriceData.setBillingTime(billingTime);
		usagePriceData.setDutyFreeAmount(dutyFreeValue);
		usagePriceData.setTaxIncludedAmount(taxIncludedAmount);
		usagePriceData.setTaxRate(taxRate);
		usagePriceData.setTypeOfPrice(TmaAbstractOrderPriceType.PAYMENT_COST);
		usagePriceData.setTierStart(1);
		usagePriceData.setTierEnd(10);
		final UsageChargeTypeData usageChargeType = new UsageChargeTypeData();
		usageChargeType.setCode("each_respective_tier");
		usagePriceData.setUsageChargeType(usageChargeType);
		final UsageUnitData usageUnit = new UsageUnitData();
		usageUnit.setName("MB/s");
		usagePriceData.setUsageUnit(usageUnit);

		return usagePriceData;
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final List<CartCostWsDTO> cartPrice = new ArrayList<>();
		final CartCostWsDTO price = new CartCostWsDTO();
		final CartCostWsDTO cartCost = new CartCostWsDTO();
		price.setId(priceData().getId());
		cartPrice.add(price);
		cartCost.setCartPrice(cartPrice);

		final TmaAbstractOrderPriceData priceData = source.getPrice();
		Mockito.when(mapperFacade.map(priceData, priceTypeDtoMap.get(priceData.getClass().getSimpleName()), context))
				.thenReturn(cartCost);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getPrice().getId(), target.getCartPrice().getCartPrice().get(0).getId());
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
