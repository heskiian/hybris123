/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.validators.TmaPolicyPscvValueComparator;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;


/**
 * Checks whether a string ProductSpecificationCharacteristicValue matches a given value.
 *
 * @since 1805
 */
public class TmaPolicyPscvStringComparator implements TmaPolicyPscvValueComparator
{
	@Override
	public boolean compare(final String prodPscvValue, final UsageUnitModel prodPscvUnit, final String stMin, final String stMax,
			final UsageUnitModel stUnit)
	{
		return prodPscvValue.equalsIgnoreCase(stMin);
	}
}
