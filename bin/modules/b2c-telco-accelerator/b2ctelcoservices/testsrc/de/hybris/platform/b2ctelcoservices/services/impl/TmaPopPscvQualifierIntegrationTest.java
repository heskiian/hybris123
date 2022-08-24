/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.impl.TmaPopPscvQualifier;
import de.hybris.platform.b2ctelcoservices.services.TmaPscvService;
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
 * IntegrationTest of {@link TmaPopPscvQualifier}.
 *
 * @since 2105
 */
@IntegrationTest
public class TmaPopPscvQualifierIntegrationTest extends ServicelayerTest
{
	private static final String PSCV_ID_1 = "100_min";
	private static final String PSCV_ID_2 = "100_sms";
	private static final String PSCV_ID_3 = "200_sms";

	private TmaPopPscvQualifier popPscvQualifier;

	@Resource
	private TmaPscvService tmaPscvService;

	@Before
	public void setUp()
	{
		popPscvQualifier = new TmaPopPscvQualifier(tmaPscvService);
	}

	@Test
	public void testValidSimplePopHavingAttachedPscvForFbpo()
	{
		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);
		final TmaSimpleProductOfferingModel spo = createSpo(new HashSet<>(pscvus));
		final Set<TmaProductOfferingModel> children = new HashSet<>(Collections.singletonList(spo));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertTrue(getPopPscvQualifier().qualifies(pop, fbpo));
	}

	@Test
	public void testValidSimplePopNotHavingAttachedPscvForFbpo()
	{
		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(new HashSet<>(Collections.emptyList()));
		final TmaSimpleProductOfferingModel spo = createSpo(new HashSet<>(pscvus));
		final Set<TmaProductOfferingModel> children = new HashSet<>(Collections.singletonList(spo));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertTrue(getPopPscvQualifier().qualifies(pop, fbpo));
	}

	@Test
	public void testValidComposedPopHavingAttachedPscvForFbpo()
	{
		final Set<TmaProductOfferingPriceModel> popChildren =
				new HashSet<>(Arrays.asList(createPop(new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)))),
						createPop(new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_2))))));
		final TmaCompositeProdOfferPriceModel compositePop = createCompositePop(popChildren);

		final Set<TmaProductSpecCharValueUseModel> pscvus_1 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final Set<TmaProductSpecCharValueUseModel> pscvus_2 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_2)));
		final TmaSimpleProductOfferingModel spo1 = createSpo(pscvus_1);
		final TmaSimpleProductOfferingModel spo2 = createSpo(pscvus_2);
		final Set<TmaProductOfferingModel> children = new HashSet<>(Arrays.asList(spo1, spo2));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertTrue(getPopPscvQualifier().qualifies(compositePop, fbpo));
	}

	@Test
	public void testInvalidSimplePopHavingAttachedPscvForFbpo()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_2)));
		final TmaSimpleProductOfferingModel spo = createSpo(pscvus);
		final Set<TmaProductOfferingModel> children = new HashSet<>(Collections.singletonList(spo));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertFalse(getPopPscvQualifier().qualifies(pop, fbpo));
	}

	@Test
	public void testInvalidComposedPopHavingAttachedPscvForFbpo()
	{
		final Set<TmaProductOfferingPriceModel> popChildren =
				new HashSet<>(Arrays.asList(createPop(new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)))),
						createPop(new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_2))))));
		final TmaCompositeProdOfferPriceModel compositePop = createCompositePop(popChildren);

		final Set<TmaProductSpecCharValueUseModel> pscvus_1 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final Set<TmaProductSpecCharValueUseModel> pscvus_3 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_3)));
		final TmaSimpleProductOfferingModel spo_1 = createSpo(pscvus_1);
		final TmaSimpleProductOfferingModel spo_2 = createSpo(pscvus_3);
		final Set<TmaProductOfferingModel> children = new HashSet<>(Arrays.asList(spo_1, spo_2));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertFalse(getPopPscvQualifier().qualifies(compositePop, fbpo));
	}

	@Test
	public void testInvalidSimplePopHavingAttachedPscvForFbpoWithoutPscv()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final TmaSimpleProductOfferingModel spo = createSpo(new HashSet<>());
		final Set<TmaProductOfferingModel> children = new HashSet<>(Collections.singletonList(spo));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(children);

		assertFalse(getPopPscvQualifier().qualifies(pop, fbpo));
	}

	@Test
	public void testValidSimplePopHavingAttachedPscvForSpo()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final TmaSimpleProductOfferingModel spo = createSpo(pscvus);

		assertTrue(getPopPscvQualifier().qualifies(pop, spo));
	}

	@Test
	public void testInvalidSimplePopHavingAttachedPscvForSpo()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final Set<TmaProductSpecCharValueUseModel> pscvus = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_2)));
		final TmaSimpleProductOfferingModel spo = createSpo(pscvus);

		assertFalse(getPopPscvQualifier().qualifies(pop, spo));
	}

	@Test
	public void testValidSimplePopHavingAttachedPscvForBpo()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_1)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final Set<TmaProductSpecCharValueUseModel> pscvus_1 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final Set<TmaProductSpecCharValueUseModel> pscvus_2 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_2)));
		final TmaSimpleProductOfferingModel spo_1 = createSpo(pscvus_1);
		final TmaSimpleProductOfferingModel spo_2 = createSpo(pscvus_2);
		final Set<TmaProductOfferingModel> childrenOfFixedBpo = new HashSet<>(Collections.singletonList(spo_2));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(childrenOfFixedBpo);
		final Set<TmaProductOfferingModel> childrenOfBpo = new HashSet<>(Arrays.asList(spo_1, spo_2, fbpo));
		final TmaBundledProductOfferingModel bpo = createBpo(childrenOfBpo);

		assertTrue(getPopPscvQualifier().qualifies(pop, bpo));
	}

	@Test
	public void testInvalidSimplePopHavingAttachedPscvForBpo()
	{
		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>(Collections.singletonList(createPscv(PSCV_ID_3)));
		final TmaProductOfferingPriceModel pop = createPop(pscvs);

		final Set<TmaProductSpecCharValueUseModel> pscvus_1 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_1)));
		final Set<TmaProductSpecCharValueUseModel> pscvus_2 = new HashSet<>(Collections.singletonList(createPscvu(PSCV_ID_2)));
		final TmaSimpleProductOfferingModel spo_1 = createSpo(pscvus_1);
		final TmaSimpleProductOfferingModel spo_2 = createSpo(pscvus_2);
		final Set<TmaProductOfferingModel> childrenOfFixedBpo = new HashSet<>(Collections.singletonList(spo_2));
		final TmaFixedBundledProductOfferingModel fbpo = createFixedBpo(childrenOfFixedBpo);
		final Set<TmaProductOfferingModel> childrenOfBpo = new HashSet<>(Arrays.asList(spo_1, spo_2, fbpo));
		final TmaBundledProductOfferingModel bpo = createBpo(childrenOfBpo);

		assertFalse(getPopPscvQualifier().qualifies(pop, bpo));
	}

	private TmaFixedBundledProductOfferingModel createFixedBpo(final Set<TmaProductOfferingModel> children)
	{
		final TmaFixedBundledProductOfferingModel fbpo = new TmaFixedBundledProductOfferingModel();
		fbpo.setChildren(children);
		return fbpo;
	}

	private TmaBundledProductOfferingModel createBpo(final Set<TmaProductOfferingModel> children)
	{
		final TmaBundledProductOfferingModel bpo = new TmaBundledProductOfferingModel();
		bpo.setChildren(children);
		return bpo;
	}

	private TmaSimpleProductOfferingModel createSpo(final Set<TmaProductSpecCharValueUseModel> pscvus)
	{
		final TmaSimpleProductOfferingModel spo = new TmaSimpleProductOfferingModel();
		spo.setProductSpecCharValueUses(pscvus);
		return spo;
	}

	private TmaProductSpecCharValueUseModel createPscvu(final String pscvId)
	{
		final TmaProductSpecCharValueUseModel pscvu = new TmaProductSpecCharValueUseModel();
		pscvu.setProductSpecCharacteristicValues(Collections.singletonList(createPscv(pscvId)));
		return pscvu;
	}

	private TmaProductSpecCharacteristicValueModel createPscv(final String id)
	{
		final TmaProductSpecCharacteristicValueModel pscv = new TmaProductSpecCharacteristicValueModel();
		pscv.setId(id);
		return pscv;
	}

	private TmaProductOfferingPriceModel createPop(final Set<TmaProductSpecCharacteristicValueModel> pscvs)
	{
		final TmaProductOfferingPriceModel pop = new TmaProductOfferingPriceModel();
		pop.setProductSpecCharacteristicValues(pscvs);
		return pop;
	}

	private TmaCompositeProdOfferPriceModel createCompositePop(final Set<TmaProductOfferingPriceModel> children)
	{
		final TmaCompositeProdOfferPriceModel composedPop = new TmaCompositeProdOfferPriceModel();
		composedPop.setChildren(children);
		return composedPop;
	}

	protected TmaPopPscvQualifier getPopPscvQualifier()
	{
		return popPscvQualifier;
	}
}
