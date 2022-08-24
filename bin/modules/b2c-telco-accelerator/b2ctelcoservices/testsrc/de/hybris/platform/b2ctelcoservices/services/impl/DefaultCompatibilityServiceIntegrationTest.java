/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.services.CompatibilityService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


@IntegrationTest
public class DefaultCompatibilityServiceIntegrationTest extends ServicelayerTest
{
	private static final Logger LOG = Logger.getLogger(DefaultCompatibilityServiceIntegrationTest.class);

	private static final String TEST_PRODUCT_CODE_NO_MANUFACTURER = "Accessory8";

	@Resource
	private CompatibilityService compatibilityService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Before
	public void setUp() throws Exception
	{
		LOG.info("Creating data for DefaultCompatibilityServiceIntegrationTest ..");
		final long startTime = System.currentTimeMillis();
		new CoreBasicDataCreator().createEssentialData(Collections.EMPTY_MAP, null);
		importCsv("/test/impex/test_feature-compatibility.impex", "utf-8");
		LOG.info("Finished data for DefaultCompatibilityServiceIntegrationTest "
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	@Test
	public void testFindFeatureCompatibleProducts()
	{
		final List<ProductModel> compatibleProducts = compatibilityService.getFeatureCompatibleProducts("109059",
				getClassAttributeAssignment());
		this.assertCompatibleListIsCorrect(compatibleProducts,
				Sets.newHashSet("109058", "009058", "Device2", "Device3", "Accessory3", "Accessory4", "Accessory5",
						"Accessory6", "Accessory7", "Accessory8"));
	}

	@Test
	public void testShouldNotFindIncompatibleFeatureAccessories()
	{
		final List<ProductModel> featureCompatibleProducts = compatibilityService.getFeatureCompatibleProducts("199919",
				getClassAttributeAssignment());

		Assert.assertTrue(featureCompatibleProducts.isEmpty());
		final Set<String> emptySet = Sets.newHashSet();
		this.assertCompatibleListIsCorrect(featureCompatibleProducts, emptySet);
	}

	/**
	 * Should return products that are only vendor compatible and not feature compatible.
	 */
	@Test
	public void testFindVendorCompatibleProducts()
	{
		final List<ProductModel> vendorCompatibleProducts = compatibilityService
				.getProductsForVendorCompatibility("109058");
		this.assertCompatibleListIsCorrect(vendorCompatibleProducts, Sets.newHashSet("109060"));
	}

	@Test
	public void testFindVendorCompatibleProductsForEmptyManufacturerName()
	{
		final List<ProductModel> vendorCompatibleProducts = compatibilityService
				.getProductsForVendorCompatibility(TEST_PRODUCT_CODE_NO_MANUFACTURER);
		assertThat(vendorCompatibleProducts, equalTo(Collections.EMPTY_LIST));
	}

	private ClassAttributeAssignmentModel getClassAttributeAssignment()
	{
		final String queryStr =
				"select {caa:pk} from {ClassAttributeAssignment as caa JOIN ClassificationAttribute as ca on {ca:pk} = {caa:classificationattribute}}"
						+ " where {ca:code} = 'featurecompatibility'";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStr);
		return flexibleSearchService.searchUnique(query);
	}

	protected void assertCompatibleListIsCorrect(final List<? extends ProductModel> returnedProducts,
			final Set<String> expectedCodes)
	{
		Assert.assertEquals(expectedCodes.size(), returnedProducts.size());
		final Collection returnedCodes = CollectionUtils.collect(returnedProducts, product ->
				((ProductModel) product).getCode());
		Assert.assertTrue(CollectionUtils.isEqualCollection(expectedCodes, returnedCodes));
	}
}
