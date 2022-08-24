/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaIdentificationDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaIdentificationDao}.
 *
 * @since 1911
 */
public class DefaultTmaIdentificationDao extends DefaultGenericDao<TmaIdentificationModel> implements TmaIdentificationDao
{
	private static final String FIND_IDENTIFICATION_BY_TYPE_AND_NUMBER = "SELECT {id:" + TmaIdentificationModel.PK
			+ "} FROM {" + TmaIdentificationModel._TYPECODE + " AS id JOIN " + TmaIdentificationType._TYPECODE + " AS it ON {id:"
			+ TmaIdentificationModel.IDENTIFICATIONTYPE + "}={it:" + EnumerationValueModel.PK + "}}" + " WHERE {id:"
			+ TmaIdentificationModel.IDENTIFICATIONNUMBER + "}=?identificationNumber" + " AND {it:"
			+ EnumerationValueModel.CODE + "}=?identificationType";

	public DefaultTmaIdentificationDao()
	{
		super(TmaIdentificationModel._TYPECODE);
	}

	@Override
	public TmaIdentificationModel getTmaIdentificationModelForTypeAndNumber(final String identificationType,
			final String identificationNumber)
	{
		ServicesUtil.validateParameterNotNullStandardMessage(TmaIdentificationModel.IDENTIFICATIONTYPE, identificationType);
		ServicesUtil.validateParameterNotNullStandardMessage(TmaIdentificationModel.IDENTIFICATIONNUMBER, identificationNumber);
		final Map<String, String> parameters = new HashMap<>(2);
		parameters.put(TmaIdentificationModel.IDENTIFICATIONTYPE, identificationType);
		parameters.put(TmaIdentificationModel.IDENTIFICATIONNUMBER, identificationNumber);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_IDENTIFICATION_BY_TYPE_AND_NUMBER);
		query.addQueryParameters(parameters);
		final List<TmaIdentificationModel> identificationModels = getFlexibleSearchService().<TmaIdentificationModel> search(query)
				.getResult();
		if (identificationModels.isEmpty())
		{
			return null;
		}
		if (CollectionUtils.size(identificationModels) > 1)
		{
			throw new AmbiguousIdentifierException("Found " + CollectionUtils.size(identificationModels) + " models for " +
					TmaIdentificationModel.IDENTIFICATIONTYPE + " '" + identificationType + "' and "
					+ TmaIdentificationModel.IDENTIFICATIONNUMBER +
					" '" + identificationNumber + "'.");
		}
		return identificationModels.get(0);
	}
}
