/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyDao;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Default implementation of the {@link TmaPolicyDao}.
 *
 * @since 1810
 */
public class DefaultTmaPolicyDao extends DefaultGenericDao<TmaCompatibilityPolicyModel> implements TmaPolicyDao
{
	private static final String FIND_AVAILABLE_POLICIES_BY_ACTION_TYPE = "SELECT DISTINCT {pm:" + TmaCompatibilityPolicyModel.PK
			+ "} FROM {" + TmaCompatibilityPolicyModel._TYPECODE + " AS pm JOIN "
			+ TmaPolicyActionModel._TMACOMPATIBILITYPOLICY2TMAPOLICYACTIONRELATION + " AS rel ON {pm:" + TmaPolicyConditionModel.PK
			+ "}={rel:source} JOIN " + TmaPolicyActionModel._TYPECODE + " AS pa " + " ON {rel:target}={pa:" + TmaPolicyActionModel.PK
			+ "} JOIN " + TmaCompatibilityPolicyActionType._TYPECODE + " AS at ON {pa:" + TmaPolicyActionModel.ACTIONTYPE + "}={at:"
			+ EnumerationValueModel.PK + "}}" + " WHERE {at:" + EnumerationValueModel.CODE + "}=?" + EnumerationValueModel.CODE;

	private static final String FIND_POLICIES_WITHOUT_CONDITIONS_AND_BY_ACTION_TYPE = "SELECT DISTINCT {cp:"
			+ TmaCompatibilityPolicyModel.PK + "} FROM {" + TmaCompatibilityPolicyModel._TYPECODE + " AS cp LEFT JOIN "
			+ TmaPolicyConditionModel._TMACOMPATIBILITYPOLICY2TMAPOLICYCONDITIONRELATION + " AS condRel on {cp:"
			+ TmaCompatibilityPolicyModel.PK + "}={condRel:source} " + "LEFT JOIN " + TmaPolicyConditionModel._TYPECODE
			+ " AS pc on {pc:" + TmaCompatibilityPolicyModel.PK + "}={condRel:target} JOIN "
			+ TmaPolicyActionModel._TMACOMPATIBILITYPOLICY2TMAPOLICYACTIONRELATION + " AS rel " + "ON {cp:" + TmaPolicyActionModel.PK
			+ "}={rel:source}" + " JOIN " + TmaPolicyActionModel._TYPECODE + " AS pa ON {rel:target}={pa:" + TmaPolicyActionModel.PK
			+ "}" + " JOIN " + TmaCompatibilityPolicyActionType._TYPECODE + " AS at on {pa:" + TmaPolicyActionModel.ACTIONTYPE
			+ "} = {at:" + EnumerationValueModel.PK + "}} WHERE {pc:" + TmaPolicyConditionModel.PK + "} is null and " + "{at:"
			+ EnumerationValueModel.CODE + "} IN (?actionTypes)";

	private static final String FIND_POLICIES_WITH_CATALOG_VERSION = " and " + "{cp:" + TmaCompatibilityPolicyModel.CATALOGVERSION
			+ "} IN (?policyCatalogVersion)";


	private List<TmaPolicyConditionDao> policyConditionDaos;

	public DefaultTmaPolicyDao()
	{
		super(TmaCompatibilityPolicyModel._TYPECODE);
	}

