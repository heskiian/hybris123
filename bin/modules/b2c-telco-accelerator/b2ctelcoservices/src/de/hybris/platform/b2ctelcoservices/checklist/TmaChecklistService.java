/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist;

import de.hybris.platform.b2ctelcoservices.checklist.context.TmaChecklistContext;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.Set;


/**
 * Service handling operations specific to checklist actions.
 *
 * @since 1907
 */
public interface TmaChecklistService
{

	/**
	 * Finds the checklist actions for a specific checklistContext
	 *
	 * @param checklistContext
	 * 		the checklistContext
	 * @return the {@link RuleEvaluationResult}s for the given checklistContext
	 */
	Set<RuleEvaluationResult> findActions(final TmaChecklistContext checklistContext);

	/**
	 * Verifies if checklist actions corresponding to the order are fulfilled or not
	 *
	 * @param abstractOrderModel
	 * 		the order for which the actions are verified
	 * @return true, if the checklist actions for the order are fulfilled, otherwise false
	 */
	boolean areActionsFulfilled(final AbstractOrderModel abstractOrderModel);
}
