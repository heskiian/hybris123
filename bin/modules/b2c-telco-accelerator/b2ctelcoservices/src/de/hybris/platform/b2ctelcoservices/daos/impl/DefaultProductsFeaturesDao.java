/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.ProductsFeaturesDao;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link ProductsFeaturesDao}.
 */
public class DefaultProductsFeaturesDao extends AbstractItemDao implements ProductsFeaturesDao
{
	private static final String CLASSIFICATION_CLASS_CODE = "classificationClassCode";
	private static final String CLASSIFICATION_ATTRIBUTE_CODE = "classificationAttributeCode";
	private static final String MANUFACTURER_NAME = "brand";

	private static final String FIND_PRODUCTS_BY_VENDOR_CODE_QUERY = "SELECT {a:" + ProductFeatureModel.PK + "} FROM {"
			+ ProductFeatureModel._TYPECODE + " AS pf JOIN " + ProductModel._TYPECODE + " AS a ON {a:pk} = {pf:"
			+ ProductFeatureModel.PRODUCT + "} " + " JOIN " + ClassAttributeAssignmentModel._TYPECODE
			+ " AS caa ON {caa:pk} = {pf:" + ProductFeatureModel.CLASSIFICATIONATTRIBUTEASSIGNMENT + "}" + "  JOIN "
			+ ClassificationAttributeModel._TYPECODE + " AS ca ON {ca:pk} = {caa: "
			+ ClassAttributeAssignmentModel.CLASSIFICATIONATTRIBUTE + "}" + "  JOIN "
			+ ClassificationClassModel._TYPECODE + " AS cc ON {cc:pk} = {caa:"
			+ ClassAttributeAssignmentModel.CLASSIFICATIONCLASS + "}" + "}" + " WHERE {cc:" + ClassificationClassModel.CODE
			+ "} = ?" + CLASSIFICATION_CLASS_CODE + " AND {ca:" + ClassificationAttributeModel.CODE
			+ "} = ?" + CLASSIFICATION_ATTRIBUTE_CODE + " AND {pf:" + ProductFeatureModel.STRINGVALUE + "} LIKE ?"
			+ MANUFACTURER_NAME + " ";

	@Nonnull
	@Override
	public List<ProductModel> findProductsByVendorCompatibility(final String manufacturerName,
			final String classificationClassCode, final String classificationAttributeCode)
	{
		validateParameterNotNullStandardMessage(CLASSIFICATION_CLASS_CODE, classificationClassCode);
		validateParameterNotNullStandardMessage(CLASSIFICATION_ATTRIBUTE_CODE, classificationAttributeCode);
		validateParameterNotNullStandardMessage(MANUFACTURER_NAME, manufacturerName);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCTS_BY_VENDOR_CODE_QUERY);
		query.addQueryParameter(MANUFACTURER_NAME, manufacturerName);
		query.addQueryParameter(CLASSIFICATION_CLASS_CODE, classificationClassCode);
		query.addQueryParameter(CLASSIFICATION_ATTRIBUTE_CODE, classificationAttributeCode);
		return getFlexibleSearchService().<ProductModel> search(query).getResult();
	}
}
