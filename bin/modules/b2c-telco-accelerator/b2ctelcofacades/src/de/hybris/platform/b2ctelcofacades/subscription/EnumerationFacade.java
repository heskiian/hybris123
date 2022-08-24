/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription;

import de.hybris.platform.b2ctelcofacades.data.EnumerationValueData;

import java.util.List;


/**
 * Facade used for enumeration related operations.
 *
 * @since 6.6
 */
public interface EnumerationFacade
{
	/**
	 * Returns list containing all enumeration values based on enumeration type code specified in facade's constructor
	 *
	 * @return {@link List} of {@link EnumerationValueData}
	 */
	List<EnumerationValueData> getValues();
}
