/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators;

import de.hybris.platform.subscriptionservices.model.UsageUnitModel;


/**
 * Checks whether a ProductSpecificationCharacteristicValue is within a range.
 *
 * @since 1805
 */
public interface TmaPolicyPscvValueComparator
{
	/**
	 * Checks whether a ProductSpecificationCharacteristicValue is within a range.
	 *
	 * @param prodPscvValue
	 * 		the value to be checked
	 * @param prodPscvUnit
	 * 		the unit for the value to be checked
	 * @param stMin
	 * 		the lower value of the range
	 * @param stMax
	 * 		the higher value of the range
	 * @param stUnit
	 * 		the unit for the range
	 * @return true if the value is within the range; false otherwise
	 */
	boolean compare(String prodPscvValue, UsageUnitModel prodPscvUnit, String stMin, String stMax, UsageUnitModel stUnit);
}
