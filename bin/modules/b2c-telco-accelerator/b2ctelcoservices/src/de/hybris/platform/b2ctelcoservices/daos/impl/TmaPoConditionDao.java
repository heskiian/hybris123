/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;


/**
 * Implementation of the {@link TmaPolicyConditionDao} for searching policy conditions having a
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel} statement.
 *
 * @since 6.7
 */
public class TmaPoConditionDao extends DefaultGenericDao<TmaPolicyConditionModel> implements TmaPolicyConditionDao
{
	private static final String FIND_POLICY_CONDITIONS_BY_PO_STATEMENT = "SELECT {sc:" + TmaPolicyConditionModel.PK + "} "
			+ "FROM {" + TmaPolicyConditionModel._TYPECODE + " AS sc " + "JOIN " + TmaPoPolicyStatementModel._TYPECODE + " AS pos "
			+ "ON {sc:" + TmaPolicyConditionModel.STATEMENT + "}={pos:" + TmaPoPolicyStatementModel.PK + "}}" + " WHERE {pos:"
			+ TmaPoPolicyStatementModel.PRODUCTOFFERING + "} IN (?" + TmaPoPolicyStatementModel.PRODUCTOFFERING + ")";

	public TmaPoConditionDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext context)
	{
		validateParameterNotNullStandardMessage(TmaPoPolicyStatementModel.PRODUCTOFFERING, context.getProductOfferings());
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICY_CONDITIONS_BY_PO_STATEMENT);
		query.addQueryParameter(TmaPoPolicyStatementModel.PRODUCTOFFERING, context.getProductOfferings());
		return getFlexibleSearchService().<TmaPolicyConditionModel> search(query).getResult();
	}
}
