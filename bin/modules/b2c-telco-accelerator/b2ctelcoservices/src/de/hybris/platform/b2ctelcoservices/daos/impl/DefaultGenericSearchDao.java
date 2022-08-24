/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.GenericSearchDao;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Data Access Object for retrieving models using generic searches.
 *
 * @since 6.6
 */
public class DefaultGenericSearchDao<M extends ItemModel> extends DefaultGenericDao<M> implements GenericSearchDao<M>
{
	public DefaultGenericSearchDao(final String typeCode)
	{
		super(typeCode);
	}


	public M findUnique(final Map<String, ? extends Object> params)
	{
		params.forEach((key, value) -> validateParameterNotNullStandardMessage(key, value));
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
