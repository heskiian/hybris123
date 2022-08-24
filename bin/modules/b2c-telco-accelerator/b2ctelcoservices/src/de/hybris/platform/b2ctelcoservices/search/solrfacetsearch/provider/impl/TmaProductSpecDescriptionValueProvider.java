/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;



/**
 * Field Value provider for indexing {@link TmaProductSpecCharacteristicModel#getDescription()}
 *
 * @since 6.7
 */
public class TmaProductSpecDescriptionValueProvider extends TmaAbstractLocalizedPropertyFieldValueProvider
{
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
	{
		final List<FieldValue> fields = new ArrayList<>();

		if (model instanceof TmaProductOfferingModel)
		{
			final TmaProductOfferingModel productOffering = (TmaProductOfferingModel) model;
			final List<TmaProductSpecCharValueUseModel> staticPscvus = productOffering
					.getProductSpecCharValueUses().stream()
					.filter((TmaProductSpecCharValueUseModel pscvu) ->
							CollectionUtils.isNotEmpty(pscvu.getProductSpecCharacteristicValues()) && pscvu.getMinCardinality()
									.equals(pscvu.getMaxCardinality()) && pscvu.getMinCardinality()
									.equals(pscvu.getProductSpecCharacteristicValues().size())).collect(Collectors.toList());

			final Set<TmaProductSpecCharacteristicValueModel> productSpecCharacteristicValues = new HashSet<>();

			staticPscvus.forEach((TmaProductSpecCharValueUseModel pscvu) -> productSpecCharacteristicValues
					.addAll(pscvu.getProductSpecCharacteristicValues()));

			productSpecCharacteristicValues.forEach(
					productSpecificationValue -> addFieldValueForLanguages(fields, indexedProperty, productSpecificationValue));
		}
		return fields;
	}

	@Override
	protected String getLocalizedString(final Object model, final Locale locale)
	{
		return ((TmaProductSpecCharacteristicValueModel) model).getDescription(locale);
	}
}
