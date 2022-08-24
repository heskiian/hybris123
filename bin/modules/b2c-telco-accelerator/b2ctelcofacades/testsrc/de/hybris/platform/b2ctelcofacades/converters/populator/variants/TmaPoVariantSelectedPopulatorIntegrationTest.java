/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.ProductVariantOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;


/**
 * Integration tests for {@link TmaPoVariantSelectedPopulator}.
 *
 * @since 1810
 */
@IntegrationTest
public class TmaPoVariantSelectedPopulatorIntegrationTest extends TmaPoVariantBaseIntegrationTest
{
	private static final Collection<ProductVariantOption> VARIANT_OPTIONS = Arrays
			.asList(ProductVariantOption.MEDIA, ProductVariantOption.URL);
	private TmaProductOfferingModel poVariant;
	private ProductData productData;
	private TmaPoVariantSelectedPopulator variantSelectedPopulator;


	@Override
	protected void setUpTest()
	{
		this.variantSelectedPopulator = new TmaPoVariantSelectedPopulator();
		this.variantSelectedPopulator.setProductVariantOptionList(VARIANT_OPTIONS);
		this.variantSelectedPopulator.setVariantOptionDataPopulator(tmaVariantOptionDataConfigurablePopulator);
		this.variantSelectedPopulator.setValueCategoryComparator(variantValueCategoryModelPriorityComparator);
		this.variantSelectedPopulator.setVariantMatrixElementPopulator(variantMatrixElementPopulator);
		this.productData = new ProductData();
	}

	@Test
	public void givenSelectedPoVariant_thenVariantMatrixIsPopulatedWithSelectedVariant()
	{
		givenPoVariant(IPHONE_X_SPACE_GREY_32GB);
		whenSelectedVariantIsPopulated();
		thenVariantMatrixContainsOnlySelectedVariant();
	}

	private void givenPoVariant(final String code)
	{
		this.poVariant = tmaPoService.getPoForCode(code);
	}

	private void whenSelectedVariantIsPopulated()
	{
		this.variantSelectedPopulator.populate(poVariant, productData);
	}

	private void thenVariantMatrixContainsOnlySelectedVariant()
	{
		final List<VariantMatrixElementData> variantMatrix = productData.getVariantMatrix();
		assertEquals(1, variantMatrix.size());
		final VariantMatrixElementData matrixElement = variantMatrix.get(0);
		assertMatrixElement(matrixElement,IPHONE_X_SPACE_GREY_32GB, SPACE_GREY, COLOR,1, false  );
		assertMatrixElement(matrixElement.getElements().get(0),IPHONE_X_SPACE_GREY_32GB, GB32, STORAGE_SIZE,0, true  );
	}
}
