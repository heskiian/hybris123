/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Field Value provider for indexing {@link TmaProductSpecCharacteristicModel}
 *
 * @since 1805
 */
public class TmaPscValueProvider extends TmaAbstractLocalizedPropertyFieldValueProvider
{
	private String pscId;

	/**
	 * @deprecated since 1805.
	 */
	@Deprecated(since = "1805", forRemoval= true)
	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model)
	{
		validateParameterNotNull(model, "PscValueProviderModel must not be null.");
		final List<FieldValue> fields = new ArrayList<>();

		if (model instanceof TmaProductOfferingModel)
		{
			final TmaProductOfferingModel productOffering = (TmaProductOfferingModel) model;
			final Set<TmaProductSpecCharacteristicValueModel> productSpecCharacteristicValues = productOffering
					.getProductSpecCharacteristicValues();

			productSpecCharacteristicValues.stream()
					.filter(productSpecCharacteristicModel -> productSpecCharacteristicModel.getProductSpecCharacteristic().getId()
							.equals(getPscId()))
					.forEach(
							productSpecificationValue -> addFieldValueForLanguages(fields, indexedProperty, productSpecificationValue));
		}
		return fields;
	}

	@Override
	protected String getLocalizedString(final Object productSpecificationValue, final Locale locale)
	{
		final TmaProductSpecCharacteristicValueModel model = (TmaProductSpecCharacteristicValueModel) productSpecificationValue;
		return setDisplay(model.getValue(), model.getUnitOfMeasure().getName(locale));
	}

	private String setDisplay(final String value, final String unitOfMeasureName)
	{
		return value + " " + unitOfMeasureName;
	}

	public String getPscId()
	{
		return pscId;
	}

	@Required
	public void setPscId(final String pscId)
	{
		this.pscId = pscId;
	}
}
