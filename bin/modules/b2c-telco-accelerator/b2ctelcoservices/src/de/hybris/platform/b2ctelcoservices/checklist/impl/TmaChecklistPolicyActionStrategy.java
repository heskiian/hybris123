/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;

import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaChecklistActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaChecklistPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Strategy to process checklist actions
 *
 * @since 1911
 */
public class TmaChecklistPolicyActionStrategy implements TmaPolicyActionStrategy
{
	private Map<TmaChecklistActionType, TmaChecklistActionResolver> actionResolverMap;

	public TmaChecklistPolicyActionStrategy(Map<TmaChecklistActionType, TmaChecklistActionResolver> actionResolverMap)
	{
		this.actionResolverMap = actionResolverMap;
	}

	@Override
	public void processActions(final Set<RuleEvaluationResult> results)
	{
		results.forEach(result -> {
			final TmaPolicyActionModel action = result.getAction();
			final List<TmaPolicyContext> contexts = result.getContexts();
			if (CollectionUtils.isNotEmpty(contexts))
			{
				final TmaChecklistActionType actionType = ((TmaChecklistPolicyStatementModel) action.getStatement()).getType();
				getActionResolverMap().get(actionType).resolveAction(action, contexts);
			}
		});
	}

	protected Map<TmaChecklistActionType, TmaChecklistActionResolver> getActionResolverMap()
	{
		return actionResolverMap;
	}
}
