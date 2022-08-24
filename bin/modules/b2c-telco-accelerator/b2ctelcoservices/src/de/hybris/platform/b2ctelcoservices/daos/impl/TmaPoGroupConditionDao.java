/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.List;


/**
 * Implementation of the {@link TmaPolicyConditionDao} for searching policy conditions having a
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaPoGroupPolicyStatementModel} statement.
 *
 * @since 6.7
 */
public class TmaPoGroupConditionDao extends DefaultGenericDao<TmaPolicyConditionModel> implements TmaPolicyConditionDao
{
	private static final String FIND_POLICY_CONDITIONS_BY_PO_GROUP_STATEMENT = "SELECT {c:" + TmaPolicyConditionModel.PK
			+ "} FROM {" + TmaPolicyConditionModel._TYPECODE + " AS c JOIN " + TmaPoGroupPolicyStatementModel._TYPECODE + " AS "
			+ "pogs ON " + "{c:" + TmaPolicyActionModel.STATEMENT + "}={pogs:" + TmaPoGroupPolicyStatementModel.PK + "}}"
			+ " WHERE {pogs:" + TmaPoGroupPolicyStatementModel.PRODUCTOFFERINGGROUP + "}=?"
			+ TmaPoGroupPolicyStatementModel.PRODUCTOFFERINGGROUP;

	public TmaPoGroupConditionDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext context)
	{
		if (context.getGroup() == null)
		{
			return Collections.emptyList();
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICY_CONDITIONS_BY_PO_GROUP_STATEMENT);
		query.addQueryParameter(TmaPoGroupPolicyStatementModel.PRODUCTOFFERINGGROUP, context.getGroup());
		return getFlexibleSearchService().<TmaPolicyConditionModel>search(query).getResult();
	}
}
