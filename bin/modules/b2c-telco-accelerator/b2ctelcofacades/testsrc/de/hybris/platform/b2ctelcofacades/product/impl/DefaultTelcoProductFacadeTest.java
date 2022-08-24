/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.services.CompatibilityService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.commerceservices.product.CommerceProductReferenceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;


@UnitTest
public class DefaultTelcoProductFacadeTest
{
	private static final Logger LOG = Logger.getLogger(DefaultTelcoProductFacadeTest.class);

	private static final String TEST_PRODUCT_CODE = "democode";

	private final DefaultTelcoProductFacade telcoProductFacade = new DefaultTelcoProductFacade();
	private final DefaultProductFacade productFacade = new DefaultProductFacade();

	@Mock
	private CompatibilityService compatibilityService;

	@Mock
	private ProductService productService;

	@Mock
	private ModelService modelService;

	@Mock
	private Converter<ProductModel, ProductData> productConverter;

	@Mock
	private CommerceProductReferenceService commerceProductReferenceService;

	private ProductModel demoProduct;

	@Before
	public void mockSetup()
	{
		MockitoAnnotations.initMocks(this);
		telcoProductFacade.setCompatibilityService(compatibilityService);
		productFacade.setProductService(productService);
		productFacade.setModelService(modelService);
		productFacade.setProductConverter(productConverter);
		productFacade.setCommerceProductReferenceService(commerceProductReferenceService);
		telcoProductFacade.setProductFacade(productFacade);
		demoProduct = new ProductModel();
		demoProduct.setCode(TEST_PRODUCT_CODE);

		demoProduct.setProductReferences(Collections.<ProductReferenceModel>emptyList());
	}

	@Test
	public void testRefsAndFeatureCCompatibleLimitsSizeToMaxAmount()
	{
		final Integer limit = Integer.valueOf(5);
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		final List<ProductModel> mockProducts = createRandomProducts(9);
		when(productService.getProductForCode(TEST_PRODUCT_CODE)).thenReturn(demoProduct);
		when(productConverter.convert((ProductModel) Matchers.any())).thenReturn(createProductData(TEST_PRODUCT_CODE));
		when(modelService.getAttributeValue(demoProduct, ProductModel.PRODUCTREFERENCES)).thenReturn(
				Collections.<ProductReferenceModel>emptyList());
		when(compatibilityService
				.getFeatureCompatibleProducts(TEST_PRODUCT_CODE, classAttributeAssignmentModel))
				.thenReturn(mockProducts);
		final List<ProductOption> options = null;

		final List<ProductData> productData = telcoProductFacade.getProductReferencesAndFeatureCompatibleProductsForCode(
				TEST_PRODUCT_CODE, Collections.singletonList(ProductReferenceTypeEnum.ACCESSORIES), options, limit,
				classAttributeAssignmentModel);

		LOG.info(productData.size() + " is size of returned data");
		Assert.assertTrue(productData.size() <= limit.intValue());
	}

	@Test
	public void testGetProductReferencesAndFeatureCompatibleAndVendorCompatibleProductsForCode()
	{
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		final List<ProductModel> mockProducts = createRandomAccessories(9);
		when(productService.getProductForCode(TEST_PRODUCT_CODE)).thenReturn(demoProduct);
		when(productConverter.convert(Matchers.any())).thenReturn(createProductData("docde"));
		when(modelService.getAttributeValue(demoProduct, ProductModel.PRODUCTREFERENCES)).thenReturn(Collections.emptyList());
		when(compatibilityService
				.getFeatureCompatibleProducts(TEST_PRODUCT_CODE, classAttributeAssignmentModel))
				.thenReturn(Collections.emptyList());
		when(compatibilityService.getProductsForVendorCompatibility(TEST_PRODUCT_CODE)).thenReturn(mockProducts);
		final List<ProductOption> options = null;

		final List<ProductData> productData = telcoProductFacade
				.getProductReferencesAndCompatibleProducts(TEST_PRODUCT_CODE,
						Collections.singletonList(ProductReferenceTypeEnum.ACCESSORIES), options, 5,
						classAttributeAssignmentModel);

		LOG.info(productData.size() + " is size of returned data");
		Assert.assertTrue(productData.size() <= 5);
	}

