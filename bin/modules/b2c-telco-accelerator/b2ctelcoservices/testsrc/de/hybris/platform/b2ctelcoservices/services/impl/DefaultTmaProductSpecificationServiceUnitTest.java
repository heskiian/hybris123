/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.daos.GenericSearchDao;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultGenericSearchDao;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductSpecificationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@UnitTest
public class DefaultTmaProductSpecificationServiceUnitTest
{
	private static final String ME_PHONE_MIN_ID = "ME_PHONE_MIN";
	private static final String ME_SMS_TEXTS_ID = "ME_SMS_TEXTS";
	private static final String ME_DATA_MB_ID = "ME_DATA_MB";
	private static final String VIDEO_ON_DEMAND_ID = "video_on_demand";
	private static final String DOCUMENTARY_ON_DEMAND_ID = "documentary_on_demand";
	private static final String HISTORY_ON_DEMAND_ID = "history_on_demand";
	private static final String BLOCKBUSTER_ON_DEMAND_ID = "blockbuster_on_demand";
	private static final String COMEDY_ON_DEMAND_ID = "comedy_on_demand";
	private static final String DRAMA_ON_DEMAND_ID = "drama_on_demand";
	private TmaProductSpecificationService productSpecificationService;
	private Set<TmaProductSpecCharacteristicModel> tvChannelAllProductSpecCharacteristics;
	private GenericSearchDao tmaProductSpecificationDao;

	@Before
	public void setUp()
	{
		productSpecificationService = new DefaultTmaProductSpecificationService((DefaultGenericSearchDao) tmaProductSpecificationDao);
		tvChannelAllProductSpecCharacteristics = new HashSet<>();
	}

	@Test
	public void testGetProductSpecCharacteristicsForAtomicPsConfiguration()
	{
		final TmaAtomicProductSpecificationModel atomicPs = givenAtomicProductSpecWithServicePlanProductSpecCharacteristics();
		final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristics = whenGetProductSpecCharacteristicsForProductSpecIsCalled(
				atomicPs);
		thenReturnedProductSpecCharacteristicsSetEqualsPsProductCharValues(atomicPs, productSpecCharacteristics);
	}

	@Test
	public void testGetProductSpecCharacteristicsForCompositePsConfiguration() throws Exception
	{
		final TmaCompositeProductSpecificationModel tvChannelProductSpec = givenCompositeProductSpecWithTvChannelProductSpecCharacteristics();
		final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristic = whenGetProductSpecCharacteristicsForProductSpecIsCalled(
				tvChannelProductSpec);
		thenReturnedProductSpecCharacteristicsEqConfiguredTvChannelProductSpecCharacteristics(productSpecCharacteristic);
	}

	private void thenReturnedProductSpecCharacteristicsEqConfiguredTvChannelProductSpecCharacteristics(
			final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristic)
	{
		assertThat(productSpecCharacteristic, is(tvChannelAllProductSpecCharacteristics));
	}

	private Set<TmaProductSpecCharacteristicModel> whenGetProductSpecCharacteristicsForProductSpecIsCalled(
			final TmaProductSpecificationModel productSpec)
	{
		return productSpecificationService.getProductSpecCharacteristicsForPsStructure(productSpec);
	}

	private void thenReturnedProductSpecCharacteristicsSetEqualsPsProductCharValues(
			final TmaAtomicProductSpecificationModel atomicPs,
			final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristic)
	{
		assertThat(atomicPs.getProductSpecCharacteristics(), is(productSpecCharacteristic));
	}

	private TmaAtomicProductSpecificationModel givenAtomicProductSpecWithServicePlanProductSpecCharacteristics()
	{
		final TmaAtomicProductSpecificationModel atomicPs = createAtomicProductSpecForId("SERVICE_PLAN_PS");
		final TmaProductSpecCharacteristicModel productSpecCharacteristic1 = createProductSpecCharacteristicForId(ME_PHONE_MIN_ID);
		final TmaProductSpecCharacteristicModel productSpecCharacteristic2 = createProductSpecCharacteristicForId(ME_SMS_TEXTS_ID);
		final TmaProductSpecCharacteristicModel productSpecCharacteristic3 = createProductSpecCharacteristicForId(ME_DATA_MB_ID);
		atomicPs.setProductSpecCharacteristics(
				new HashSet<>(Arrays.asList(productSpecCharacteristic1, productSpecCharacteristic2, productSpecCharacteristic3)));
		return atomicPs;
	}

