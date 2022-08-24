/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaTierUsageChargeCompositePopModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPscvService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * UnitTest of {@link TmaPscvService}.
 *
 * @since 2011
 */
@UnitTest
public class TmaPscvServiceUnitTest
{

	private TmaPscvService tmaPscvService;
	private final static String PSCV_VALID_ID_1 = "pscv1";
	private final static String PSCV_VALID_ID_2 = "pscv2";
	private final static String PSCV_INVALID_ID_1 = "invalidPscv1";
	private final static String PSCV_INVALID_ID_2 = "invalidPscv2";
	private final Set<TmaProductSpecCharacteristicValueModel> validPscvs = new HashSet<>();
	private final Set<TmaProductSpecCharacteristicValueModel> invalidPscvs = new HashSet<>();

	@Before
	public void setUp()
	{
		tmaPscvService = new DefaultTmaPscvService();
		validPscvs.add(createPscv(PSCV_VALID_ID_1));
		validPscvs.add(createPscv(PSCV_VALID_ID_2));
		invalidPscvs.add(createPscv(PSCV_INVALID_ID_1));
		invalidPscvs.add(createPscv(PSCV_INVALID_ID_2));
	}

	@Test
	public void testValidPscvsForComponentPop()
	{
		assertEquals(getTmaPscvService().getPscvsUsedFor(createRecurringPopCharge(validPscvs)), validPscvs);
	}

	@Test
	public void testInvalidPscvsForComponentPop()
	{
		assertNotEquals(getTmaPscvService().getPscvsUsedFor(createRecurringPopCharge(validPscvs)), invalidPscvs);
	}

	@Test
	public void testValidPscvsForComposedPop()
	{
		assertEquals(getTmaPscvService().getPscvsUsedFor(createComposedPop(validPscvs)), validPscvs);
	}

	@Test
	public void testValidPscvsForTierUsageComposedPop()
	{
		assertEquals(getTmaPscvService().getPscvsUsedFor(createTierUsageCompositePop(validPscvs)), validPscvs);
	}

	@Test
	public void testInvalidPscvsForComposedPop()
	{
		assertNotEquals(getTmaPscvService().getPscvsUsedFor(createComposedPop(validPscvs)), invalidPscvs);
	}

	@Test
	public void testValidPscvsForSimpleOffering()
	{
		assertEquals(getTmaPscvService().getPscvsUsedFor(createSimpleOffering(validPscvs)), validPscvs);
	}

	@Test
	public void testInvalidPscvsForSimpleOffering()
	{
		assertNotEquals(getTmaPscvService().getPscvsUsedFor(createSimpleOffering(validPscvs)), invalidPscvs);
	}

	@Test
	public void testValidPscvsForBundledOffering()
	{
		assertEquals(getTmaPscvService()
				.getPscvsUsedFor(createBundledOffering(new HashSet<>(Arrays.asList(createSimpleOffering(validPscvs))))), validPscvs);
	}

	@Test
	public void testInvalidPscvsForBundledOffering()
	{
		assertNotEquals(getTmaPscvService()
						.getPscvsUsedFor(createBundledOffering(new HashSet<>(Arrays.asList(createSimpleOffering(validPscvs))))),
				invalidPscvs);
	}

	private TmaUsageProdOfferPriceChargeModel createUsagePopCharge(final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaUsageProdOfferPriceChargeModel usagePopCharge = new TmaUsageProdOfferPriceChargeModel();
		usagePopCharge.setProductSpecCharacteristicValues(pscvs);
		return usagePopCharge;
	}

	private TmaRecurringProdOfferPriceChargeModel createRecurringPopCharge(final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaRecurringProdOfferPriceChargeModel tmaRecurringProdOfferPriceChargeModel = new TmaRecurringProdOfferPriceChargeModel();
		tmaRecurringProdOfferPriceChargeModel
				.setProductSpecCharacteristicValues(pscvs);
		return tmaRecurringProdOfferPriceChargeModel;
	}

	private TmaProductSpecCharacteristicValueModel createPscv(String id)
	{
		final TmaProductSpecCharacteristicValueModel pscv = new TmaProductSpecCharacteristicValueModel();
		pscv.setId(id);
		return pscv;
	}

	private TmaCompositeProdOfferPriceModel createComposedPop(final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPriceModel = new TmaCompositeProdOfferPriceModel();
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel = createUsagePopCharge(pscvs);
		final TmaProductOfferingPriceModel tmaRecurringProdOfferPriceChargeModel = createRecurringPopCharge(pscvs);
		final Set<TmaProductOfferingPriceModel> prices = new HashSet<>(
				Arrays.asList(tmaUsageProdOfferPriceChargeModel, tmaRecurringProdOfferPriceChargeModel));
		compositeProdOfferPriceModel.setChildren(prices);
		return compositeProdOfferPriceModel;
	}

	private TmaTierUsageChargeCompositePopModel createTierUsageCompositePop(Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaTierUsageChargeCompositePopModel tierUsageChargeCompositePopModel = new TmaTierUsageChargeCompositePopModel();
		tierUsageChargeCompositePopModel.setProductSpecCharacteristicValues(pscvs);
		return tierUsageChargeCompositePopModel;
	}

	private TmaProductOfferingModel createSimpleOffering(final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaProductOfferingModel tmaProductOfferingModel = new TmaSimpleProductOfferingModel();
		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>();
		pscvs.forEach((TmaProductSpecCharacteristicValueModel pscv) -> {
			final TmaProductSpecCharValueUseModel pscvu = new TmaProductSpecCharValueUseModel();
			pscvu.setProductSpecCharacteristicValues(Collections.singletonList(pscv));
			pscvus.add(pscvu);
		});

		tmaProductOfferingModel.setProductSpecCharValueUses(pscvus);
		return tmaProductOfferingModel;
	}

	private TmaProductOfferingModel createBundledOffering(final Set<TmaProductOfferingModel> children)
	{
		final TmaBundledProductOfferingModel tmaProductOfferingModel = new TmaBundledProductOfferingModel();
		tmaProductOfferingModel.setChildren(children);
		return tmaProductOfferingModel;
	}

	protected TmaPscvService getTmaPscvService()
	{
		return tmaPscvService;
	}
}
