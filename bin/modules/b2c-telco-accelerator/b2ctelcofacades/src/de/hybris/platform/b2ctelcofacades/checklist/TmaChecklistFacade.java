/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.checklist;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionParamData;

import java.util.Set;


/**
 * Implements functionality for the checklistAction policies.
 *
 * @since 1907
 */
public interface TmaChecklistFacade
{
	/**
	 * Retrieves checklist actions for a specific checklist action parameter
	 *
	 * @param tmaChecklistActionParamData
	 * 		the input parameter object
	 * @return the checklist actions data.
	 */
	Set<TmaChecklistActionData> findActions(final TmaChecklistActionParamData tmaChecklistActionParamData);
}
