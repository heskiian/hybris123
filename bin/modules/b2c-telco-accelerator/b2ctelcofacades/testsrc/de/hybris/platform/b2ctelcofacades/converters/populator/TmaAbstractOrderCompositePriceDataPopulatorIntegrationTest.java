/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderCompositePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderRecurringChargePriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderUsageChargePriceData;
import de.hybris.platform.b2ctelcofacades.price.converters.TmaAbstractOrderCompositePriceDataPopulator;
import de.hybris.platform.b2ctelcofacades.price.converters.TmaAbstractOrderPriceDataPopulator;
import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderRecurringChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderUsageChargePriceModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.subscriptionservices.enums.UsageChargeType;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;
import de.hybris.platform.testframework.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Required;

import static org.junit.Assert.assertTrue;



/**
 * Test suite for {@link TmaAbstractOrderPriceDataPopulator}
 *
 * @since 1907
 */
@IntegrationTest
public class TmaAbstractOrderCompositePriceDataPopulatorIntegrationTest extends ServicelayerTest
{
	private static final String SAMPLE_CODE = "code";
	private static final String CURRENCY_ISO = "USD";
	private static final String COMPOSITE_PRODUCT_OFFERING_PRICE_ID = "test_composite_pop";

	@Resource
	private TmaAbstractOrderCompositePriceDataPopulator<TmaAbstractOrderCompositePriceModel,
			TmaAbstractOrderCompositePriceData> tmaAbstractOrderCompositePriceDataPopulator;
	@Resource
	private KeyGenerator tmaOrderPriceIdGenerator;
	@Resource
	private ModelService modelService;

	private CurrencyModel currency;

	@Before
	public void setUp() throws Exception
	{
		currency = new CurrencyModel();
		currency.setIsocode(CURRENCY_ISO);
	}

	@Test
	public void testPopulateCompositeWithOTCChildPrice()
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = createOneTimeChargePrice(10.0,
				19.0, 9.0);
		final TmaAbstractOrderCompositePriceModel source = createCompositePriceWithChildren(oneTimeChargePriceModel);
		final TmaAbstractOrderCompositePriceData target = new TmaAbstractOrderCompositePriceData();

