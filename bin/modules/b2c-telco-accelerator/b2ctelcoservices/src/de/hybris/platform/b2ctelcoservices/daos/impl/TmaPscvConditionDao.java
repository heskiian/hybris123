/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.model.TmaPoPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPscvPolicyStatementModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Dao for CompatibilityPolicy conditions based on Pscv.
 *
 * @since 1805
 */
public class TmaPscvConditionDao extends DefaultGenericDao<TmaPolicyConditionModel> implements TmaPolicyConditionDao
{
	public TmaPscvConditionDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	/**
	 * Retrieves all the the PolicyConditions with statements related to an Psc from the Po sent as request parameter.
	 *
	 * @param context
	 *           input parameters used for searching conditions - contains the po
	 * @return the list of policies
	 */
	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext context)
	{
		validateParameterNotNullStandardMessage(TmaPoPolicyStatementModel.PRODUCTOFFERING, context.getProductOfferings());

		final Set<TmaProductSpecCharacteristicValueModel> pscvs = new HashSet<>();

		context.getProductOfferings().forEach(productOffering -> {
			pscvs.addAll(productOffering.getProductSpecCharValueUses().stream()
					.flatMap(pscvu -> pscvu.getProductSpecCharacteristicValues().stream()).collect(Collectors.toSet()));
		});

		final Set<TmaProductSpecCharacteristicModel> pscs = new HashSet<>();
		for (final TmaProductSpecCharacteristicValueModel p : pscvs)
		{
			pscs.add(p.getProductSpecCharacteristic());
		}

		if (pscs.isEmpty())
		{
			return Collections.unmodifiableList(new ArrayList<>());
		}
		final String QUERY = "SELECT {sc:" + TmaPolicyConditionModel.PK + "} " + "FROM {" + TmaPolicyConditionModel._TYPECODE
				+ " AS sc " + "  JOIN " + TmaPscvPolicyStatementModel._TYPECODE + " AS pos " + "  ON {sc:"
				+ TmaPolicyConditionModel.STATEMENT + "}={pos:" + TmaPscvPolicyStatementModel.PK + "}} " + "WHERE {pos:"
				+ TmaPscvPolicyStatementModel.PRODUCTSPECCHARACTERISTIC + "} " + "IN (?psc)";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(QUERY);
		query.addQueryParameter("psc", pscs);

		return getFlexibleSearchService().<TmaPolicyConditionModel> search(query).getResult();
	}
}
