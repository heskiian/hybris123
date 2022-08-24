/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyService;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyDao;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Set;


/**
 * Default implementation of {@link TmaPolicyService}
 *
 * @since 1810
 */
public class DefaultTmaPolicyService implements TmaPolicyService
{
	private ModelService modelService;
	private TmaPolicyDao tmaPolicyDao;

	@Override
	public void addNewCondition(final TmaCompositePolicyConditionModel parentCondition,
			final TmaPolicyConditionModel childCondition)
	{
		final Set<TmaPolicyConditionModel> childrenConditions = new HashSet<>();
		childrenConditions.addAll(parentCondition.getChildren());
		childrenConditions.add(childCondition);
		parentCondition.setChildren(childrenConditions);
		getModelService().save(parentCondition);
		getModelService().refresh(parentCondition);
		getModelService().save(childCondition);
		getModelService().refresh(childCondition);
	}

	@Override
	public void addNewCondition(final TmaCompatibilityPolicyModel policyModel,
			final TmaPolicyConditionModel childCondition)
	{
		final Set<TmaPolicyConditionModel> childrenConditions = new HashSet<>();
		childrenConditions.addAll(policyModel.getConditions());
		childrenConditions.add(childCondition);
		policyModel.setConditions(childrenConditions);
		getModelService().save(policyModel);
		getModelService().refresh(policyModel);
		getModelService().save(childCondition);
		getModelService().refresh(childCondition);
	}

	@Override
	public void removeCondition(final TmaCompositePolicyConditionModel parentCondition,
			final TmaPolicyConditionModel childCondition)
	{
		final Set<TmaPolicyConditionModel> childrenConditions = new HashSet<>();
		childrenConditions.addAll(parentCondition.getChildren());
		childrenConditions.remove(childCondition);
		parentCondition.setChildren(childrenConditions);
		getModelService().save(parentCondition);
		getModelService().refresh(parentCondition);
		getModelService().save(childCondition);
		getModelService().refresh(childCondition);
	}

	@Override
	public void removeCondition(final TmaCompatibilityPolicyModel policy, final TmaPolicyConditionModel condition)
	{
		Set<TmaPolicyConditionModel> policyConditions = new HashSet<>();
		policyConditions.addAll(policy.getConditions());
		policyConditions.remove(condition);
		policy.setConditions(policyConditions);
		getModelService().save(policy);
		getModelService().refresh(policy);
		getModelService().save(condition);
		getModelService().refresh(condition);
	}

	@Override
	public Set<TmaCompatibilityPolicyModel> getAllPolicies(final TmaPolicyConditionModel conditionModel)
	{
		return getTmaPolicyDao().gatherAllPolicies().apply(conditionModel);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected TmaPolicyDao getTmaPolicyDao()
	{
		return tmaPolicyDao;
	}

	public void setTmaPolicyDao(TmaPolicyDao tmaPolicyDao)
	{
		this.tmaPolicyDao = tmaPolicyDao;
	}
}
