/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductUsageSpecModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRecurringProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaUsageProdOfferPriceChargeModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.impl.TmaPopUsageSpecQualifier;
import de.hybris.platform.b2ctelcoservices.services.TmaProductUsageSpecService;
import de.hybris.platform.servicelayer.ServicelayerTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * IntegrationTest of {@link TmaPopUsageSpecQualifier}.
 *
 * @since 2011
 */
@IntegrationTest
public class TmaPopUsageSpecQualifierUnitTest extends ServicelayerTest
{
	private static final String ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1 = "00000001";
	private static final String ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_2 = "00000002";
	private static final String ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_3 = "00000003";
	private static final String ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4 = "00000004";

	private TmaPopUsageSpecQualifier tmaPopUsageSpecQualifier;

	@Resource
	private TmaProductUsageSpecService tmaProductUsageSpecService;

	@Before
	public void setUp()
	{
		tmaPopUsageSpecQualifier = new TmaPopUsageSpecQualifier(tmaProductUsageSpecService);
	}

	@Test
	public void testValidPopWithAtUsForPoWitAtPsAndAtPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testWrongPopWithAtUsForPoWitAtPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testValidPopWithAtUsForPoWitAtPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testWrongPopWithAtUsForPoWitAtPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testValidPopWithAtUsForPoWitCompPsAndAtPus(){
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongPopWithAtUsForPoWitCompPsAndAtPus(){
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidPopWithAtUsForPoWitCompPsAndCompPus(){
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongPopWithAtUsForPoWitCompPsAndCompPus(){
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongPopWithCompUsForPoWitAtPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));

	}

	@Test
	public void testValidPopWithCompUsForPoWitAtPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testWrongPopWithCompUsForPoWitAtPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testValidPopWithCompUsForPoWitCompPsAndAtPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongPopWithCompUsForPoWitCompPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidPopWithCompUsForPoWitCompPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongPopWithCompUsForPoWitCompPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(createUsagePopCharge(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithAtUsForPoWitAtPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testValidCompPopWithAtUsForPoWitAtPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testWrongCompPopWithAtUsForPoWitAtPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testValidCompPopWithAtUsForPoWitCompPsAndAtPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithAtUsForPoWitCompPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidCompPopWithAtPsForPoWitCompPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithAtPsForPoWitCompPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithCompUsForPoWitAtPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidCompPopWithCompUsForPoWitAtPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));
	}

