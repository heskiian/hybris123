/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;


import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;

import java.util.List;


/**
 * Data Access Object for operations related to the {@link TmaPolicyConditionModel} type.
 *
 * @since 6.7
 */

public interface TmaPolicyConditionDao
{
	/**
	 * Retrieves policy conditions found for the given parameters (product offering, group,etc.)
	 *
	 * @param context
	 *           input parameters used for searching conditions
	 * @return the list of corresponding {@link TmaPolicyConditionModel}
	 */
	List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext context);
}
