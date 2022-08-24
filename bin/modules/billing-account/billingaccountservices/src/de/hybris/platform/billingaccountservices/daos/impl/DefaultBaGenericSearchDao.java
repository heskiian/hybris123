/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccountservices.daos.impl;

import de.hybris.platform.billingaccountservices.daos.BaGenericSearchDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Data Access Object for retrieving models using generic searches.
 *
 * @since 2105
 */
public class DefaultBaGenericSearchDao<M extends ItemModel> extends DefaultGenericDao<M> implements BaGenericSearchDao<M>
{
	public DefaultBaGenericSearchDao(final String typeCode)
	{
		super(typeCode);
	}

	public M findUnique(final Map<String, ? extends Object> params)
	{
		params.forEach(ServicesUtil::validateParameterNotNullStandardMessage);
		final List<M> results = find(params);
		if (CollectionUtils.isEmpty(results))
		{
			throw new ModelNotFoundException("Could not find " + M._TYPECODE + ".");
		}
		if (results.size() > 1)
		{
			throw new AmbiguousIdentifierException("Expected unique model, but found " + results.size() + " models.");
		}
		return results.iterator().next();
	}
}