	@Test
	public void testWrongCompPopWithCompUsForPoWitAtPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getAtomicProdSpec(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)))));;
	}

	@Test
	public void testValidCompPopWithCompUsForPoWitCompPsAndAtPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithCompUsForPoWitCompPsAndAtPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidCompPopWithCompUsForPoWitCompPsAndCompPus()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testWrongCompPopWithCompUsForPoWitCompPsAndCompPus()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createOffering(getCompositeProdSpec())));
	}

	@Test
	public void testValidPopWithAtUsForFixedBpo(){
		assertTrue(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
				createFixedBpo()));
	}

	@Test
	public void testWrongPopWithAtUsForFixedBpo(){
		assertFalse(getTmaPopUsageSpecQualifier().qualifies(
				createUsagePopCharge(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
				createFixedBpo()));
	}

	@Test
	public void testValidCompPopWithCompUsForFixedBpo()
	{
		assertTrue(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1)),
						createFixedBpo()));
	}

	@Test
	public void testWrongCompPopWithCompUsForFixedBpo()
	{
		assertFalse(getTmaPopUsageSpecQualifier()
				.qualifies(createComposedPop(getCompositeProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_4)),
						createFixedBpo()));
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
		final TmaRecurringProdOfferPriceChargeModel productOfferingPriceModel = new TmaRecurringProdOfferPriceChargeModel();
		return productOfferingPriceModel;
	}

	private TmaProductOfferingModel createOffering(final TmaProductSpecificationModel productSpecificationModel)
	{
		final TmaProductOfferingModel productOfferingModel = new TmaProductOfferingModel();
		productOfferingModel.setProductSpecification(productSpecificationModel);
		return productOfferingModel;
	}

	private TmaFixedBundledProductOfferingModel createFixedBpo()
	{
		final TmaFixedBundledProductOfferingModel fixedBpo = new TmaFixedBundledProductOfferingModel();
		fixedBpo.setChildren(new HashSet<>());
		fixedBpo.getChildren()
				.add(createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1))));
		fixedBpo.getChildren()
				.add(createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_2))));
		final TmaBundledProductOfferingModel bpoChild = new TmaBundledProductOfferingModel();
		bpoChild.setChildren(Collections
				.singleton(createOffering(getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_3)))));
		fixedBpo.getChildren().add(bpoChild);
		return fixedBpo;
	}

	private TmaCompositeProdOfferPriceModel createComposedPop(final TmaProductUsageSpecificationModel productUsageSpec)
	{
		final TmaCompositeProdOfferPriceModel compositeProdOfferPriceModel = new TmaCompositeProdOfferPriceModel();
		final TmaUsageProdOfferPriceChargeModel tmaUsageProdOfferPriceChargeModel = createUsagePopCharge(productUsageSpec);
		final TmaProductOfferingPriceModel tmaRecurringProdOfferPriceChargeModel = createRecurringPopCharge();
		tmaUsageProdOfferPriceChargeModel.setId(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1);
		tmaRecurringProdOfferPriceChargeModel.setId(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_2);
		final Set<TmaProductOfferingPriceModel> prices = new HashSet<>(
				Arrays.asList(tmaUsageProdOfferPriceChargeModel, tmaRecurringProdOfferPriceChargeModel)
		);
		compositeProdOfferPriceModel.setChildren(prices);
		tmaUsageProdOfferPriceChargeModel.setProductUsageSpec(productUsageSpec);
		return compositeProdOfferPriceModel;
	}

	private TmaAtomicProductSpecificationModel getAtomicProdSpec(
			final TmaProductUsageSpecificationModel tmaProductUsageSpecificationModel){
		final TmaAtomicProductSpecificationModel tmaAtomicProductSpecificationModel = new TmaAtomicProductSpecificationModel();
		tmaAtomicProductSpecificationModel.setProductUsageSpecification(tmaProductUsageSpecificationModel);
		return tmaAtomicProductSpecificationModel;
	}

	private TmaCompositeProductSpecificationModel getCompositeProdSpec(){
		final TmaCompositeProductSpecificationModel compositeProductSpecificationModel = new TmaCompositeProductSpecificationModel();
		final TmaAtomicProductSpecificationModel atomicProductSpecificationModel1 = getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1));
		final TmaAtomicProductSpecificationModel atomicProductSpecificationModel2 = getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_2));
		final TmaAtomicProductSpecificationModel atomicProductSpecificationModel3 = getAtomicProdSpec(getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_3));
		final Set<TmaProductSpecificationModel> children = new HashSet<>(
				Arrays.asList(atomicProductSpecificationModel1, atomicProductSpecificationModel2, atomicProductSpecificationModel3)
		);
		compositeProductSpecificationModel.setChildren(children);
		return compositeProductSpecificationModel;
	}

	private TmaAtomicProductUsageSpecModel getAtomicProdUsageSpec(final String atomicProdUsageSpecId){
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel = new TmaAtomicProductUsageSpecModel();
		tmaAtomicProductUsageSpecModel.setId(atomicProdUsageSpecId);
		return tmaAtomicProductUsageSpecModel;
	}

	private TmaCompositeProductUsageSpecModel getCompositeProdUsageSpec(final String atomicPsId){
		final TmaCompositeProductUsageSpecModel tmaCompositeProductUsageSpecModel = new TmaCompositeProductUsageSpecModel();
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel1 =
				getAtomicProdUsageSpec(atomicPsId);
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel2 =
				getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_2);
		final TmaAtomicProductUsageSpecModel tmaAtomicProductUsageSpecModel3 =
				getAtomicProdUsageSpec(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_3);
		tmaCompositeProductUsageSpecModel.setId(ATOMIC_PRODUCT_USAGE_SPECIFICATION_ID_1);
		final Set<TmaProductUsageSpecificationModel> children = new HashSet<>(
				Arrays.asList(tmaAtomicProductUsageSpecModel1, tmaAtomicProductUsageSpecModel2, tmaAtomicProductUsageSpecModel3)
		);
		tmaCompositeProductUsageSpecModel.setChildren(children);
		return tmaCompositeProductUsageSpecModel;
	}

	protected TmaPopUsageSpecQualifier getTmaPopUsageSpecQualifier()
	{
		return tmaPopUsageSpecQualifier;
	}
}