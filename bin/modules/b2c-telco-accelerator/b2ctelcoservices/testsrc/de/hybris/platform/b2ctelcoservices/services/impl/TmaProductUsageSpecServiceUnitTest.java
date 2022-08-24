/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductUsageSpecService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * UnitTest of {@link TmaProductUsageSpecService}.
 *
 * @since 2011
 */
@UnitTest
public class TmaProductUsageSpecServiceUnitTest
{
	private TmaProductUsageSpecService productUsageSpecService;

	@Before
	public void setUp()
	{
		productUsageSpecService = new DefaultTmaProductUsageSpecService();
	}

	@Test
	public void testValidAtPsWithAtPusForOffering()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs = new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertEquals(
				productUsageSpecService
						.getProdUsageSpecificationsFor(createOffering(getAtomicProdSpec(tmaAtomicProductUsageSpecModel))),
				prodUsageSpecs);
	}

	@Test
	public void testWrongAtPsWithAtPusForOffering()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs = new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertNotEquals(
				productUsageSpecService
						.getProdUsageSpecificationsFor(createOffering(getAtomicProdSpec(getAtomicProdUsageSpec()))),
				prodUsageSpecs);
	}

	@Test
	public void testValidAtPsWithCompPusForOffering()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel = getCompositeProdUsageSpec(
				getAtomicProdUsageSpec());
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		prodUsageSpecs.addAll(tmaCompositeProductUsageSpecModel.getChildren());
		assertEquals(
				productUsageSpecService
						.getProdUsageSpecificationsFor(createOffering(getAtomicProdSpec(tmaCompositeProductUsageSpecModel))),
				prodUsageSpecs);
	}

	@Test
	public void testWrongAtPsWithAtCompForOffering()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel = getCompositeProdUsageSpec(
				getAtomicProdUsageSpec());
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		assertNotEquals(productUsageSpecService.getProdUsageSpecificationsFor(
				createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(getAtomicProdUsageSpec())))),
				prodUsageSpecs);
	}

	@Test
	public void testValidCompPsWithAtPusForOffering()
	{
		final TmaCompositeProductSpecificationModel tmaCompositeProductSpecModel = getCompositeProdSpec(
				getAtomicProdSpec(getAtomicProdUsageSpec()));
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs = new HashSet<>();
		prodUsageSpecs.addAll(getPusForCompPs(tmaCompositeProductSpecModel));
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(createOffering(tmaCompositeProductSpecModel)),
				prodUsageSpecs);
	}

	@Test
	public void testWrongCompPsWithAtPusForOffering()
	{
		final TmaCompositeProductSpecificationModel tmaCompositeProductSpecModel = getCompositeProdSpec(
				getAtomicProdSpec(getAtomicProdUsageSpec()));
		final Set<TmaProductUsageSpecificationModel> prodUsageSpecs =
				new HashSet<>(Arrays.asList(tmaCompositeProductSpecModel.getProductUsageSpecification()));
		assertNotEquals(productUsageSpecService
						.getProdUsageSpecificationsFor(createOffering(getCompositeProdSpec(tmaCompositeProductSpecModel))),
				prodUsageSpecs);
	}

	@Test
	public void testValidCompPsWithCompPusForOffering()
	{

		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel =
				getCompositeProdUsageSpec(getAtomicProdUsageSpec());
		final TmaCompositeProductSpecificationModel tmaCompositeProductSpecModel = getCompositeProdSpec(
				getAtomicProdSpec(tmaCompositeProductUsageSpecModel));
		final Set<TmaProductUsageSpecificationModel> children = new HashSet<>();
		children.addAll(getPusForCompPs(tmaCompositeProductSpecModel));
		children.addAll(tmaCompositeProductUsageSpecModel.getChildren());
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(createOffering(tmaCompositeProductSpecModel)),
				children);
	}

	@Test
	public void testWrongCompPsWithCompPusForOffering()
	{
		final TmaCompositeProductSpecificationModel tmaCompositeProductSpecification =
				getCompositeProdSpec(getAtomicProdSpec(getCompositeProdUsageSpec(getAtomicProdUsageSpec())));
		final Set<TmaProductUsageSpecificationModel> tmaProductUsageSpecificationModels = new HashSet<>();
		assertNotEquals(
				productUsageSpecService.getProdUsageSpecificationsFor(createOffering(tmaCompositeProductSpecification)),
				tmaProductUsageSpecificationModels);
	}

	@Test
	public void testValidAtPusForUsageChargePop()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel = createUsagePopCharge(
				tmaAtomicProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaUsageProdOfferPriceChargeModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testWrongAtPusForUsageChargePop()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel = createUsagePopCharge(getAtomicProdUsageSpec());
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertNotEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaUsageProdOfferPriceChargeModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testValidCompPusForUsageChargePop()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel =
				getCompositeProdUsageSpec(getAtomicProdUsageSpec());
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel =
				createUsagePopCharge(tmaCompositeProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		productUsageSpecificationModels.addAll(tmaCompositeProductUsageSpecModel.getChildren());
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaUsageProdOfferPriceChargeModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testWrongCompPusForUsageChargePop()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel =
				getCompositeProdUsageSpec(getAtomicProdUsageSpec());
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel =
				createUsagePopCharge(tmaCompositeProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		assertNotEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaUsageProdOfferPriceChargeModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testValidAtPusForComposedPop()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final TmaCompositeProdOfferPriceModel tmaCompositeProdOfferPriceModel = createComposedPop(tmaAtomicProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaCompositeProdOfferPriceModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testWrongAtPusForComposedPop()
	{
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = getAtomicProdUsageSpec();
		final TmaCompositeProdOfferPriceModel tmaCompositeProdOfferPriceModel = createComposedPop(getAtomicProdUsageSpec());
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaAtomicProductUsageSpecModel));
		assertNotEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaCompositeProdOfferPriceModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testValidCompPusForComposedPop()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel =
				getCompositeProdUsageSpec(getAtomicProdUsageSpec());
		final TmaCompositeProdOfferPriceModel tmaCompositeProdOfferPriceModel = createComposedPop(
				tmaCompositeProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		productUsageSpecificationModels.addAll(tmaCompositeProductUsageSpecModel.getChildren());
		assertEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaCompositeProdOfferPriceModel),
				productUsageSpecificationModels);
	}

	@Test
	public void testWrongCompPusForComposedPop()
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel =
				getCompositeProdUsageSpec(getAtomicProdUsageSpec());
		final TmaCompositeProdOfferPriceModel tmaCompositeProdOfferPriceModel = createComposedPop(
				tmaCompositeProductUsageSpecModel);
		final Set<TmaProductUsageSpecificationModel> productUsageSpecificationModels =
				new HashSet<>(Arrays.asList(tmaCompositeProductUsageSpecModel));
		assertNotEquals(productUsageSpecService.getProdUsageSpecificationsFor(tmaCompositeProdOfferPriceModel),
				productUsageSpecificationModels);
	}

	private TmaProductOfferingModel createOffering(final TmaProductSpecificationModel productSpecificationModel)
	{
		final TmaProductOfferingModel productOfferingModel = new TmaProductOfferingModel();
		productOfferingModel.setProductSpecification(productSpecificationModel);
		return productOfferingModel;
	}

	private TmaAtomicProductSpecificationModel getAtomicProdSpec(
			final TmaProductUsageSpecificationModel tmaProductUsageSpecificationModel)
	{
		final TmaAtomicProductSpecificationModel tmaAtomicProductSpecificationModel = new TmaAtomicProductSpecificationModel();
		tmaAtomicProductSpecificationModel.setProductUsageSpecification(tmaProductUsageSpecificationModel);
		return tmaAtomicProductSpecificationModel;
	}

	private TmaCompositeProductSpecificationModel getCompositeProdSpec(
			final TmaProductSpecificationModel tmaProductSpecificationModel)
	{
		final TmaCompositeProductSpecificationModel compositeProductSpecificationModel = new TmaCompositeProductSpecificationModel();
		final TmaAtomicProductSpecificationModel atomicProductSpecificationModel2 = getAtomicProdSpec(getAtomicProdUsageSpec());
		final TmaAtomicProductSpecificationModel atomicProductSpecificationModel3 = getAtomicProdSpec(getAtomicProdUsageSpec());
		final Set<TmaProductSpecificationModel> children = new HashSet<>(
				Arrays.asList(tmaProductSpecificationModel, atomicProductSpecificationModel2, atomicProductSpecificationModel3)
		);
		compositeProductSpecificationModel.setChildren(children);
		return compositeProductSpecificationModel;
	}

	private TmaAtomicProductUsageSpecModel getAtomicProdUsageSpec()
	{
		return new TmaAtomicProductUsageSpecModel();
	}

	private TmaCompositeProductUsageSpecModel getCompositeProdUsageSpec(
			final TmaProductUsageSpecificationModel tmaProductUsageSpecificationModel)
	{
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel = new TmaCompositeProductUsageSpecModel();
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel2 =
				getAtomicProdUsageSpec();
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel3 =
				getAtomicProdUsageSpec();
		final Set<TmaProductUsageSpecificationModel> children = new HashSet<>(
				Arrays.asList(tmaProductUsageSpecificationModel, tmaAtomicProductUsageSpecModel2, tmaAtomicProductUsageSpecModel3)
		);
		tmaCompositeProductUsageSpecModel.setChildren(children);
		return tmaCompositeProductUsageSpecModel;
	}

	private Set<TmaProductUsageSpecificationModel> getPusForCompPs(
			final TmaCompositeProductSpecificationModel tmaCompositeProductSpecificationModel)
	{
		final Set<TmaProductUsageSpecificationModel> tmaProductUsageSpecificationModels = new HashSet<>();
		final Set<TmaProductSpecificationModel> tmaProductSpecificationModels = new HashSet<>();
		tmaProductSpecificationModels.addAll(tmaCompositeProductSpecificationModel.getChildren());
		final Iterator<TmaProductSpecificationModel> iteratorOfPs = tmaProductSpecificationModels.iterator();
		while (iteratorOfPs.hasNext())
		{
			tmaProductUsageSpecificationModels.add(iteratorOfPs.next().getProductUsageSpecification());
		}
		return tmaProductUsageSpecificationModels;
	}

	private TmaUsageProdOfferPriceChargeModel createUsagePopCharge(
			final TmaProductUsageSpecificationModel prodUsageSpec)
	{
		final TmaUsageProdOfferPriceChargeModel usagePopCharge = new TmaUsageProdOfferPriceChargeModel();
		usagePopCharge.setProductUsageSpec(prodUsageSpec);
		return usagePopCharge;
	}

	private TmaRecurringProdOfferPriceChargeModel createRecurringPopCharge()
	{
		return new TmaRecurringProdOfferPriceChargeModel();
	}

	private TmaCompositeProdOfferPriceModel createComposedPop(final TmaProductUsageSpecificationModel productUsageSpec)
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPriceModel = new TmaCompositeProdOfferPriceModel();
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel = createUsagePopCharge(productUsageSpec);
		final TmaProductOfferingPriceModel tmaRecurringProdOfferPriceChargeModel = createRecurringPopCharge();
		final Set<TmaProductOfferingPriceModel> prices = new HashSet<>(
				Arrays.asList(tmaUsageProdOfferPriceChargeModel, tmaRecurringProdOfferPriceChargeModel));
		compositeProdOfferPriceModel.setChildren(prices);
		tmaUsageProdOfferPriceChargeModel.setProductUsageSpec(productUsageSpec);
		return compositeProdOfferPriceModel;
	}
}
