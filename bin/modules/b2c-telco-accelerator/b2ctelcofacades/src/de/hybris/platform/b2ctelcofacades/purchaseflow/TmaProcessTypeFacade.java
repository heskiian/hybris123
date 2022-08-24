/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.purchaseflow;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;

import java.util.Collection;
import java.util.List;


/**
 * Facade manipulating data and processes involved in available process types (Retention, Acquistion etc)
 *
 * @since 6.7
 */
public interface TmaProcessTypeFacade
{

	/**
	 * Obtains the list of process types names from the given collection of process type enums.
	 *
	 * @param processTypesEnumCollection
	 * 		the collection of purchase flow enums
	 * @return the list of process type names corresponding to the given enum
	 */
	List<String> getProcessTypeNamesFromEnums(final Collection<TmaProcessType> processTypesEnumCollection);

	/**
	 * Returns a process type given a code.
	 *
	 * @param code
	 * 		the code of the processType.
	 * @return The ProcessType object.
	 */
	TmaProcessType getProcessType(final String code);
}
