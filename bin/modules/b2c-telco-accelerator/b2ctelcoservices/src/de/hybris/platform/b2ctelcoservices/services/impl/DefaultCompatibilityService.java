/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.ProductsFeaturesDao;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.CompatibilityService;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValueCondition;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.genericsearch.GenericSearchService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of Compatibility Service {@link CompatibilityService}.
 */
public class DefaultCompatibilityService implements CompatibilityService
{
	private ClassificationService classificationService;
	private ProductService productService;
	private GenericSearchService genericSearchService;
	private String classificationAttributeCode;
	private String classificationClassCode;
	private ModelService modelService;
	private ProductsFeaturesDao productsFeaturesDao;

	@Nonnull
	@Override
	public List<ProductModel> getFeatureCompatibleProducts(final String code,
			final ClassAttributeAssignmentModel classificationAttributeAssignment)
	{
		final ProductModel product = getProductService().getProductForCode(code);
		final Set<ProductModel> relatedProducts = new HashSet<>();

		final Feature modelFeature = getClassificationService().getFeature(product, classificationAttributeAssignment);

		if (modelFeature != null)
		{
			relatedProducts.addAll(getProductsWithFeature(modelFeature));
		}

		return relatedProducts.stream().filter(relatedProduct -> !StringUtils.equals(relatedProduct.getCode(), product.getCode()))
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public List<ProductModel> getProductsForVendorCompatibility(final String productCode)
	{
		final ProductModel product = getProductService().getProductForCode(productCode);
		if (StringUtils.isBlank(product.getManufacturerName()))
		{
			return Collections.emptyList();
		}

		return getProductsFeaturesDao().findProductsByVendorCompatibility(product.getManufacturerName(),
				getClassificationClassCode(), getClassificationAttributeCode());
	}

	/**
	 * Search list of products with a particular classification feature value.
	 *
	 * @return list of matching products
	 */
	protected List<ProductModel> getProductsWithFeature(final Feature modelFeature)
	{
		final List<FeatureValue> featureValues = modelFeature.getValues();
		final List<ProductModel> featureCompatibleProducts = new ArrayList<>();

		for (final FeatureValue featureValue : featureValues)
		{
			if (featureValue != null)
			{
				featureCompatibleProducts.addAll(getCompatibleProductsForFeature(featureValue, modelFeature));
			}
		}
		return featureCompatibleProducts;
	}

	/**
	 * Use generic search service to list of products based on feature compatibilty values.
	 *
	 * @return collection of matching products
	 */
	protected Collection<ProductModel> getCompatibleProductsForFeature(final FeatureValue featureValue,
			final Feature modelFeature)
	{
		ClassificationAttributeValue classAttributeValue;
		if (featureValue.getValue() instanceof ClassificationAttributeValueModel)
		{
			classAttributeValue = getModelService().getSource(featureValue.getValue());
		}
		else
		{
			classAttributeValue = (ClassificationAttributeValue) featureValue.getValue();
		}
		final ClassAttributeAssignment classAttributeAssignment = getModelService().getSource(
				modelFeature.getClassAttributeAssignment());

		final SearchResult<ProductModel> prodsResult = getGenericSearchService().search(
				generateQuery(classAttributeAssignment, classAttributeValue));

		return prodsResult.getResult();
	}

	/**
	 * GenericQuery and jalo classes. This should be replaced by servicelayer code as soon as wiki page us updated.
	 * For code samples check the Classification and Feature Value API on wiki.
	 */
	protected GenericQuery generateQuery(final ClassAttributeAssignment classAttributeAssignment,
			final ClassificationAttributeValue classAttributeValue)
	{
		return new GenericQuery(TmaProductOfferingModel._TYPECODE,
				FeatureValueCondition.equals(classAttributeAssignment, classAttributeValue));
	}

	protected ProductsFeaturesDao getProductsFeaturesDao()
	{
		return productsFeaturesDao;
	}

	@Required
	public void setProductsFeaturesDao(final ProductsFeaturesDao productsFeaturesDao)
	{
		this.productsFeaturesDao = productsFeaturesDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected GenericSearchService getGenericSearchService()
	{
		return genericSearchService;
	}

	@Required
	public void setGenericSearchService(final GenericSearchService genericSearchService)
	{
		this.genericSearchService = genericSearchService;
	}

	protected ClassificationService getClassificationService()
	{
		return classificationService;
	}

	@Required
	public void setClassificationService(final ClassificationService classificationService)
	{
		this.classificationService = classificationService;
	}

	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected String getClassificationAttributeCode()
	{
		return classificationAttributeCode;
	}

	@Required
	public void setClassificationAttributeCode(final String classificationAttributeCode)
	{
		this.classificationAttributeCode = classificationAttributeCode;
	}

	protected String getClassificationClassCode()
	{
		return classificationClassCode;
	}

	@Required
	public void setClassificationClassCode(final String classificationClassCode)
	{
		this.classificationClassCode = classificationClassCode;
	}
}
