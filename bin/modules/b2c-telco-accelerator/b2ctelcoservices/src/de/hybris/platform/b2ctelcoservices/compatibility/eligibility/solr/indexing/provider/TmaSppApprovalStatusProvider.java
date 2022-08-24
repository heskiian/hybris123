/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing.provider;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Value provider that handles indexing of approvalStatus property from data model to Solr.
 *
 * @since 1903
 */
public class TmaSppApprovalStatusProvider extends TmaAbstractSppProvider
{
	/**
	 * Creates and returns a list of fieldValues that contains the approvalStatus code property of the model.
	 * @param indexedProperty The indexed property
	 * @param model The object whose value is indexed
	 * @return A list of fieldValues which contain the model's approvalStatus code.
	 */
	@Override
	protected Collection<FieldValue> getValues(IndexedProperty indexedProperty, Object model)
	{
		validateParameterNotNullStandardMessage("model", model);

		final List<FieldValue> fieldValues = new ArrayList<>();

		if (!(model instanceof TmaProductOfferingModel))
		{
			return fieldValues;
		}

		String approvalStatus = "";
		TmaProductOfferingModel productOffering = (TmaProductOfferingModel) model;

		if (productOffering.getApprovalStatus() != null)
		{
			approvalStatus = productOffering.getApprovalStatus().getCode();
		}

		addFieldValues(fieldValues, indexedProperty, null, approvalStatus);
		return fieldValues;
	}
}