	private TmaAtomicProductSpecificationModel createAtomicProductSpecForId(final String id)
	{
		final TmaAtomicProductSpecificationModel atomicPs = new TmaAtomicProductSpecificationModel();
		atomicPs.setId(id);
		return atomicPs;
	}

	private TmaCompositeProductSpecificationModel givenCompositeProductSpecWithTvChannelProductSpecCharacteristics()
	{
		final TmaCompositeProductSpecificationModel tvChannelsProductSpec = createTvChannelProductSpec();
		final TmaProductSpecCharacteristicModel productSpecCharacteristic1 = createAndAddTvChannelProductSpecCharacteristicForId(
				VIDEO_ON_DEMAND_ID);
		tvChannelsProductSpec.setProductSpecCharacteristics(new HashSet<>(Collections.singletonList(productSpecCharacteristic1)));

		final TmaAtomicProductSpecificationModel atomicDocumentaryPs = createAtomicDocumentaryProductSpec();
		final TmaAtomicProductSpecificationModel atomicMoviePs = createAtomicMovieProductSpec();

		tvChannelsProductSpec.setChildren(new HashSet<>(Arrays.asList(atomicDocumentaryPs, atomicMoviePs)));
		return tvChannelsProductSpec;
	}

	private TmaAtomicProductSpecificationModel createAtomicMovieProductSpec()
	{
		final TmaAtomicProductSpecificationModel atomicMoviePs = createAtomicProductSpecForId("MOVIES_PS");
		final TmaProductSpecCharacteristicModel productSpecCharacteristic1 = createAndAddTvChannelProductSpecCharacteristicForId(
				BLOCKBUSTER_ON_DEMAND_ID);
		final TmaProductSpecCharacteristicModel productSpecCharacteristic2 = createAndAddTvChannelProductSpecCharacteristicForId(
				COMEDY_ON_DEMAND_ID);
		final TmaProductSpecCharacteristicModel productSpecCharacteristic3 = createAndAddTvChannelProductSpecCharacteristicForId(
				DRAMA_ON_DEMAND_ID);
		atomicMoviePs.setProductSpecCharacteristics(
				new HashSet<>(Arrays.asList(productSpecCharacteristic1, productSpecCharacteristic2, productSpecCharacteristic3)));
		return atomicMoviePs;
	}

	private TmaAtomicProductSpecificationModel createAtomicDocumentaryProductSpec()
	{
		final TmaAtomicProductSpecificationModel atomicDocumentaryPs = createAtomicProductSpecForId("DOCUMENTARY_PS");
		final TmaProductSpecCharacteristicModel productSpecCharacteristic1 = createAndAddTvChannelProductSpecCharacteristicForId(
				HISTORY_ON_DEMAND_ID);
		final TmaProductSpecCharacteristicModel productSpecCharacteristic2 = createAndAddTvChannelProductSpecCharacteristicForId(
				DOCUMENTARY_ON_DEMAND_ID);
		atomicDocumentaryPs
				.setProductSpecCharacteristics(new HashSet<>(Arrays.asList(productSpecCharacteristic1, productSpecCharacteristic2)));
		return atomicDocumentaryPs;
	}

	private TmaProductSpecCharacteristicModel createAndAddTvChannelProductSpecCharacteristicForId(final String id)
	{
		final TmaProductSpecCharacteristicModel productSpecCharacteristic1 = createProductSpecCharacteristicForId(id);
		tvChannelAllProductSpecCharacteristics.add(productSpecCharacteristic1);
		return productSpecCharacteristic1;
	}

	private TmaCompositeProductSpecificationModel createTvChannelProductSpec()
	{
		final TmaCompositeProductSpecificationModel compositePs = new TmaCompositeProductSpecificationModel();
		compositePs.setId("TV_CHANNEL_PS");
		return compositePs;
	}

	private TmaProductSpecCharacteristicModel createProductSpecCharacteristicForId(final String id)
	{
		final TmaProductSpecCharacteristicModel productSpecCharacteristic = new TmaProductSpecCharacteristicModel();
		productSpecCharacteristic.setId(id);
		return productSpecCharacteristic;
	}

}