	@Test
	public void testRefsFeatureCompatibleAndVendorCompatibleSizeToMaxAmount()
	{
		final Integer limit = 5;
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		final List<ProductModel> mockProducts = createRandomAccessories(9);
		when(productService.getProductForCode(TEST_PRODUCT_CODE)).thenReturn(demoProduct);
		when(productConverter.convert(Matchers.any())).thenReturn(createProductData("docde"));
		when(modelService.getAttributeValue(demoProduct, ProductModel.PRODUCTREFERENCES))
				.thenReturn(Collections.emptyList());
		when(compatibilityService
				.getFeatureCompatibleProducts(TEST_PRODUCT_CODE, classAttributeAssignmentModel))
				.thenReturn(Collections.emptyList());
		when(compatibilityService.getProductsForVendorCompatibility(TEST_PRODUCT_CODE)).thenReturn(mockProducts);
		final List<ProductOption> options = null;

		final List<ProductData> productData = telcoProductFacade
				.getProductReferencesAndCompatibleProducts(TEST_PRODUCT_CODE,
						Collections.singletonList(ProductReferenceTypeEnum.ACCESSORIES), options, limit,
						classAttributeAssignmentModel);

		LOG.info(productData.size() + " is size of returned data");
		Assert.assertTrue(productData.size() <= limit);
	}

	@Test
	public void testFeatureCompatibleProductWithNullLimit()
	{
		final ClassAttributeAssignmentModel classAttributeAssignmentModel = new ClassAttributeAssignmentModel();
		final List<ProductModel> mockProducts = createRandomAccessories(1);
		when(productService.getProductForCode(TEST_PRODUCT_CODE)).thenReturn(demoProduct);
		when(productConverter.convert(Matchers.any())).thenReturn(createProductData("docde"));
		when(modelService.getAttributeValue(demoProduct, ProductModel.PRODUCTREFERENCES)).thenReturn(
				Collections.emptyList());
		when(compatibilityService
				.getFeatureCompatibleProducts(TEST_PRODUCT_CODE, classAttributeAssignmentModel))
				.thenReturn(Collections.emptyList());
		when(compatibilityService.getProductsForVendorCompatibility(TEST_PRODUCT_CODE)).thenReturn(
				mockProducts);

		final List<ProductData> productData = telcoProductFacade
				.getProductReferencesAndCompatibleProducts(TEST_PRODUCT_CODE,
						Collections.singletonList(ProductReferenceTypeEnum.ACCESSORIES), null, null,
						classAttributeAssignmentModel);
		Assert.assertEquals(1, productData.size());
	}


	private List<ProductModel> createRandomProducts(final int count)
	{
		final List<ProductModel> products = new ArrayList<ProductModel>();
		for (int k = 0; k < count; k++)
		{
			products.add(new ProductModel());
		}
		return products;
	}

	private List<ProductModel> createRandomAccessories(final int count)
	{
		final List<ProductModel> products = new ArrayList<ProductModel>();
		for (int k = 0; k < count; k++)
		{
			products.add(new ProductModel());
		}
		return products;
	}

	private ProductData createProductData(final String code)
	{
		final ProductData productData = new ProductData();
		productData.setCode(code);
		return productData;
	}

	@Test
	public void testDuplicateProductsDontGetAddedTwice()
	{
		final List<ProductData> products = new ArrayList<ProductData>();
		final ProductData product1 = new ProductData();
		product1.setCode("jokd");
		final ProductData product2 = new ProductData();
		product2.setCode("kodfkk");
		final ProductData newProduct = new ProductData();
		newProduct.setCode("jokd");
		final ProductData brandnewProduct = new ProductData();
		brandnewProduct.setCode("mitthi");
		products.add(product1);
		products.add(product2);
		Assert.assertTrue(telcoProductFacade.contains(products, newProduct));

		Assert.assertFalse(telcoProductFacade.contains(products, brandnewProduct));
	}
}
