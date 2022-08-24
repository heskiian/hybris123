/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProcessTypesPolicyStatementModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.List;


/**
 * Dao for CompatibilityPolicy conditions based on {@link de.hybris.platform.b2ctelcoservices.enums.TmaProcessType}.
 *
 * @since 1907
 */
public class TmaProcessTypeConditionDao extends DefaultGenericDao<TmaPolicyConditionModel> implements TmaPolicyConditionDao
{
	private static final String FIND_POLICIES_HAVING_CONDITION_WITH_PROCESS_TYPE_STATEMENT = "SELECT {pc:" + TmaPolicyConditionModel.PK + "} " +
			"FROM {" + TmaPolicyConditionModel._TYPECODE + " AS pc " +
			"  JOIN " + TmaProcessTypesPolicyStatementModel._TYPECODE + " AS ps " +
			"  ON {pc:" + TmaPolicyConditionModel.STATEMENT + "}={ps:" + TmaProcessTypesPolicyStatementModel.PK + "}} ";

	public TmaProcessTypeConditionDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext requestParam)
	{
		final TmaProcessType processType = requestParam.getProcessType();

		if (processType != null)
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICIES_HAVING_CONDITION_WITH_PROCESS_TYPE_STATEMENT);

			return getFlexibleSearchService().<TmaPolicyConditionModel>search(query).getResult();
		}
		return Collections.emptyList();
	}
}
