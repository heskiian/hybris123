/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.ProductVariantOption;
import de.hybris.platform.commercefacades.product.converters.populator.VariantMatrixElementPopulator;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.entity.VariantValueCategoryModelPriorityComparator;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;

import javax.annotation.Resource;

import org.junit.Before;


/**
 * Base class for po variants integration tests.
 *
 * @since 1810
 */
public abstract class TmaPoVariantBaseIntegrationTest extends ServicelayerTest
{
	protected static final String IPHONE_X = "iPhone_x";
	protected static final String IPHONE_X_NO_VARIANTS = "iPhone_x_no_variants";
	protected static final String IPHONE_X_SILVER_128GB = "iPhone_x_silver_128g";
	protected static final String IPHONE_X_SPACE_GREY_16GB = "iPhone_x_space_grey_16g";
	protected static final String IPHONE_X_SPACE_GREY_32GB = "iPhone_x_space_grey_32g";
	protected static final String COLOR = "Color";
	protected static final String SILVER = "Silver";
	protected static final String SPACE_GREY = "Space Grey";
	protected static final String STORAGE_SIZE = "Storage Size";
	protected static final String GB16 = "16G";
	protected static final String GB32 = "32G";
	protected static final String GB128 = "128G";

	@Resource
	protected TmaPoService tmaPoService;
	@Resource
	protected VariantMatrixElementPopulator variantMatrixElementPopulator;
	@Resource
	protected VariantValueCategoryModelPriorityComparator variantValueCategoryModelPriorityComparator;
	@Resource
	protected ConfigurablePopulator<TmaPoVariantModel, VariantOptionData, ProductVariantOption> tmaVariantOptionDataConfigurablePopulator;


	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/b2ctelcofacades/test/testTmaPoVariantsData.impex", "utf-8");
		setUpTest();
	}
	protected abstract void setUpTest();

	protected void assertMatrixElement(final VariantMatrixElementData matrixElement, final String variantCode,
			final String variantValueCategoryName, final String parentVariantCategoryName, final int elementsCount,
			final boolean isLeaf)
	{
		assertEquals("Variant code", variantCode, matrixElement.getVariantOption().getCode());
		assertEquals("Variant value category name", variantValueCategoryName, matrixElement.getVariantValueCategory().getName());
		assertEquals("Parent variant category name", parentVariantCategoryName, matrixElement.getParentVariantCategory().getName());
		assertEquals("Variant elements", elementsCount, matrixElement.getElements().size());
		assertEquals("Variant is leaf", isLeaf, matrixElement.getIsLeaf());
	}
}
