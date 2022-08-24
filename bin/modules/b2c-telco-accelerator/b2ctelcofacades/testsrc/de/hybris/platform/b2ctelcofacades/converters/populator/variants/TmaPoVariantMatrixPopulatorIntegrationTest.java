/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;

import java.util.List;

import org.junit.Test;


/**
 * Integration tests for {@link TmaPoVariantMatrixPopulator}.
 *
 * @since 1810
 */
@IntegrationTest
public class TmaPoVariantMatrixPopulatorIntegrationTest extends TmaPoVariantBaseIntegrationTest
{
	private ProductData productData;
	private TmaProductOfferingModel basePo;
	private TmaPoVariantMatrixPopulator matrixPopulator;
	private List<VariantMatrixElementData> variantMatrix;

	@Override
	protected void setUpTest()
	{
		this.matrixPopulator = new TmaPoVariantMatrixPopulator();
		this.matrixPopulator.setVariantMatrixElementPopulator(variantMatrixElementPopulator);
		this.matrixPopulator.setValueCategoryComparator(variantValueCategoryModelPriorityComparator);
		this.productData = new ProductData();
	}

	@Test
	public void givenProductWithoutVariants_thenVariantMatrixIsNull()
	{
		givenBasePo(IPHONE_X_NO_VARIANTS);
		whenVariantMatrixIsPopulated();
		thenProductIsMultidimensional(false);
		thenVariantMatrixIsNull();
	}

	@Test
	public void givenProductWithVariants_thenVariantMatrixIsPopulated()
	{
		testProductWithVariants(IPHONE_X);
	}

	@Test
	public void givenVariant_thenVariantMatrixIsPopulated()
	{
		testProductWithVariants(IPHONE_X_SILVER_128GB);
	}

	private void testProductWithVariants(final String poCode)
	{
		givenBasePo(poCode);
		whenVariantMatrixIsPopulated();
		thenProductIsMultidimensional(true);
		thenVariantMatrixSizeIs(2, 1, 2);
		assertMatrixElement(getMatrixElement(0), IPHONE_X_SILVER_128GB, SILVER, COLOR, 1, false);
		assertMatrixElement(getMatrixElement(1), IPHONE_X_SPACE_GREY_16GB, SPACE_GREY, COLOR, 2, false);
		assertMatrixElement(getMatrixElement(0, 0), IPHONE_X_SILVER_128GB, GB128, STORAGE_SIZE, 0, true);
		assertMatrixElement(getMatrixElement(1, 0), IPHONE_X_SPACE_GREY_16GB, GB16, STORAGE_SIZE, 0, true);
		assertMatrixElement(getMatrixElement(1, 1), IPHONE_X_SPACE_GREY_32GB, GB32, STORAGE_SIZE, 0, true);
	}

	private void givenBasePo(final String code)
	{
		this.basePo = tmaPoService.getPoForCode(code);
	}

	private void whenVariantMatrixIsPopulated()
	{
		matrixPopulator.populate(basePo, productData);
		this.variantMatrix = productData.getVariantMatrix();
	}

	private void thenVariantMatrixSizeIs(final int mainVariants, final int... subVariants)
	{
		assertNotNull(variantMatrix);
		assertEquals(mainVariants, variantMatrix.size());
		for (int i = 0; i < variantMatrix.size(); i++)
		{
			final VariantMatrixElementData elem = variantMatrix.get(i);
			assertEquals("Variant matrix element size", subVariants[i], elem.getElements().size());
		}
	}

	private void thenVariantMatrixIsNull()
	{
		assertEquals(null, productData.getVariantMatrix());
	}

	private void thenProductIsMultidimensional(final boolean isMultidimensional)
	{
		assertEquals(isMultidimensional, productData.getMultidimensional());
	}

	private VariantMatrixElementData getMatrixElement(int x, int y)
	{
		final VariantMatrixElementData topElement = getMatrixElement(x);
		return topElement.getElements().get(y);
	}

	private VariantMatrixElementData getMatrixElement(final int x)
	{
		return variantMatrix.get(x);
	}
}
