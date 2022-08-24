/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Value provider for solr property hasParentBpos . Flag for SPOs to mark if the spo is part of other BPOs
 *
 * @since 6.7
 */
public class TmaSpoHasParentBposValueProvider extends TmaAbstractPropertyFieldValueProvider
{
	private TmaPoService tmaPoService;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		validateParameterNotNullStandardMessage("model", model);

		final List<FieldValue> fieldValues = new ArrayList<>();

		if (model instanceof TmaSimpleProductOfferingModel)
		{
			final TmaSimpleProductOfferingModel spo = (TmaSimpleProductOfferingModel) model;
			final Collection<TmaBundledProductOfferingModel> bpos = getTmaPoService().getAllParents(spo);

			final boolean spoHasParentBpos = !bpos.isEmpty();

			addFieldValues(fieldValues, indexedProperty, null, spoHasParentBpos);
		}

		return fieldValues;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}
}
