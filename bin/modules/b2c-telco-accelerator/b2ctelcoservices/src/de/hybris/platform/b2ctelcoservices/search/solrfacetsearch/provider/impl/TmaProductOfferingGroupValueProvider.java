/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * The value provider class for ProductOffering , renders the group associated with product Offerings, renders default
 * if the PO is not associated with any group .
 * since 6.7
 */
public class TmaProductOfferingGroupValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;

	private static final String DEFAULT_GROUP = "default";
	private static final String SEPERATOR = "_";

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		validateParameterNotNullStandardMessage("model", model);

		final List<FieldValue> fieldValues = new ArrayList<>();

		if (model instanceof TmaProductOfferingModel)
		{
			final TmaProductOfferingModel productOfferingModel = (TmaProductOfferingModel) model;
			fieldValues.addAll(createFieldValue(productOfferingModel, indexedProperty));
		}

		return fieldValues;
	}

	protected List<FieldValue> createFieldValue(final TmaProductOfferingModel productOfferingModel,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<>();

		final Collection<TmaBundledProductOfferingModel> bundledProductOfferings = productOfferingModel.getParents();

		for (final TmaBundledProductOfferingModel bundledProductOffering : bundledProductOfferings)

		{
			final List<TmaProductOfferingGroupModel> productOfferingGroups = bundledProductOffering.getProductOfferingGroups();

			if (CollectionUtils.isEmpty(productOfferingGroups))
			{
				addFieldValues(fieldValues, indexedProperty, null,
						bundledProductOffering.getCode().concat(SEPERATOR).concat(DEFAULT_GROUP));
			}
			else
			{
				addGroupNames(productOfferingGroups, productOfferingModel, fieldValues, indexedProperty, bundledProductOffering);

			}

		}

		return fieldValues;
	}

	private void addGroupNames(final List<TmaProductOfferingGroupModel> productOfferingGroups,
			final TmaProductOfferingModel productOfferingModel, final List<FieldValue> fieldValues,
			final IndexedProperty indexedProperty, final TmaBundledProductOfferingModel bundledProductOffering)
	{
		boolean flag = false;
		for (final TmaProductOfferingGroupModel productOfferingGroup : productOfferingGroups)
		{
			if (productOfferingGroup.getChildProductOfferings().contains(productOfferingModel))
			{
				flag = true;
				addFieldValues(fieldValues, indexedProperty, null, productOfferingGroup.getCode());
			}
		}

		if (!flag)
		{
			addFieldValues(fieldValues, indexedProperty, null, bundledProductOffering.getCode().concat("_").concat(DEFAULT_GROUP));
		}

	}

	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				language == null ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

}
