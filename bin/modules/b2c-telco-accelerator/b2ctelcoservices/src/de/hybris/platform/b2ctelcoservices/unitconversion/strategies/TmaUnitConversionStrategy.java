/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.unitconversion.strategies;

import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.math.BigDecimal;


/**
 * This strategy is designed to for unit level conversion.
 *
 * @since 6.7
 */
public interface TmaUnitConversionStrategy
{
	/**
	 * Unit conversion strategy service for given fromUsageUnit to given toUsageUnit, for given fromUsageUnitValue
	 *
	 * @param fromUsageUnit
	 *           the UsageUnitModel for the unit needs to converted
	 * @param toUsageUnit
	 *           the UsageUnitModel to which value needs to converted
	 * @param fromUsageUnitValue
	 *           the value of the unit needs to converted to toUsageUnit
	 * @return {@link BigDecimal} object after conversion
	 */
	BigDecimal getUnitConversion(final UsageUnitModel fromUsageUnit, final UsageUnitModel toUsageUnit,
			final BigDecimal fromUsageUnitValue);
}
