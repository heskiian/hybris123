/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderOneTimeChargePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MoneyWsDTO;
import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.UsageChargeTypeData;
import de.hybris.platform.subscriptionfacades.data.UsageUnitData;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
 * JUnit Tests for the @{TmaCartCostChildrenAttributeMapper}
 */

@UnitTest
public class TmaCartCostChildrenAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@Mock
	Map<String, Class<CartCostWsDTO>> priceTypeDtoMap;
	
	@InjectMocks
	private final TmaCartCostChildrenAttributeMapper mapper = new TmaCartCostChildrenAttributeMapper();

	MappingContext context;

	TmaAbstractOrderCompositePriceData source;
	CartCostWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new TmaAbstractOrderCompositePriceData();
		target = new CartCostWsDTO();
	}

	private void setUpTestData()
	{
		final TmaAbstractOrderOneTimeChargePriceData onetimePriceData = new TmaAbstractOrderOneTimeChargePriceData();
		onetimePriceData.setId("44444AA");
		final BillingTimeData billingTime = new BillingTimeData();
		billingTime.setDescription("Pay Now");
		billingTime.setName("Pay On Checkout");
		billingTime.setCode("paynow");
		onetimePriceData.setBillingTime(billingTime);
		final BigDecimal value = new BigDecimal("100.00");
		final PriceData dutyFreeValue = new PriceData();
		dutyFreeValue.setValue(value);
		dutyFreeValue.setCurrencyIso("USD");
		onetimePriceData.setDutyFreeAmount(dutyFreeValue);
		final PriceData taxIncludedAmount = new PriceData();
		taxIncludedAmount.setValue(value);
		taxIncludedAmount.setCurrencyIso("USD");
		onetimePriceData.setTaxIncludedAmount(taxIncludedAmount);
		final PriceData taxRate = new PriceData();
		taxRate.setValue(value);
		taxRate.setCurrencyIso("USD");
		onetimePriceData.setTaxRate(taxRate);
		onetimePriceData.setTypeOfPrice(TmaAbstractOrderPriceType.PAYMENT_COST);

		final TmaAbstractOrderRecurringChargePriceData recurringPriceData = new TmaAbstractOrderRecurringChargePriceData();
		recurringPriceData.setId("44444BB");
		recurringPriceData.setBillingTime(billingTime);
		recurringPriceData.setDutyFreeAmount(dutyFreeValue);
		recurringPriceData.setTaxIncludedAmount(taxIncludedAmount);
		recurringPriceData.setTaxRate(taxRate);
		recurringPriceData.setTypeOfPrice(TmaAbstractOrderPriceType.PAYMENT_COST);
		recurringPriceData.setCycleStart(1);
		recurringPriceData.setCycleEnd(10);

		final TmaAbstractOrderUsageChargePriceData usagePriceData = new TmaAbstractOrderUsageChargePriceData();
		usagePriceData.setId("44444CC");
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

		final Set<TmaAbstractOrderPriceData> childPrices = new HashSet<>();
		childPrices.add(onetimePriceData);
		childPrices.add(recurringPriceData);
		childPrices.add(usagePriceData);
		source.setChildPrices(childPrices);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		setUpTestData();
		final CartCostWsDTO cartCost = new CartCostWsDTO();
		final MoneyWsDTO money = new MoneyWsDTO();
		money.setCurrencyIso("USD");
		money.setValue("100.00");
		for (final TmaAbstractOrderPriceData priceData : source.getChildPrices())
		{
			cartCost.setId("44444CC");
			cartCost.setDescription("Pay Now");
			cartCost.setName("Pay On Checkout");
			cartCost.setDutyFreeAmount(money);
			cartCost.setTaxIncludedAmount(money);
			cartCost.setTaxRate(money);
			cartCost.setPriceType("PAYMENT_COST");
			Mockito.when(mapperFacade.map(priceData, priceTypeDtoMap.get(priceData.getClass().getSimpleName()), context))
					.thenReturn(cartCost);
		}
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getChildPrices().size(), target.getCartPrice().size());
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
