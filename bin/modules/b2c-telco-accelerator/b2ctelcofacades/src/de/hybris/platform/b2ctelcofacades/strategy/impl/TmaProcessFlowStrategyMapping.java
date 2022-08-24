/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy.impl;

import de.hybris.platform.b2ctelcofacades.strategy.TmaProcessFlowStrategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Handles mapping between process flow type and its strategy implementation.
 *
 * @since 6.7
 */
public class TmaProcessFlowStrategyMapping
{
	private Map<String, TmaProcessFlowStrategy> strategyMap;

	/**
	 * @return map containing all flow strategies.
	 */
	public Map<String, TmaProcessFlowStrategy> getStrategyMap()
	{
		return strategyMap;
	}

	@Required
	public void setStrategyMap(Map<String, TmaProcessFlowStrategy> strategyMap)
	{
		this.strategyMap = strategyMap;
	}
}
