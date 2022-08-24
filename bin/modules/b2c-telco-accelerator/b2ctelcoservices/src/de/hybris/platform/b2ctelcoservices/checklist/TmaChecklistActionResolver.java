/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;

import java.util.List;


/**
 * Strategy to resolve checklist actions against a list of contexts
 *
 * @since 1911
 */
public interface TmaChecklistActionResolver
{

	/**
	 * Resolve the action against the list of contexts
	 *
	 * @param action
	 * 		the action to be resolved
	 * @param contexts
	 * 		the contexts for which the action will be resolved
	 */
	void resolveAction(final TmaPolicyActionModel action, final List<TmaPolicyContext> contexts);
}
