/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductSpecificationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration Test of {@link DefaultTmaProductSpecificationService}.
 *
 * @since 2102
 */
@IntegrationTest
public class DefaultTmaProductSpecificationServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String UNLIMITED_5G_PLAN_CODE = "unlimited5GPlans";
	private static final String INTERNET_OFFERING_CODE = "internet";
	private static final String NOT_EXISTING_PS_CODE = "salsaS";
	private static final String INTERNET_COMPOSITE_PS_CODE = "internet_compositeProductSpec";

	@Resource
	private TmaProductSpecificationService defaultTmaProductSpecificationService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/impex/test_productSpecifications.impex", "utf-8");
	}

	@Test
	public void testGetAtomicProductSpecificationForCode()
	{
		final TmaProductSpecificationModel tmaProductSpecificationModel =
				defaultTmaProductSpecificationService.getProductSpecification(UNLIMITED_5G_PLAN_CODE);

		Assert.assertEquals(UNLIMITED_5G_PLAN_CODE, tmaProductSpecificationModel.getId());
	}

	@Test(expected = ModelNotFoundException.class)
	public void testNotExistingAtomicProductSpecificationForCode()
	{
		defaultTmaProductSpecificationService.getProductSpecification(NOT_EXISTING_PS_CODE);
	}

	@Test
	public void testGetComposedProductSpecificationForCode()
	{
		final Set<TmaProductSpecificationModel> children = new HashSet<>();
		children.add(defaultTmaProductSpecificationService.getProductSpecification(INTERNET_OFFERING_CODE));
		final TmaCompositeProductSpecificationModel tmaProductSpecificationModel =
				(TmaCompositeProductSpecificationModel) defaultTmaProductSpecificationService
						.getProductSpecification(INTERNET_COMPOSITE_PS_CODE);

		Assert.assertEquals(INTERNET_COMPOSITE_PS_CODE, tmaProductSpecificationModel.getId());
		Assert.assertTrue(tmaProductSpecificationModel.getChildren().containsAll(children));
	}
}
