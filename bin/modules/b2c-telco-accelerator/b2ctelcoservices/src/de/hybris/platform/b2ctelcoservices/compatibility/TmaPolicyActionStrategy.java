/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility;

import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;

import java.util.Set;


/**
 * Strategy to process policy actions
 *
 * @since 1911
 */
public interface TmaPolicyActionStrategy
{
	/**
	 * Process the actions corresponding to the given results
	 *
	 * @param results
	 * 		the given rule evaluation results
	 */
	void processActions(final Set<RuleEvaluationResult> results);
}
