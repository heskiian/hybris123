/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.provider;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Value provider that handles indexing of parent property from data model to Solr.
 *
 * @since 1903
 */
public class TmaSppParentBundlePoProvider extends TmaAbstractSppProvider
{

	/**
	 * Creates and returns a list of fieldValues that contains the parentBundleProductOfferings of the model.
	 * @param indexedProperty The indexed property
	 * @param model The object whose value is indexed
	 * @return A list of fieldValues which contain the model's parentBundledProductOffering values.
	 */
	@Override
	protected Collection<FieldValue> getValues(IndexedProperty indexedProperty, Object model)
	{
		validateParameterNotNullStandardMessage("model", model);

		final List<FieldValue> fieldValues = new ArrayList<>();

		if (model instanceof TmaProductOfferingModel)
		{
			TmaProductOfferingModel productOffering = (TmaProductOfferingModel) model;
			Collection<TmaBundledProductOfferingModel> parents = productOffering.getParents();
			for (TmaBundledProductOfferingModel bpo : parents)
			{
				addFieldValues(fieldValues, indexedProperty, null, bpo.getCode());
			}

		}

		return fieldValues;
	}
}