		getTmaAbstractOrderCompositePriceDataPopulator().populate(source, target);

		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getCatalogCode(), target.getPriceId());

		Assert.assertEquals(1, target.getChildPrices().size());

		assertChildPrices(source, target);
	}

	@Test
	public void testPopulateCompositeWithTwoOTCChildPrices()
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = createOneTimeChargePrice(10.0,
				19.0, 9.0);
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel2 = createOneTimeChargePrice(100.0,
				199.0, 99.0);
		final TmaAbstractOrderCompositePriceModel source = createCompositePriceWithChildren(oneTimeChargePriceModel,
				oneTimeChargePriceModel2);
		final TmaAbstractOrderCompositePriceData target = new TmaAbstractOrderCompositePriceData();

		getTmaAbstractOrderCompositePriceDataPopulator().populate(source, target);

		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getCatalogCode(), target.getPriceId());

		Assert.assertEquals(2, target.getChildPrices().size());

		assertChildPrices(source, target);
	}

	@Test
	public void testPopulateCompositeWithOTCAndRcChildPrices()
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = createOneTimeChargePrice(10.0,
				19.0, 9.0);
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel =
				createRecurringChargePrice(20.0,
						25.0, 5.0, 2, 6);
		final TmaAbstractOrderCompositePriceModel source = createCompositePriceWithChildren(oneTimeChargePriceModel,
				recurringChargePriceModel);
		final TmaAbstractOrderCompositePriceData target = new TmaAbstractOrderCompositePriceData();

		getTmaAbstractOrderCompositePriceDataPopulator().populate(source, target);

		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getCatalogCode(), target.getPriceId());

		Assert.assertEquals(2, target.getChildPrices().size());

		assertChildPrices(source, target);
	}

	@Test
	public void testPopulateCompositeWithRecurringChargeChildPrices()
	{
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel =
				createRecurringChargePrice(3.5,
						5.0, 1.5, 1);
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel2 =
				createRecurringChargePrice(20.0,
						25.0, 5.0, 2, 6);
		final TmaAbstractOrderCompositePriceModel source = createCompositePriceWithChildren(
				recurringChargePriceModel, recurringChargePriceModel2);
		final TmaAbstractOrderCompositePriceData target = new TmaAbstractOrderCompositePriceData();

		getTmaAbstractOrderCompositePriceDataPopulator().populate(source, target);

		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getCatalogCode(), target.getPriceId());

		Assert.assertEquals(2, target.getChildPrices().size());

		assertChildPrices(source, target);
	}

	@Test
	public void testPopulateCompositeWithOTCRCAndUCChildPrices()
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = createOneTimeChargePrice(10.0,
				19.0, 9.0);
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel =
				createRecurringChargePrice(20.0,
						25.0, 5.0, 2, 6);
		final TmaAbstractOrderUsageChargePriceModel usageChargePriceModel =
				createUsageChargePrice(20.0,
						25.0, 5.0, 2, 6);
		final TmaAbstractOrderUsageChargePriceModel usageChargePriceModel2 =
				createUsageChargePrice(35.0,
						40.0, 15.0, 1, 3);
		final TmaAbstractOrderCompositePriceModel childCompositePriceModel = createCompositePriceWithChildren(
				usageChargePriceModel, usageChargePriceModel2);

		final TmaAbstractOrderCompositePriceModel source = createCompositePriceWithChildren(
				oneTimeChargePriceModel, recurringChargePriceModel, childCompositePriceModel);
		final TmaAbstractOrderCompositePriceData target = new TmaAbstractOrderCompositePriceData();

		getTmaAbstractOrderCompositePriceDataPopulator().populate(source, target);

		Assert.assertEquals(source.getId(), target.getId());
		Assert.assertEquals(source.getCatalogCode(), target.getPriceId());

		Assert.assertEquals(3, target.getChildPrices().size());

		assertChildPrices(source, target);
	}

	protected void assertChildPrices(TmaAbstractOrderCompositePriceModel source, TmaAbstractOrderCompositePriceData target)
	{
		source.getChildPrices().forEach(priceModel -> {
			Optional<TmaAbstractOrderPriceData> matchedPriceData = target.getChildPrices().stream()
					.filter(priceData -> priceData.getId().equals(priceModel.getId())).findFirst();
			{
				assertTrue(matchedPriceData.isPresent());

				if (priceModel instanceof TmaAbstractOrderCompositePriceModel)
				{
					Assert.assertEquals(((TmaAbstractOrderCompositePriceModel) priceModel).getChildPrices().size(),
							((TmaAbstractOrderCompositePriceData) matchedPriceData.get()).getChildPrices().size());
					assertChildPrices((TmaAbstractOrderCompositePriceModel) priceModel,
							(TmaAbstractOrderCompositePriceData) matchedPriceData.get());
				}
				else
				{
					assertEqualDutyFreeAmount(priceModel, matchedPriceData.get());
					assertEqualTaxIncludedAmount(priceModel, matchedPriceData.get());
					assertEqualTaxRateAmount(priceModel, matchedPriceData.get());
					assertEqualBillingTimeAttributes(priceModel, matchedPriceData.get());

					if (priceModel instanceof TmaAbstractOrderRecurringChargePriceModel)
					{
						assertEqualCycleStart(priceModel, matchedPriceData.get());
						assertEqualCycleEnd(priceModel, matchedPriceData.get());
					}
					if (priceModel instanceof TmaAbstractOrderUsageChargePriceModel)
					{
						assertEqualTierStart(priceModel, matchedPriceData.get());
						assertEqualTierEnd(priceModel, matchedPriceData.get());
						assertEqualUsageUnit(priceModel, matchedPriceData.get());
						assertEqualUsageChargeType(priceModel, matchedPriceData.get());
					}
				}
			}
		});
	}

	protected void assertEqualDutyFreeAmount(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getDutyFreeAmount(),
				((TmaAbstractOrderChargePriceData) priceData).getDutyFreeAmount().getValue()
						.doubleValue());
	}

	protected void assertEqualTaxIncludedAmount(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getTaxIncludedAmount(),
				((TmaAbstractOrderChargePriceData) priceData).getTaxIncludedAmount().getValue()
						.doubleValue());
	}

	protected void assertEqualTaxRateAmount(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getTaxRate(),
				((TmaAbstractOrderChargePriceData) priceData).getTaxRate().getValue()
						.doubleValue());
	}

	protected void assertEqualBillingTimeAttributes(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getBillingTime().getCode(),
				((TmaAbstractOrderChargePriceData) priceData).getBillingTime().getCode());
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getBillingTime().getNameInCart(),
				((TmaAbstractOrderChargePriceData) priceData).getBillingTime().getName());
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getBillingTime().getNameInOrder(),
				((TmaAbstractOrderChargePriceData) priceData).getBillingTime().getNameInOrder());
		Assert.assertEquals(((TmaAbstractOrderChargePriceModel) priceModel).getBillingTime().getDescription(),
				((TmaAbstractOrderChargePriceData) priceData).getBillingTime().getDescription());
	}

	protected void assertEqualCycleStart(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderRecurringChargePriceModel) priceModel).getCycleStart(),
				((TmaAbstractOrderRecurringChargePriceData) priceData).getCycleStart());
	}

	protected void assertEqualCycleEnd(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderRecurringChargePriceModel) priceModel).getCycleEnd(),
				((TmaAbstractOrderRecurringChargePriceData) priceData).getCycleEnd());
	}

	protected void assertEqualTierStart(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getTierStart(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getTierStart());
	}

	protected void assertEqualTierEnd(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getTierEnd(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getTierEnd());
	}

	protected void assertEqualUsageUnit(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getUsageUnit().getId(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getUsageUnit().getId());
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getUsageUnit().getNamePlural(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getUsageUnit().getNamePlural());
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getUsageUnit().getName(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getUsageUnit().getName());
	}

	protected void assertEqualUsageChargeType(TmaAbstractOrderPriceModel priceModel, TmaAbstractOrderPriceData priceData)
	{
		Assert.assertEquals(((TmaAbstractOrderUsageChargePriceModel) priceModel).getUsageChargeType().getCode(),
				((TmaAbstractOrderUsageChargePriceData) priceData).getUsageChargeType().getCode());
	}

	protected TmaAbstractOrderCompositePriceModel createCompositePriceWithChildren(
			TmaAbstractOrderPriceModel... childPrices)
	{
		final TmaAbstractOrderCompositePriceModel source = new TmaAbstractOrderCompositePriceModel();
		source.setChildPrices(new HashSet<>(Arrays.asList(childPrices)));
		source.setId((String) tmaOrderPriceIdGenerator.generate());
		source.setCatalogCode(COMPOSITE_PRODUCT_OFFERING_PRICE_ID);
		return source;
	}

	protected TmaAbstractOrderOneTimeChargePriceModel createOneTimeChargePrice(double dutyFreeAmount,
			double taxIncludedAmount, double taxRate)
	{
		final TmaAbstractOrderOneTimeChargePriceModel oneTimeChargePriceModel = new TmaAbstractOrderOneTimeChargePriceModel();
		setGenericValues(oneTimeChargePriceModel, dutyFreeAmount, taxIncludedAmount, taxRate);
		oneTimeChargePriceModel.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		oneTimeChargePriceModel.setCurrency(currency);
		return oneTimeChargePriceModel;
	}

	protected TmaAbstractOrderRecurringChargePriceModel createRecurringChargePrice(double dutyFreeAmount,
			double taxIncludedAmount, double taxRate, int cycleStart)
	{
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel = new TmaAbstractOrderRecurringChargePriceModel();
		setGenericValues(recurringChargePriceModel, dutyFreeAmount, taxIncludedAmount, taxRate);
		recurringChargePriceModel.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		recurringChargePriceModel.setCycleStart(cycleStart);
		recurringChargePriceModel.setCurrency(currency);
		return recurringChargePriceModel;
	}

	protected TmaAbstractOrderRecurringChargePriceModel createRecurringChargePrice(double dutyFreeAmount,
			double taxIncludedAmount, double taxRate, int cycleStart, int cycleEnd)
	{
		final TmaAbstractOrderRecurringChargePriceModel recurringChargePriceModel = createRecurringChargePrice(dutyFreeAmount,
				taxIncludedAmount, taxRate, cycleStart);
		recurringChargePriceModel.setCycleEnd(cycleEnd);
		recurringChargePriceModel.setCurrency(currency);
		return recurringChargePriceModel;
	}

	protected TmaAbstractOrderUsageChargePriceModel createUsageChargePrice(double dutyFreeAmount,
			double taxIncludedAmount, double taxRate, int tierStart, int tierEnd)
	{
		final TmaAbstractOrderUsageChargePriceModel usageChargePriceModel = new TmaAbstractOrderUsageChargePriceModel();
		final UsageUnitModel usageUnit = createSampleUsageUnitModel();
		setGenericValues(usageChargePriceModel, dutyFreeAmount, taxIncludedAmount, taxRate);
		usageChargePriceModel.setPriceType(TmaAbstractOrderPriceType.PRODUCT_PRICE);
		usageChargePriceModel.setTierStart(tierStart);
		usageChargePriceModel.setTierEnd(tierEnd);
		usageChargePriceModel.setUsageUnit(usageUnit);
		usageChargePriceModel.setUsageChargeType(UsageChargeType.HIGHEST_APPLICABLE_TIER);
		usageChargePriceModel.setCurrency(currency);
		return usageChargePriceModel;
	}

	protected void setGenericValues(TmaAbstractOrderChargePriceModel priceModel, double dutyFreeAmount,
			double taxIncludedAmount, double taxRate)
	{
		final BillingTimeModel billingTimeModel = createSampleBillingTimeModel();
		priceModel.setBillingTime(billingTimeModel);
		final String priceId = (String) tmaOrderPriceIdGenerator.generate();
		priceModel.setId(priceId);
		priceModel.setCatalogCode("pop_" + priceId);
		priceModel.setDutyFreeAmount(dutyFreeAmount);
		priceModel.setTaxIncludedAmount(taxIncludedAmount);
		priceModel.setTaxRate(taxRate);
	}

	protected BillingTimeModel createSampleBillingTimeModel()
	{
		final BillingTimeModel billingTimeModel = modelService.create(BillingTimeModel.class);
		setLocaleProvider((ItemModelContextImpl) billingTimeModel.getItemModelContext());
		billingTimeModel.setCode(SAMPLE_CODE);
		billingTimeModel.setNameInCart(SAMPLE_CODE);
		billingTimeModel.setNameInOrder(SAMPLE_CODE);
		billingTimeModel.setDescription(SAMPLE_CODE);
		return billingTimeModel;
	}

	protected UsageUnitModel createSampleUsageUnitModel()
	{
		final UsageUnitModel usageUnit = modelService.create(UsageUnitModel.class);
		setLocaleProvider((ItemModelContextImpl) usageUnit.getItemModelContext());
		usageUnit.setId(SAMPLE_CODE);
		usageUnit.setNamePlural(SAMPLE_CODE);
		usageUnit.setName(SAMPLE_CODE);
		return usageUnit;
	}

	private void setLocaleProvider(ItemModelContextImpl itemModelContext)
	{
		final StubLocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaAbstractOrderCompositePriceDataPopulator<TmaAbstractOrderCompositePriceModel, TmaAbstractOrderCompositePriceData> getTmaAbstractOrderCompositePriceDataPopulator()
	{
		return tmaAbstractOrderCompositePriceDataPopulator;
	}

	@Required
	public void setTmaAbstractOrderCompositePriceDataPopulator(
			TmaAbstractOrderCompositePriceDataPopulator<TmaAbstractOrderCompositePriceModel, TmaAbstractOrderCompositePriceData> tmaAbstractOrderCompositePriceDataPopulator)
	{
		this.tmaAbstractOrderCompositePriceDataPopulator = tmaAbstractOrderCompositePriceDataPopulator;
	}
}
