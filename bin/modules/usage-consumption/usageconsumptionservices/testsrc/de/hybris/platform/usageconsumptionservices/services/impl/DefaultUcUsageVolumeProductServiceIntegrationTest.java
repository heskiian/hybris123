/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.usageconsumptionservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;
import de.hybris.platform.usageconsumptionservices.services.UcUsageVolumeProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultUcUsageVolumeProductServiceIntegrationTest extends ServicelayerTransactionalTest
{

	private static final String MAIN_OFFER_DATA_ID = "MainOffer-Data";
	private static final String MAIN_OFFER_NATIONAL_VOICE_ID = "MainOffer-NationalVoice";
	private static final String MAIN_OFFER_NATIONAL_VOICE_2_ID = "MainOffer-NationalVoice2";
	private static final String PARTY_ID = "party_10830";
	private static final String PRODUCT_ID = "MainOffer2";
	private static final String PUBLIC_IDENTIFIER_ID = "0601010101";

	@Resource
	private UcUsageVolumeProductService ucUsageVolumeProductService;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/test/impex/test_usage_volume_product.impex", "utf-8");
	}

	@Test
	public void testGetUsageVolumeProducts()
	{
		final List<UcUsageVolumeProductModel> usageVolumeProductModels = Arrays
				.asList(createUcUsageVolumeProduct(MAIN_OFFER_DATA_ID), createUcUsageVolumeProduct(MAIN_OFFER_NATIONAL_VOICE_ID),
						createUcUsageVolumeProduct(MAIN_OFFER_NATIONAL_VOICE_2_ID));
		Assert.assertEquals(usageVolumeProductModels.size(),
				getUcUsageVolumeProductService().getUsageVolumeProducts(createUsageVolumeProductContext(null, null, null), 0, 3)
						.size());
		Assert.assertTrue(getUsageVolumeProductIds(
				getUcUsageVolumeProductService().getUsageVolumeProducts(createUsageVolumeProductContext(null, null, null), 0, 3))
				.containsAll(getUsageVolumeProductIds(usageVolumeProductModels)));
	}

	@Test
	public void testGetUsageVolumeProductsFilteredByPartyRole()
	{
		final List<UcUsageVolumeProductModel> usageVolumeProductModels = Arrays
				.asList(createUcUsageVolumeProduct(MAIN_OFFER_DATA_ID), createUcUsageVolumeProduct(MAIN_OFFER_NATIONAL_VOICE_ID));
		Assert.assertEquals(usageVolumeProductModels.size(),
				getUcUsageVolumeProductService().getUsageVolumeProducts(createUsageVolumeProductContext(PARTY_ID, null, null), 0, 2)
						.size());
		Assert.assertTrue(
				getUsageVolumeProductIds(
						getUcUsageVolumeProductService()
								.getUsageVolumeProducts(createUsageVolumeProductContext(PARTY_ID, null, null), 0, 2))
						.containsAll(getUsageVolumeProductIds(usageVolumeProductModels)));
	}

	@Test
	public void testGetUsageVolumeProductsFilteredByProductId()
	{
		final List<UcUsageVolumeProductModel> specificationModels = Arrays
				.asList(createUcUsageVolumeProduct(MAIN_OFFER_NATIONAL_VOICE_2_ID));
		Assert.assertEquals(specificationModels.size(),
				getUcUsageVolumeProductService().getUsageVolumeProducts(createUsageVolumeProductContext(null, PRODUCT_ID, null), 0, 1)
						.size());
		Assert.assertTrue(getUsageVolumeProductIds(
				getUcUsageVolumeProductService()
						.getUsageVolumeProducts(createUsageVolumeProductContext(null, PRODUCT_ID, null), 0, 1))
				.containsAll(getUsageVolumeProductIds(specificationModels)));
	}

	@Test
	public void testGetUsageVolumeProductsFilteredByPublicIdentifier()
	{
		final List<UcUsageVolumeProductModel> specificationModels = Arrays
				.asList(createUcUsageVolumeProduct(MAIN_OFFER_DATA_ID), createUcUsageVolumeProduct(MAIN_OFFER_NATIONAL_VOICE_ID));
		Assert.assertEquals(specificationModels.size(),
				getUcUsageVolumeProductService()
						.getUsageVolumeProducts(createUsageVolumeProductContext(null, null, PUBLIC_IDENTIFIER_ID), 0, 2)
						.size());
		Assert.assertTrue(getUsageVolumeProductIds(
				getUcUsageVolumeProductService()
						.getUsageVolumeProducts(createUsageVolumeProductContext(null, null, PUBLIC_IDENTIFIER_ID), 0, 2))
				.containsAll(getUsageVolumeProductIds(specificationModels)));
	}

	private UcUsageVolumeProductModel createUcUsageVolumeProduct(final String id)
	{
		final UcUsageVolumeProductModel usageVolumeProductModel = new UcUsageVolumeProductModel();
		usageVolumeProductModel.setId(id);
		return usageVolumeProductModel;
	}

	private UcUsageVolumeProductContext createUsageVolumeProductContext(final String partyId, final String productId,
			String publicIdentifier)
	{
		final UcUsageVolumeProductContext usageVolumeProductContext = new UcUsageVolumeProductContext();
		usageVolumeProductContext.setRelatedPartyId(partyId);
		usageVolumeProductContext.setProductId(productId);
		usageVolumeProductContext.setPublicIdentifier(publicIdentifier);
		return usageVolumeProductContext;
	}

	private List<String> getUsageVolumeProductIds(final List<UcUsageVolumeProductModel> usageVolumeProductModels)
	{
		final List<String> usageVolumeProductModelsIds = new ArrayList<>();
		usageVolumeProductModels
				.forEach(usageVolumeProductModel -> usageVolumeProductModelsIds.add(usageVolumeProductModel.getId()));
		return usageVolumeProductModelsIds;
	}

	public UcUsageVolumeProductService getUcUsageVolumeProductService()
	{
		return ucUsageVolumeProductService;
	}
}