	@Override
	public List<TmaCompatibilityPolicyModel> findPoliciesByActionType(final TmaCompatibilityPolicyActionType actionType)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_AVAILABLE_POLICIES_BY_ACTION_TYPE);
		query.addQueryParameter(EnumerationValueModel.CODE, actionType.getCode());

		return getFlexibleSearchService().<TmaCompatibilityPolicyModel> search(query).getResult();
	}

	@Override
	public Function<TmaPolicyConditionModel, Set<TmaCompatibilityPolicyModel>> gatherAllPolicies()
	{
		return policyCondition -> {
			final Set<TmaCompatibilityPolicyModel> result = new HashSet<>();
			addPolicies(policyCondition, result);
			return result;
		};
	}

	@Override
	public List<TmaCompatibilityPolicyModel> findPoliciesWithoutCondition(final Set<TmaCompatibilityPolicyActionType> actionTypes)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICIES_WITHOUT_CONDITIONS_AND_BY_ACTION_TYPE);
		query.addQueryParameter("actionTypes",
				actionTypes.stream().map(TmaCompatibilityPolicyActionType::getCode).collect(Collectors.toList()));
		return getFlexibleSearchService().<TmaCompatibilityPolicyModel> search(query).getResult();
	}

	@Override
	public List<TmaCompatibilityPolicyModel> findPoliciesWithoutCondition(final Set<TmaCompatibilityPolicyActionType> actionTypes,
			final TmaPolicyContext context)
	{
		final StringBuilder policyWithoutCondition = new StringBuilder(FIND_POLICIES_WITHOUT_CONDITIONS_AND_BY_ACTION_TYPE);
		FlexibleSearchQuery query = new FlexibleSearchQuery(policyWithoutCondition);

		if (context.getPolicyCatalogVersion() != null)
		{
			query = new FlexibleSearchQuery(policyWithoutCondition.append(FIND_POLICIES_WITH_CATALOG_VERSION));
			query.addQueryParameter("policyCatalogVersion", context.getPolicyCatalogVersion());
		}
		query.addQueryParameter("actionTypes",
				actionTypes.stream().map(TmaCompatibilityPolicyActionType::getCode).collect(Collectors.toList()));
		return getFlexibleSearchService().<TmaCompatibilityPolicyModel> search(query).getResult();
	}

	@Override
	public List<TmaCompatibilityPolicyModel> findCompatibilityPoliciesByConditions(final TmaPolicyContext context,
			final Set<TmaCompatibilityPolicyActionType> actionTypes)
	{
		final List<TmaCompatibilityPolicyModel> policies = new ArrayList<>();
		getPolicyConditionDaos().forEach(policyConditionDao -> {
			final Function<TmaPolicyConditionModel, Set<TmaCompatibilityPolicyModel>> mapper = gatherAllPolicies();
			policyConditionDao.findPolicyConditions(context).stream()
					.map(conditionModel -> mapper.apply(conditionModel).stream()
							.filter(policyModel -> isPolicyOfType(policyModel, actionTypes) && isPolicyAvailable(policyModel)
									&& isPolicyOfCatalogVersion(policyModel, context))
							.collect(Collectors.toSet()))
					.forEach(policies::addAll);
		});

		return policies;
	}

	/**
	 * Verifies if the policy is matching with context catalog version.
	 *
	 * @param policyModel
	 *           policy to be evaluated
	 * @param context
	 *           the context for which catalog version are matched
	 * @return true if the policy is matching catalog version, false otherwise
	 */
	protected boolean isPolicyOfCatalogVersion(final TmaCompatibilityPolicyModel policyModel, final TmaPolicyContext context)
	{
		if (context == null || context.getPolicyCatalogVersion() == null)
		{
			return true;
		}
		return policyModel.getCatalogVersion().equals(context.getPolicyCatalogVersion());
	}

	/**
	 * Verifies if the policy is available.
	 *
	 * @param policyModel
	 *           policy to be evaluated
	 * @return true if the policy is available, false otherwise
	 */
	protected boolean isPolicyAvailable(final TmaCompatibilityPolicyModel policyModel)
	{
		if (policyModel.getOnlineFrom() == null && policyModel.getOnlineTo() == null)
		{
			return true;
		}
		final Date currentDate = new Date();
		if (policyModel.getOnlineTo() == null)
		{
			return policyModel.getOnlineFrom() != null && !policyModel.getOnlineFrom().after(currentDate);
		}
		if (policyModel.getOnlineFrom() == null)
		{
			return policyModel.getOnlineTo() != null && !policyModel.getOnlineTo().before(currentDate);
		}
		return !policyModel.getOnlineFrom().after(currentDate) && !policyModel.getOnlineTo().before(currentDate);
	}

	private boolean isPolicyOfType(final TmaCompatibilityPolicyModel policyModel,
			final Set<TmaCompatibilityPolicyActionType> actionTypes)
	{
		return policyModel.getActions().stream().anyMatch(action -> actionTypes.contains(action.getActionType()));
	}

	/**
	 * Recursive method which iterates over all the parents of the given {@link TmaPolicyConditionModel} and adds the
	 * associated {@link TmaCompatibilityPolicyModel} to the input collection.
	 *
	 * @param policyCondition
	 *           {@link TmaPolicyConditionModel} for which policies and parents' policies need to be retrieved
	 * @param compatibilityPolicies
	 *           the collection of {@link TmaCompatibilityPolicyModel} retrieved for given policy conditions and parent
	 *           conditions.
	 */
	protected void addPolicies(final TmaPolicyConditionModel policyCondition,
			final Set<TmaCompatibilityPolicyModel> compatibilityPolicies)
	{
		compatibilityPolicies.addAll(policyCondition.getPolicies());

		if (CollectionUtils.isNotEmpty(policyCondition.getParents()))
		{
			policyCondition.getParents().forEach(parentPolicies -> addPolicies(parentPolicies, compatibilityPolicies));
		}
	}

	protected List<TmaPolicyConditionDao> getPolicyConditionDaos()
	{
		return policyConditionDaos;
	}

	public void setPolicyConditionDaos(final List<TmaPolicyConditionDao> policyConditionDaos)
	{
		this.policyConditionDaos = policyConditionDaos;
	}
}
