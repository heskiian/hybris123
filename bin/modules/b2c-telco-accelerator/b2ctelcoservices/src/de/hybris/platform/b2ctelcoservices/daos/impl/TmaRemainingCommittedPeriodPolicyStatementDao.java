/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRemainingCommittedPeriodPolicyStatementModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;


/**
 * Dao for CompatibilityPolicy conditions based on
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaRemainingCommittedPeriodPolicyStatementModel}.
 *
 * @since 2003
 */
public class TmaRemainingCommittedPeriodPolicyStatementDao extends DefaultGenericDao<TmaPolicyConditionModel>
		implements TmaPolicyConditionDao

{
	public TmaRemainingCommittedPeriodPolicyStatementDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	private static final String FIND_POLICIES_HAVING_CONDITION_WITH_REMAINING_DAYS_STATEMENT = "SELECT {pc:"
			+ TmaPolicyConditionModel.PK + "} " + "FROM {" + TmaPolicyConditionModel._TYPECODE + " AS pc " + "  JOIN "
			+ TmaRemainingCommittedPeriodPolicyStatementModel._TYPECODE + " AS ps " + "  ON {pc:" + TmaPolicyConditionModel.STATEMENT
			+ "}={ps:" + TmaRemainingCommittedPeriodPolicyStatementModel.PK + "}} ";

	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext requestParam)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICIES_HAVING_CONDITION_WITH_REMAINING_DAYS_STATEMENT);

		return getFlexibleSearchService().<TmaPolicyConditionModel> search(query).getResult();

	}
}
