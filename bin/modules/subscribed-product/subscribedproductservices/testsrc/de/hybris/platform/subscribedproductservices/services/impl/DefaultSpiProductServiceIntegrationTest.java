/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.subscribedproductservices.data.SpiProductContext;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiProductService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * IntegrationTest of {@link SpiProductService}.
 *
 * @since 2105
 */
@IntegrationTest
public class DefaultSpiProductServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String SIGNATURE_UNLIMITED_PLAN_ID = "signatureUnlimitedPlan";
	private static final String CHRISTMAS_HOME_DEAL_ID = "christmasHomeDeal";
	private static final String INVALID_PRODUCT_ID = "invalidId";
	private static final String PARTY_ID = "party_108281";
	private static final String BILLING_ACCOUNT_ID = "batimjames@hybris.com";
	private static final String STATUS = "ACTIVE";
	private static final String PRODUCT_ID = "spiProductId1";
	private static final String PRODUCT_ID_2 = "spiProductId2";

	@Resource
	private SpiProductService spiProductService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_products.impex", "utf-8");
	}

	@Test
	public void testGetProductById()
	{
		final SpiProductModel spiProductModel = getSpiProductService().getProduct(SIGNATURE_UNLIMITED_PLAN_ID);
		Assert.assertEquals(SIGNATURE_UNLIMITED_PLAN_ID, spiProductModel.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testGetProductByIdForIncorrectId()
	{
		getSpiProductService().getProduct(INVALID_PRODUCT_ID);
	}

	@Test
	public void testGetProducts()
	{
		final List<SpiProductModel> productModels = Arrays
				.asList(createSpiProductComponent(SIGNATURE_UNLIMITED_PLAN_ID), createSpiProductComponent(CHRISTMAS_HOME_DEAL_ID));
		Assert.assertEquals(productModels.size(),
				getSpiProductService().getProducts(createProductContext(null, null, null), 0, 2).size());
		Assert.assertTrue(getProductsIds(getSpiProductService().getProducts(createProductContext(null, null, null), 0, 2))
				.containsAll(getProductsIds(productModels)));
	}

	@Test
	public void testGetProductsFilteredByPartyRole()
	{
		final List<SpiProductModel> productModels = Arrays
				.asList(createSpiProductComponent(SIGNATURE_UNLIMITED_PLAN_ID));
		Assert.assertEquals(productModels.size(),
				getSpiProductService().getProducts(createProductContext(PARTY_ID, null, null), 0, 1).size());
		Assert.assertTrue(
				getProductsIds(getSpiProductService().getProducts(createProductContext(PARTY_ID, null, null), 0, 1))
						.containsAll(getProductsIds(productModels)));
	}

	@Test
	public void testGetProductsFilteredByBillingAccount()
	{
		final List<SpiProductModel> productModels = Arrays
				.asList(createSpiProductComponent(CHRISTMAS_HOME_DEAL_ID));
		Assert.assertEquals(productModels.size(),
				getSpiProductService().getProducts(createProductContext(null, BILLING_ACCOUNT_ID, null), 0, 1).size());
		Assert.assertTrue(
				getProductsIds(getSpiProductService().getProducts(createProductContext(null, BILLING_ACCOUNT_ID, null), 0, 1))
						.containsAll(getProductsIds(productModels)));
	}

	@Test
	public void testGetProductsFilteredByStatus()
	{
		final List<SpiProductModel> productModels = Arrays
				.asList(createSpiProductComponent(SIGNATURE_UNLIMITED_PLAN_ID), createSpiProductComponent(CHRISTMAS_HOME_DEAL_ID));
		Assert.assertEquals(productModels.size(),
				getSpiProductService().getProducts(createProductContext(null, null, STATUS), 0, 2).size());
		Assert.assertTrue(getProductsIds(getSpiProductService().getProducts(createProductContext(null, null, STATUS), 0, 2))
				.containsAll(getProductsIds(productModels)));
	}

	@Test
	public void testCreateAndSaveProduct()
	{
		final SpiProductModel productComponentModel = getSpiProductService().createProduct(SpiProductComponentModel.class);
		productComponentModel.setId(PRODUCT_ID);
		getSpiProductService().saveProduct(productComponentModel);

		final SpiProductModel retrievedProductComponentModel = getSpiProductService().getProduct(PRODUCT_ID);
		Assert.assertEquals(PRODUCT_ID, retrievedProductComponentModel.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testRemoveProduct()
	{
		final SpiProductModel productComponentModel = getSpiProductService().createProduct(SpiProductComponentModel.class);
		productComponentModel.setId(PRODUCT_ID_2);
		getSpiProductService().saveProduct(productComponentModel);

		final SpiProductModel retrievedProductComponentModel = getSpiProductService().getProduct(PRODUCT_ID_2);
		Assert.assertEquals(PRODUCT_ID_2, retrievedProductComponentModel.getId());

		getSpiProductService().removeProduct(retrievedProductComponentModel);
		getSpiProductService().getProduct(PRODUCT_ID_2);
	}

	private SpiProductComponentModel createSpiProductComponent(final String id)
	{
		final SpiProductComponentModel spiProductComponentModel = new SpiProductComponentModel();
		spiProductComponentModel.setId(id);
		return spiProductComponentModel;
	}

	private SpiProductContext createProductContext(final String partyId, final String billingId, final String status)
	{
		final SpiProductContext productContext = new SpiProductContext();
		productContext.setRelatedPartyId(partyId);
		productContext.setBillingAccountId(billingId);
		productContext.setStatus(status);
		return productContext;
	}

	private List<String> getProductsIds(final List<SpiProductModel> productModels)
	{
		final List<String> productModelsIds = new ArrayList<>();
		productModels.forEach(spiProductModel -> productModelsIds.add(spiProductModel.getId()));
		return productModelsIds;
	}

	protected SpiProductService getSpiProductService()
	{
		return spiProductService;
	}
}
